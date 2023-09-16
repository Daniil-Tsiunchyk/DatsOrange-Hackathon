package com.modus.DatsOrangeHackathon;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static com.modus.DatsOrangeHackathon.Const.*;
import static com.modus.DatsOrangeHackathon.OrangeBuyerScript.getSellOrders;

public class OrangeInfoScript {
    public static void main(String[] args) throws InterruptedException, IOException {
        while (true) {
            try {
                String json = getAccountInfoJson();
//
                AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);
//                if (accountInfo != null && accountInfo.getAccount() != null) {
//                    System.out.println("ID аккаунта: " + accountInfo.getAccount().getId());
//                    System.out.println("Команда: " + accountInfo.getAccount().getName());
//                }
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

//                    System.out.println("\n=================== Информация об аккаунте ===================");
//                    for (AccountInfo.Asset asset : accountInfo.getAssets()) {
//                        if ("Oranges".equalsIgnoreCase(asset.getName())) {
//                            System.out.println("  Текущее количество апельсинов: " + asset.getQuantity());
//                        }
//                    }
//                    System.out.println("Всего ордеров: " + totalOrders);
//                    System.out.println("Суммарная цена ордеров: " + totalPrice);
//                    System.out.println("Всего активов: " + totalAssets);
//                    System.out.println("Количество компаний с акциями > 0: " + totalCompaniesWithShares);
//                    System.out.println("Среднее количество активов на компанию: " + averageAssetsPerCompany);
//                    System.out.println("Всего замороженных активов: " + totalFrozenAssets);
//                    System.out.println("\n============================================================");

                    List<OrangeBuyerScript.SellOrder> sellOrders = getSellOrders();
                    sellOrders.sort(Comparator.comparingDouble(OrangeBuyerScript.SellOrder::getPrice));
                    int below100 = 0;
                    int between100below125 = 0;
                    int between125below133 = 0;
                    int between133below150 = 0;
                    int between150below200 = 0;
                    int between200below500 = 0;
                    int between500below1000 = 0;
                    int between1000and2000 = 0;
                    int above2000 = 0;

                    for (OrangeBuyerScript.SellOrder order : sellOrders) {
                        int price = order.getPrice();
                        if (price <= 100) {
                            below100 += order.getQuantity();
                        } else if (price <= 125) {
                            between100below125 += order.getQuantity();
                        } else if (price <= 133) {
                            between125below133 += order.getQuantity();
                        } else if (price <= 150) {
                            between133below150 += order.getQuantity();
                        } else if (price <= 200) {
                            between150below200 += order.getQuantity();
                        } else if (price <= 500) {
                            between200below500 += order.getQuantity();
                        } else if (price <= 1000) {
                            between500below1000 += order.getQuantity();
                        } else if (price <= 2000) {
                            between1000and2000 += order.getQuantity();
                        } else {
                            above2000 += order.getQuantity();
                        }
                    }

//                    System.out.println("\n========== Информация о продажных ордерах ==========");
                    if (below100 > 0)
                        System.out.println("Количество акций с ценой до 100: " + below100);
                    if (between100below125 > 0)
                        System.out.println("Количество акций с ценой от 100 до 125: " + between100below125);
                    if (between125below133 > 0)
                        System.out.println("Количество акций с ценой от 125 до 133: " + between125below133);
                    if (between133below150 > 0)
                        System.out.println("Количество акций с ценой от 133 до 150: " + between133below150);
                    if (between150below200 > 0)
                        System.out.println("Количество акций с ценой от 150 до 200: " + between150below200);
                    if (between200below500 > 0)
                        System.out.println("Количество акций с ценой от 200 до 500: " + between200below500);
                    if (between500below1000 > 0)
                        System.out.println("Количество акций с ценой от 500 до 1000: " + between500below1000);
                    if (between1000and2000 > 0)
                        System.out.println("Количество акций с ценой от 1000 до 2000: " + between1000and2000);
                    if (above2000 > 0)
                        System.out.println("Количество акций с ценой >= 2000: " + above2000);
                    System.out.println("\n");


                }
            } catch (HttpClientErrorException.TooManyRequests e) {
//                System.out.println("Слишком много запросов. Подождем перед следующим запросом.");
                Thread.sleep(10000);  // подождать 10 секунд перед повторением
            } catch (Exception e) {
                e.printStackTrace();  // другие исключения
            }
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
