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
    static final long orderSellPrice = 300;
    static final long orderBuyPrice = 50;
    static final int buyLimit = 25;
    static final List<Integer> EXCLUDED_IDS = Arrays.asList(356, 76, 452, 38, 286, 279, 262);
    static final int worstAssetSellPrice = 50;

}
