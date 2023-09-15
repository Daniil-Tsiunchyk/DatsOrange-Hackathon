package test;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class OrangeTrader {

    private String token = "64f38d2665df964f38d2665dfd";
    private RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "https://datsorange.devteam.games";
    private long symbolId = 2; // ID апельсинов

    private List<Long> buyOrderIds = new ArrayList<>();
    private List<Long> buyPrices = new ArrayList<>();
    private int activeBids = 0;

    public void placeBuyOrder(long price, int quantity) {
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
            Long buyOrderId = (Long) response.getBody().get("bidId");
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
            for (Map stock : response.getBody()) {
                long stockPrice = (Long) stock.get("price");

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

    public void placeSellOrder(long price, int quantity) {
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

    public void cancelOrder(long bidId) {
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