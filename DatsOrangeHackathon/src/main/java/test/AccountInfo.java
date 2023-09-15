package test;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountInfo {
    // Поле для хранения информации о счете
    private Account account;
    // Поле для хранения списка ставок
    private List<Bid> bids;
    // Поле для хранения списка активов
    private List<Asset> assets;
    // Поле для хранения списка замороженных активов
    private List<FrozenAsset> frozenAssets;

    // Геттеры и сеттеры
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List<FrozenAsset> getFrozenAssets() {
        return frozenAssets;
    }

    public void setFrozenAssets(List<FrozenAsset> frozenAssets) {
        this.frozenAssets = frozenAssets;
    }

    // Вложенный класс для информации о счете
    public static class Account {
        private int id;
        private String name;

        // Геттеры и сеттеры
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // Вложенный класс для ставок (пока пустой, так как в JSON нет данных)
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

        // Геттеры и сеттеры
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSymbolId() {
            return symbolId;
        }

        public void setSymbolId(int symbolId) {
            this.symbolId = symbolId;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }

    // Вложенный класс для активов
    public static class Asset {
        private int id;
        private String name;
        private int quantity;

        // Геттеры и сеттеры
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    // Вложенный класс для замороженных активов (предположительно аналогичен Asset)
    public static class FrozenAsset {
        // Так как структура предположительно аналогична Asset,
        // используем те же поля и геттеры/сеттеры
        private int id;
        private String name;
        private int quantity;

        // Геттеры и сеттеры
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
