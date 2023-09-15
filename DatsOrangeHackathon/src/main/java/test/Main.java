package test;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        OrangeTrader orangeTrader = new OrangeTrader(); // Создайте объект вашего сервиса
        String json = orangeTrader.getAccountInfoJson(); // Получите JSON как строку

        Gson gson = new Gson();
        AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);

        // Далее вы можете работать с объектом `accountInfo` как обычно.

        // Выводим всю информацию в консоль
        if (accountInfo != null) {
            System.out.println("Account Info:");
            System.out.println("Account ID: " + accountInfo.getAccount().getId());
            System.out.println("Account Name: " + accountInfo.getAccount().getName());

            System.out.println("Bids:");
            for (AccountInfo.Bid bid : accountInfo.getBids()) {
                System.out.println("Bid ID: " + bid.getId());
                // ... и так далее для всех полей Bid
            }

            System.out.println("Assets:");
            for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                System.out.println("Asset ID: " + asset.getId());
                // ... и так далее для всех полей Asset
            }

            System.out.println("Frozen Assets:");
            for (AccountInfo.FrozenAsset frozenAsset : accountInfo.getFrozenAssets()) {
                System.out.println("Frozen Asset ID: " + frozenAsset.getId());
                // ... и так далее для всех полей FrozenAsset
            }
        }

        // Для красивого вывода JSON
        String prettyJson = gson.toJson(accountInfo);
        System.out.println("Pretty JSON:");
        System.out.println(prettyJson);
    }
}


