package com.modus.DatsOrangeHackathon;

import com.google.gson.Gson;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.*;

import java.io.IOException;

import static com.modus.DatsOrangeHackathon.Const.*;
import static com.modus.DatsOrangeHackathon.Const.restTemplate;
import static com.modus.DatsOrangeHackathon.OrangeSellerScript.*;

public class InfoScript {
    public static void main(String[] args) {
        String json = getAccountInfoJson();

        AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);
        displayOrangesQuantity(accountInfo);
        seeOranges(accountInfo, gson);
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

    public static void seeOranges(AccountInfo accountInfo, Gson gson) {
        if (accountInfo != null) {
            System.out.println("\n=================== Информация об аккаунте ===================");
            System.out.println("ID аккаунта: " + accountInfo.getId());
            System.out.println("Имя аккаунта: " + accountInfo.getName());

            System.out.println("\n------------------- Ордеры -------------------");
            for (AccountInfo.Bid bid : accountInfo.getBids()) {
                System.out.println("ID ордера: " + bid.getId());
                System.out.println("  ID актива: " + bid.getSymbolId());
                System.out.println("  Цена: " + bid.getPrice());
                System.out.println("  Тип: " + bid.getType());
                System.out.println("  Дата создания: " + bid.getCreateDate());
            }

            System.out.println("\n------------------- Активы -------------------");
            for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                System.out.println("ID актива: " + asset.getId());
                System.out.println("  Имя: " + asset.getName());
                System.out.println("  Количество: " + asset.getQuantity());
            }

            System.out.println("\n------------------- Замороженные активы -------------------");
            for (AccountInfo.FrozenAsset frozenAsset : accountInfo.getFrozenAssets()) {
                System.out.println("ID замороженного актива: " + frozenAsset.getId());
                System.out.println("  Имя: " + frozenAsset.getName());
                System.out.println("  Количество: " + frozenAsset.getQuantity());
            }

            System.out.println("\n====================================================");
        }

        System.out.println(gson.toJson(accountInfo));
    }
}
