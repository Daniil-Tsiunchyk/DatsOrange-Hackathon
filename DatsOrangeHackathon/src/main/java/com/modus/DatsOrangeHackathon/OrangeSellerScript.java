package com.modus.DatsOrangeHackathon;

import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.MediaType;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import static com.modus.DatsOrangeHackathon.Const.*;
import static com.modus.DatsOrangeHackathon.OrangeInfoScript.getAccountInfoJson;

public class OrangeSellerScript {

    public static void main(String[] args) {
        AtomicInteger loopCounter = new AtomicInteger(0);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Количество выполненных циклов: " + loopCounter.get())));

        String json = getAccountInfoJson();

        Gson gson = new Gson();
        AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);
        displayOrangesQuantity(accountInfo);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loopCounter.incrementAndGet();
                try {
                    AccountInfo accountInfo = getAccountInfo();
                    if (accountInfo != null && accountInfo.getAssets() != null) {
                        for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                            if (asset.getQuantity() > 0 && asset.getId() != 1) {
                                if (EXCLUDED_IDS.contains(asset.getId())) {
                                    placeSellOrder(asset.getId(), worstAssetSellPrice, asset.getQuantity());
                                } else {
                                    placeSellOrder(asset.getId(), orderSellPrice, asset.getQuantity());
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 100);  // Запустить каждую секунду
    }


    public static void placeSellOrder(int assetId, long price, int quantity) throws IOException, InterruptedException {
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
                System.out.println("\nОрдер на продажу успешно размещен.");
                System.out.println("ID актива: " + assetId);
                System.out.println("Цена за акцию: " + price);
                System.out.println("Количество: " + quantity);
                System.out.println("Общая сумма: " + (price * quantity));
            } else {
                System.out.println("Не удалось разместить ордер на продажу для ID актива " + assetId);
                System.out.println("Код ответа: " + response.code());
                System.out.println("Сообщение ответа: " + response.message());
                if (response.body() != null) {
                    System.out.println("Тело ответа: " + response.body().string());
                }
            }
        }

        // Задержка в 1 секунду перед следующей операцией
        Thread.sleep(100);
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