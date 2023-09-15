package test;

import org.springframework.data.relational.core.sql.In;
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

    public void checkAndSell() {
        String url = baseUrl + "/sellStock";

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            for (Map stock : Objects.requireNonNull(response.getBody())) {
                Integer symbolId = (Integer)  stock.get("symbolId");
                Integer stockPrice = (Integer) stock.get("price");
                Integer 

                for (int i = 0; i < buyPrices.size(); i++) {
                    long boughtPrice = buyPrices.get(i);

                    if (stockPrice > boughtPrice) {
                        placeSellOrder(stockPrice, 1);  // 1 quantity, adjust as needed
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
}
