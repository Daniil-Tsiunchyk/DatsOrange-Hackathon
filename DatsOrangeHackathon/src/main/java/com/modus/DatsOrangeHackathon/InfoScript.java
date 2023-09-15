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
            System.out.println("\n=================== Account Info ===================");
            System.out.println("Account ID: " + accountInfo.getId());
            System.out.println("Account Name: " + accountInfo.getName());

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

        System.out.println(gson.toJson(accountInfo));
    }
}
