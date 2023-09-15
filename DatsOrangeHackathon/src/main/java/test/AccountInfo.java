package test;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountInfo {
    // Поле для хранения информации о счете
    private Account account;
    // Поле для хранения списка ставок
    private List<Bid> bids;
    // Поле для хранения списка активов
    private List<Asset> assets;
    // Поле для хранения списка замороженных активов
    private List<FrozenAsset> frozenAssets;


    // Вложенный класс для информации о счете
    @Setter
    @Getter
    public static class Account {
        private int id;
        private String name;
    }

    // Вложенный класс для ставок (пока пустой, так как в JSON нет данных)
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

    // Вложенный класс для активов
    @Getter
    @Setter
    public static class Asset {
        private int id;
        private String name;
        private int quantity;

    }

    // Вложенный класс для замороженных активов (предположительно аналогичен Asset)
    @Getter
    @Setter
    public static class FrozenAsset {
        // Так как структура предположительно аналогична Asset,
        // используем те же поля и геттеры/сеттеры
        private int id;
        private String name;
        private int quantity;
    }
}
