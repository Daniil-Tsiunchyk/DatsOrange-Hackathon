package test;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class OrangeTrader {

    private final String token = "64f38d2665df964f38d2665dfd";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "https://datsorange.devteam.games";

    private final List<Integer> buyOrderIds = new ArrayList<>();
    private final List<Integer> buyPrices = new ArrayList<>();
    private int activeBids = 0;

    public void placeBuyOrder(int symbolId, int price, int quantity) {
        String url = baseUrl + "/LimitPriceBuy";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("symbolId", symbolId);
        map.put("price", price);
        map.put("quantity", quantity);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Integer buyOrderId = (Integer) Objects.requireNonNull(response.getBody()).get("bidId");
            buyOrderIds.add(buyOrderId);
            buyPrices.add(price);
            activeBids++;
        }
    }

    // получает все заявки на продажу акций
    public ResponseEntity<Map[]> checkSellStock() {
        String url = baseUrl + "/sellStock";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);
    }

    // получает все заявки на продажу и ищет лучшее предложение
    public void checkAndSell() {
        ResponseEntity<Map[]> response = checkSellStock();

        if (response.getStatusCode() == HttpStatus.OK) {
            for (Map stock : Objects.requireNonNull(response.getBody())) {

                Integer symbolId = (Integer) stock.get("id");
                Integer stockPrice = (Integer) stock.get("price");

                for (int i = 0; i < buyPrices.size(); i++) {
                    long boughtPrice = buyPrices.get(i);

                    if (stockPrice > boughtPrice) {
//                        placeSellOrder(stockPrice, 1);  // 1 quantity, adjust as needed
                        cancelOrder(buyOrderIds.get(i));

                        buyPrices.remove(i);
                        buyOrderIds.remove(i);
                        i--;  // adjust index due to removal
                        activeBids--;
                    }
                }
            }
        }


    }

    public void placeSellOrder(int symbolId, int price, int quantity) {
        String url = baseUrl + "/LimitPriceSell";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("symbolId", symbolId);
        map.put("price", price);
        map.put("quantity", quantity);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        restTemplate.postForEntity(url, entity, Map.class);
    }

    public void cancelOrder(int bidId) {
        String url = baseUrl + "/RemoveBid";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("bidId", bidId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        restTemplate.postForEntity(url, entity, Map.class);
    }

    public AccountInfo getAccountInfo() {
        String url = baseUrl + "/info";  // Замените на ваш API endpoint для получения информации о аккаунте

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<AccountInfo> response = restTemplate.exchange(url, HttpMethod.GET, entity, AccountInfo.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;  // или кидать исключение
        }
    }

    public static void displayOrangesQuantity(AccountInfo accountInfo) {
        if (accountInfo != null && accountInfo.getAssets() != null) {
            for (AccountInfo.Asset asset : accountInfo.getAssets()) {
                if ("Oranges".equalsIgnoreCase(asset.getName())) {
                    System.out.println("Asset ID: " + asset.getId());
                    System.out.println("  Name: " + asset.getName());
                    System.out.println("  Quantity: " + asset.getQuantity());
                    return;
                }
            }
        }
        System.out.println("Oranges asset not found.");
    }

    public Map<Integer, Long> getSellOffers() {
        // TODO: реализовать метод
        return null; // временно, нужно реализовать
    }

    public Map<Integer, Long> getBuyOffers() {
        // TODO: реализовать метод
        return null; // временно, нужно реализовать
    }

    public void buy(int assetId, long price) {
        placeBuyOrder(assetId, (int) price, 1);  // Количество можно настроить
    }

    public void sell(int assetId, long price) {
        placeSellOrder(assetId, (int) price, 1);  // Количество можно настроить
    }

    public String getAccountInfoJson() {
        String url = baseUrl + "/info";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

}
