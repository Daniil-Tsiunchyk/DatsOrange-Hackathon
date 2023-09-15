package com.modus.DatsOrangeHackathon;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.*;

import java.io.IOException;

import static com.modus.DatsOrangeHackathon.Const.*;

public class OrangeInfoScript {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            String json = getAccountInfoJson();

            AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);
            if (accountInfo != null && accountInfo.getAccount() != null) {
                System.out.println("ID аккаунта: " + accountInfo.getAccount().getId());
                System.out.println("Команда: " + accountInfo.getAccount().getName());
            }
            if (accountInfo != null && accountInfo.getAccount() != null) {

                int totalOrders = 0;
                double totalPrice = 0;
                int totalAssets = 0;
                int totalCompaniesWithShares = 0;
                int totalFrozenAssets = 0;

                // Суммарное количество и цена ордеров
                for (AccountInfo.Bid bid : accountInfo.getBids()) {
                    totalOrders++;
                    totalPrice += bid.getPrice();
                }

                // Суммарное количество активов и среднее количество на компанию
                for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                    if (asset.getId() != 1) { // Исключаем активы с id = 1
                        totalAssets += asset.getQuantity();
                        if (asset.getQuantity() > 0) {
                            totalCompaniesWithShares++;
                        }
                    }
                }

                double averageAssetsPerCompany = (totalCompaniesWithShares == 0) ? 0 : (double) totalAssets / totalCompaniesWithShares;

                // Суммарное количество замороженных активов
                for (AccountInfo.FrozenAsset frozenAsset : accountInfo.getFrozenAssets()) {
                    totalFrozenAssets += frozenAsset.getQuantity();
                }

                System.out.println("\n=================== Информация об аккаунте ===================");
                for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                    if ("Oranges".equalsIgnoreCase(asset.getName())) {
                        System.out.println("  Текущее количество апельсинов: " + asset.getQuantity());
                    }
                }
                System.out.println("Всего ордеров: " + totalOrders);
                System.out.println("Суммарная цена ордеров: " + totalPrice);
                System.out.println("Всего активов: " + totalAssets);
                System.out.println("Количество компаний с акциями > 0: " + totalCompaniesWithShares);
                System.out.println("Среднее количество активов на компанию: " + averageAssetsPerCompany);
                System.out.println("Всего замороженных активов: " + totalFrozenAssets);
                System.out.println("\n============================================================");
            }
            Thread.sleep(5000);            // Проверка раз в 5 секунд

        }
    }

    public static String getAccountInfoJson() {
        String url = baseUrl + "/info";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public static AccountInfo getAccountInfo() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/info")
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
}
