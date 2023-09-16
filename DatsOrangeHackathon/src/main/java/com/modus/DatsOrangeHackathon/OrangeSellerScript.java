package com.modus.DatsOrangeHackathon;

import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.MediaType;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.modus.DatsOrangeHackathon.Const.orderSellPrice;

public class OrangeSellerScript {
    public static final String TOKEN = "64f38d2665df964f38d2665dfd";
    public static final OkHttpClient client = new OkHttpClient();
    public static final Gson gson = new Gson();

    public static void main(String[] args) {
        String json = getAccountInfoJson();

        Gson gson = new Gson();
        AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);
        displayOrangesQuantity(accountInfo);
//        seeOranges(accountInfo, gson);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Получить информацию об акциях
                    AccountInfo accountInfo = getAccountInfo();
                    if (accountInfo != null && accountInfo.getAssets() != null) {
                        for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                            // Выставляем каждую акцию на продажу за 1000

                            placeSellOrder(asset.getId(), orderSellPrice, asset.getQuantity());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 60000);  // Запустить каждую минуту

    }

    public static String getAccountInfoJson() {
        String url = baseUrl + "/info";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public static void placeSellOrder(int assetId, long price, int quantity) throws IOException, InterruptedException {
        if (quantity <= 0) {
            System.out.println("Cannot place sell order for assetId " + assetId + ". Quantity should be greater than 0.");
            return;
        }

        // Формирование тела запроса
        String jsonBody = gson.toJson(new SellOrderRequest(assetId, price, quantity));

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url("https://datsorange.devteam.games/LimitPriceSell")
                .addHeader("token", TOKEN)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Sell order placed successfully for assetId " + assetId);
            } else {
                System.out.println("Failed to place sell order for assetId " + assetId);
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());
                if (response.body() != null) {
                    System.out.println("Response body: " + response.body().string());
                }
            }
        }

        // Задержка в 1 секунду перед следующей операцией
        Thread.sleep(1000);
    }


    public static AccountInfo getAccountInfo() throws IOException {
        Request request = new Request.Builder()
                .url("https://datsorange.devteam.games/info")
                .addHeader("TOKEN", TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String jsonData = response.body().string();
                return gson.fromJson(jsonData, AccountInfo.class);
            }
        }
        return null;
    }


    public static void seeOranges(AccountInfo accountInfo, Gson gson) {
        if (accountInfo != null) {
            System.out.println("\n=================== Account Info ===================");
            System.out.println("Account ID: " + accountInfo.getAccount().getId());
            System.out.println("Account Name: " + accountInfo.getAccount().getName());

            System.out.println("\n------------------- Bids -------------------");
            for (AccountInfo.Bid bid : accountInfo.getBids()) {
                System.out.println("Bid ID: " + bid.getId());
                System.out.println("  Symbol ID: " + bid.getSymbolId());
                System.out.println("  Price: " + bid.getPrice());
                System.out.println("  Type: " + bid.getType());
                System.out.println("  Create Date: " + bid.getCreateDate());
            }

            System.out.println("\n------------------- Assets -------------------");
            for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                System.out.println("Asset ID: " + asset.getId());
                System.out.println("  Name: " + asset.getName());
                System.out.println("  Quantity: " + asset.getQuantity());
            }

            System.out.println("\n------------------- Frozen Assets -------------------");
            for (AccountInfo.FrozenAsset frozenAsset : accountInfo.getFrozenAssets()) {
                System.out.println("Frozen Asset ID: " + frozenAsset.getId());
                System.out.println("  Name: " + frozenAsset.getName());
                System.out.println("  Quantity: " + frozenAsset.getQuantity());
            }

            System.out.println("\n====================================================");
        }

        String prettyJson = gson.toJson(accountInfo);
        System.out.println(prettyJson);
    }

    public static class SellOrderRequest {
        private int symbolId;
        private long price;
        private int quantity;

        public SellOrderRequest(int symbolId, long price, int quantity) {
            this.symbolId = symbolId;
            this.price = price;
            this.quantity = quantity;
        }
    }

    private static final String token = "64f38d2665df964f38d2665dfd";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String baseUrl = "https://datsorange.devteam.games";

    public static void displayOrangesQuantity(AccountInfo accountInfo) {
        if (accountInfo != null && accountInfo.getAssets() != null) {
            for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                if ("Oranges".equalsIgnoreCase(asset.getName())) {
                    System.out.println("Asset ID: " + asset.getId());
                    System.out.println("  Name: " + asset.getName());
                    System.out.println("  Quantity: " + asset.getQuantity());
                    return;
                }
            }
        }
        System.out.println("Oranges asset not found.");
    }

}