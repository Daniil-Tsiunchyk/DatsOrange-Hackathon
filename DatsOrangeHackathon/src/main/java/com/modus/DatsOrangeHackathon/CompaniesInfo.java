package com.modus.DatsOrangeHackathon;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.modus.DatsOrangeHackathon.Const.*;

public class CompaniesInfo {

    @Getter
    @Setter
    public static class News {
        @SerializedName("date")
        private String date;
        @SerializedName("text")
        private String text;
        @SerializedName("rate")
        private int rate;
        @SerializedName("companiesAffected")
        private List<String> companiesAffected;
    }

    @Getter
    @Setter
    public static class Company {
        @SerializedName("id")
        private int id;
        @SerializedName("ticker")
        private String ticker;
    }

    public static class AffectedCompany {
        private final int id;
        private final String name;
        private double priceFactor;

        public AffectedCompany(int id, String name) {
            this.id = id;
            this.name = name;
            this.priceFactor = 1;
        }

        public void changePriceFactor(double rate) {
            this.priceFactor *= (1 + rate / 100);
        }
    }

    public News getLastNews() {
        News latestNews = null;
        Request request = new Request.Builder()
                .url(baseUrl + "/LatestNews")
                .addHeader("token", TOKEN)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                latestNews = gson.fromJson(responseBody, News.class);
            } else {
                System.out.println("Не удалось получить ордеры на продажу. Код ответа: " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return latestNews;
    }

    public static List<Company> getAllCompanies() throws IOException {
        List<Company> companies = new ArrayList<>();

        Request request = new Request.Builder()
                .url(baseUrl + "/getSymbols")
                .addHeader("token", TOKEN)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                Company[] comps = gson.fromJson(responseBody, Company[].class);
                companies.addAll(Arrays.asList(comps));
            } else {
                System.out.println("Не удалось получить ордеры на продажу. Код ответа: " + response.code());
            }
        }
        return companies;
    }

    public static void main(String[] args) {

    }

}
