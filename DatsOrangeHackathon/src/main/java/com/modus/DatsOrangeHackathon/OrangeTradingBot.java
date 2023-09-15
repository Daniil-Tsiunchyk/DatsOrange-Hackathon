package com.modus.DatsOrangeHackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrangeTradingBot {

    @Autowired
    private OrangeTraderService orangeTrader;

    // Карта для хранения информации о покупках
    private final Map<Integer, Long> boughtAssets = new HashMap<>();

    @Scheduled(fixedRate = 10000) // каждые 10 секунд
    public void trade() {
        // 1. Получаем информацию о наших активах
        var assetsInfo = orangeTrader.getAccountInfo().getAssets();

        // 2. Проверяем активы на продажу
        var sellOffers = orangeTrader.getSellOffers();

        // 3. Если есть возможность продать с прибылью, продать
        for (var asset : assetsInfo) {
            int assetId = asset.getId();
            long boughtPrice = boughtAssets.getOrDefault(assetId, Long.MAX_VALUE);

            long currentSellingPrice = sellOffers.getOrDefault(assetId, Long.MAX_VALUE);

            if (currentSellingPrice < boughtPrice) {
                orangeTrader.sell(assetId, currentSellingPrice);
                boughtAssets.remove(assetId); // Удаляем запись, так как актив продан
            }
        }

        // 4. Проверяем активы для покупки и покупаем по минимальной цене
        var buyOffers = orangeTrader.getBuyOffers();

        for (var offer : buyOffers.entrySet()) {
            int assetId = offer.getKey();
            long minBuyingPrice = offer.getValue();
            orangeTrader.buy(assetId, minBuyingPrice);
            boughtAssets.put(assetId, minBuyingPrice); // Запоминаем цену покупки
        }
    }
}
