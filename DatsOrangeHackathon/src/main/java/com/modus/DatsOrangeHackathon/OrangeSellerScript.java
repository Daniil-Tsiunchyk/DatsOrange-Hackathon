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
                    // Получить информацию об активах
                    AccountInfo accountInfo = OrangeInfoScript.getAccountInfo();
                    if (accountInfo != null && accountInfo.getAssets() != null) {
                        for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                            if (asset.getQuantity() != 0)
                                placeSellOrder(asset.getId(), orderSellPrice, asset.getQuantity());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 100);  // Минута - 1000

    }


    public static void placeSellOrder(int assetId, long price, int quantity) throws IOException, InterruptedException {
        if (quantity <= 0) {
            System.out.println("Невозможно разместить ордер на продажу для assetId " + assetId + ". Количество должно быть больше 0.");
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
                System.out.println("Ордер на продажу успешно размещен для assetId " + assetId);
            } else {
                System.out.println("Не удалось разместить ордер на продажу для assetId " + assetId);
                System.out.println("Код ответа: " + response.code());
                System.out.println("Сообщение ответа: " + response.message());
                if (response.body() != null) {
                    System.out.println("Тело ответа: " + response.body().string());
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
}
