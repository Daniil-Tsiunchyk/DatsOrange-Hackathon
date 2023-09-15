package com.modus.DatsOrangeHackathon;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.springframework.web.client.RestTemplate;

public class Const {
    static final String TOKEN = "64f38d2665df964f38d2665dfd";
    static final OkHttpClient client = new OkHttpClient();
    static final RestTemplate restTemplate = new RestTemplate();
    static final String baseUrl = "https://datsorange.devteam.games";
    static final Gson gson = new Gson();
    static final long orderPrice = 100;
}
