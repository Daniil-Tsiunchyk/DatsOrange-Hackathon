package com.modus.DatsOrangeHackathon;

import lombok.Getter;
import lombok.Setter;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.modus.DatsOrangeHackathon.Const.*;

public class OrangeBuyerScript {


    public static void main(String[] args) throws IOException, InterruptedException {
        while (true) {
            System.out.println("Starting the OrangeBuyerScript.");
            System.out.println("Starting to analyze and buy oranges.");
            List<SellOrder> sellOrders = getSellOrders();
            System.out.println("Received " + sellOrders.size() + " sell orders.");

            for (SellOrder order : sellOrders) {
                if (order.getPrice() <= 5) {
                    System.out.println("Attempting to place buy order for assetId " + order.getSymbolId());
                    placeBuyOrder(order.getSymbolId(), order.getPrice(), order.getQuantity());
                } else {
                    System.out.println("Skipping assetId " + order.getSymbolId() + " as price is " + order.getPrice());
                }
            }
            System.out.println("Finished analyzing and buying oranges.");

            System.out.println("Finished the OrangeBuyerScript.");
            try {
                Thread.sleep(1000);  // Задержка на 1 секунду
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static class BuyOrderRequest {
        int symbolId;
        int price;
        int quantity;

        public BuyOrderRequest(int symbolId, int price, int quantity) {
            this.symbolId = symbolId;
            this.price = price;
            this.quantity = quantity;
        }
    }

    @Getter
    @Setter
    public static class SellOrder {
        private int symbolId;
        private int price;
        private int quantity;
    }

    public static void placeBuyOrder(int symbolId, int price, int quantity) throws InterruptedException {

        String jsonBody = gson.toJson(new BuyOrderRequest(symbolId, price, quantity));

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url("https://datsorange.devteam.games/LimitPriceBuy")
                .addHeader("token", TOKEN)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Buy order placed successfully for assetId " + symbolId);
            } else {
                System.out.println("Failed to place buy order for assetId " + symbolId);
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());
                if (response.body() != null) {
                    System.out.println("Response body: " + response.body().string());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(1000);
    }

    @Getter
    @Setter
    public static class Bid {
        private long price;
        private int quantity;
    }

    @Getter
    @Setter
    public static class Stock {
        private int id;
        private String ticker;
        private List<Bid> bids;
    }

    public static List<SellOrder> getSellOrders() throws IOException {
        List<SellOrder> sellOrders = new ArrayList<>();

        Request request = new Request.Builder()
                .url("https://datsorange.devteam.games/sellStock")
                .addHeader("token", TOKEN)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                Stock[] stocks = gson.fromJson(responseBody, Stock[].class);
                for (Stock stock : stocks) {
                    for (Bid bid : stock.getBids()) {
                        SellOrder sellOrder = new SellOrder();
                        sellOrder.setSymbolId(stock.getId());
                        sellOrder.setPrice((int) bid.getPrice());
                        sellOrder.setQuantity(bid.getQuantity());
                        sellOrders.add(sellOrder);
                    }
                }
            } else {
                System.out.println("Failed to get sell orders.");
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());
            }
        }

        return sellOrders;
    }
}
