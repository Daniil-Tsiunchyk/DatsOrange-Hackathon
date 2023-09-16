package com.modus.DatsOrangeHackathon;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class Const {
    static final String TOKEN = "64f38d2665df964f38d2665dfd";
    static final OkHttpClient client = new OkHttpClient();
    static final RestTemplate restTemplate = new RestTemplate();
    static final String baseUrl = "https://datsorange.devteam.games";
    static final Gson gson = new Gson();
    static final long orderSellPrice = 65;
    static final long orderBuyPrice = 15;
    static final int buyLimit = 100;
    static final List<Integer> EXCLUDED_IDS = Arrays.asList(200010, 200050);
    static final int worstAssetSellPrice = 60;
}
