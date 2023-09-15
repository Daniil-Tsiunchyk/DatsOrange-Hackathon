package com.modus.DatsOrangeHackathon;

import okhttp3.*;
import okhttp3.MediaType;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.modus.DatsOrangeHackathon.Const.*;

public class OrangeSellerScript {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Получить информацию об акциях
                    AccountInfo accountInfo = InfoScript.getAccountInfo();
                    if (accountInfo != null && accountInfo.getAssets() != null) {
                        for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                            if (asset.getQuantity() != 0)
                                // Выставляем каждую акцию на продажу за 1000
                                placeSellOrder(asset.getId(), orderPrice, asset.getQuantity());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 1000);  // Запустить каждую минуту

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
                .url(baseUrl + "/LimitPriceSell")
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


    public static class SellOrderRequest {
        public SellOrderRequest(int symbolId, long price, int quantity) {
        }
    }

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
