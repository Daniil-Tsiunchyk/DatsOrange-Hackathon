package com.modus.DatsOrangeHackathon;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountInfo {
    private Account account;
    private List<Bid> bids; // Ставки
    private List<Asset> assets; //Активы
    private List<FrozenAsset> frozenAssets; // Замороженные активы

    @Getter
    @Setter
    public static class Account {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;
    }

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

    @Getter
    @Setter
    public static class Asset {
        private int id;
        private String name;
        private int quantity;

    }

    @Getter
    @Setter
    public static class FrozenAsset {
        private int id;
        private String name;
        private int quantity;
    }
}
