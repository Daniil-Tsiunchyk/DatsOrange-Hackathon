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
    static final long orderSellPrice = 10; //цена продажи
    static final long orderBuyPrice = 1; //цена покупки
    static final int buyLimit = 100; //лимит однократной покупки
    static final List<Integer> EXCLUDED_IDS = Arrays.asList(100, 150, 200);//id мусорных компаний
    static final int worstAssetSellPrice = 60; //цена продажи мусорных компаний
}
