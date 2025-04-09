package com.example.stockportfolio.service;

import com.example.stockportfolio.dto.SymbolSearchResult;
import com.example.stockportfolio.exception.StockNotFoundException;
import com.example.stockportfolio.exception.VantageApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockVantageService {

    @Value("${stock.api.key}")
    private String apiKey;

    @Value("${stock.api.url}")
    private String apiUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public double getStockPrice(String symbol) {
        String url = apiUrl + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        try {
            Map response = webClientBuilder.build().get().uri(url).retrieve().bodyToMono(Map.class).block();

            Map<String, String> quoteData = (Map<String, String>) response.get("Global Quote");

            if (quoteData != null && quoteData.containsKey("05. price")) {
                return Double.parseDouble(quoteData.get("05. price"));
            } else {
                throw new StockNotFoundException("Stock price for symbol '" + symbol + "' not found");
            }
        } catch (Exception e) {
            throw new VantageApiException(e.getMessage());
        }
    }


    public List<SymbolSearchResult> searchSymbol(String keyword) {
        String url = apiUrl + "?function=SYMBOL_SEARCH&keywords=" + keyword + "&apikey=" + apiKey;
        List<SymbolSearchResult> resultList = new ArrayList<>();
        try {
            Map response = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("bestMatches")) {
                throw new VantageApiException("Invalid response from Vantage API");
            }

            List<String> matches = (List<String>) response.get("bestMatches");

            for (Object item : matches) {
                if (item instanceof Map<?, ?> matchMap) {
                    String symbol = String.valueOf(matchMap.get("1. symbol"));
                    String name = String.valueOf(matchMap.get("2. name"));
                    resultList.add(new SymbolSearchResult(symbol, name));
                }
            }
        } catch (Exception e) {
            throw new VantageApiException(e.getMessage());
        }
        return resultList;
    }

}
