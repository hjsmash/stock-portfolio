package com.example.stockportfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class StockPriceService {

    @Value("${stock.api.key}")
    private String apiKey;

    @Value("${stock.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public double getStockPrice(String symbol) {
        String url = apiUrl + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, String> quoteData = (Map<String, String>) response.get("Global Quote");

            if (quoteData != null && quoteData.containsKey("05. price")) {
                return Double.parseDouble(quoteData.get("05. price"));
            } else {
                //TODO use the exception handling correctly
                //throw new RuntimeException("Stock price not found");
                return 0.0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching stock price: " + e.getMessage());
        }
    }
}
