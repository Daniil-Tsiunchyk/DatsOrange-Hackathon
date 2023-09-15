package test;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        OrangeTrader orangeTrader = new OrangeTrader();
        String json = orangeTrader.getAccountInfoJson();

        Gson gson = new Gson();
        AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);

        if (accountInfo != null) {
            System.out.println("\n=================== Account Info ===================");
            System.out.println("Account ID: " + accountInfo.getAccount().getId());
            System.out.println("Account Name: " + accountInfo.getAccount().getName());

            System.out.println("\n------------------- Bids -------------------");
            for (AccountInfo.Bid bid : accountInfo.getBids()) {
                System.out.println("Bid ID: " + bid.getId());
                System.out.println("  Symbol ID: " + bid.getSymbolId());
                System.out.println("  Price: " + bid.getPrice());
                System.out.println("  Type: " + bid.getType());
                System.out.println("  Create Date: " + bid.getCreateDate());
            }

            System.out.println("\n------------------- Assets -------------------");
            for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                System.out.println("Asset ID: " + asset.getId());
                System.out.println("  Name: " + asset.getName());
                System.out.println("  Quantity: " + asset.getQuantity());
            }

            System.out.println("\n------------------- Frozen Assets -------------------");
            for (AccountInfo.FrozenAsset frozenAsset : accountInfo.getFrozenAssets()) {
                System.out.println("Frozen Asset ID: " + frozenAsset.getId());
                System.out.println("  Name: " + frozenAsset.getName());
                System.out.println("  Quantity: " + frozenAsset.getQuantity());
            }

            System.out.println("\n====================================================");
        }

        String prettyJson = gson.toJson(accountInfo);
        System.out.println("Pretty JSON:");
        System.out.println(prettyJson);
    }
}
