package com.modus.DatsOrangeHackathon;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountInfo {
    // Поле для хранения информации о счете
    private int id;
    private String name;
    // Поле для хранения списка ставок
    private List<Bid> bids;
    // Поле для хранения списка активов
    private List<Asset> assets;
    // Поле для хранения списка замороженных активов
    private List<FrozenAsset> frozenAssets;

    // Ставки
    @Getter
    @Setter
    public static class Bid {
        @SerializedName("id")
        private int id;
        @SerializedName("symbolId")
        private int symbolId;
        @SerializedName("price")
        private int price;
        @SerializedName("type")
        private String type;
        @SerializedName("createDate")
        private String createDate;

    }

    //Активы
    @Getter
    @Setter
    public static class Asset {
        private int id;
        private String name;
        private int quantity;

    }

    // Замороженные активы
    @Getter
    @Setter
    public static class FrozenAsset {
        private int id;
        private String name;
        private int quantity;
    }
}
