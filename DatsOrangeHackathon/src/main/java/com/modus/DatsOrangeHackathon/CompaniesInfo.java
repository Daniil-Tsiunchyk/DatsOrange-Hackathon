package com.modus.DatsOrangeHackathon;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.modus.DatsOrangeHackathon.Const.*;

public class CompaniesInfo {

    public static final Map[] companies = new Map[2002];

    public class Company {
        public final int companyId;
        public double priceFactor;
        public final String companyName;

        public Company(int companyId, String companyName) {
            this.companyId = companyId;
            this.priceFactor = 1;
            this.companyName = companyName;
        }

        public void changePrice(double rate) {
            priceFactor *= (1 + rate / 100);
        }
    }

    public static Map[] getCompanies() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/getSymbols")
                .addHeader("token", TOKEN)
                .get()
                .build();
        Map[] companies = new Map[2002];
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;

            }
        }
        return companies;
    }

    public static void main(String[] args) {

    }

}
