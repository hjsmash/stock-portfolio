package com.example.stockportfolio.service;

import com.example.stockportfolio.dto.SymbolSearchResult;
import com.example.stockportfolio.exception.VantageApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class StockVantageServiceTest {

    @InjectMocks
    private StockVantageService stockVantageService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    private void mockWebClient(Map<String, Object> mockResponse) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(mockResponse != null ? Mono.just(mockResponse) : null);

    }


    private void mockWebClientException(Throwable error) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenThrow(error);
    }

    @Test
    void testGetStockPrice_Success() {
        Map<String, String> quote = Map.of("05. price", "123.45");
        Map<String, Object> response = Map.of("Global Quote", quote);

        mockWebClient(response);

        double price = stockVantageService.getStockPrice("AAPL");

        assertEquals(123.45, price);
    }

    @Test
    void testGetStockPrice_MissingPrice_ThrowsException() {
        Map<String, Object> response = Map.of("Global Quote", new HashMap<>());

        mockWebClient(response);

        assertThrows(VantageApiException.class, () -> {
            stockVantageService.getStockPrice("AAPL");
        });
    }

    @Test
    void testGetStockPrice_NullResponse_ThrowsException() {
        mockWebClient(null);

        assertThrows(VantageApiException.class, () -> {
            stockVantageService.getStockPrice("AAPL");
        });
    }

    @Test
    void testGetStockPrice_ApiFailure_ThrowsException() {
        mockWebClientException(new RuntimeException("API Failure"));

        assertThrows(VantageApiException.class, () -> {
            stockVantageService.getStockPrice("TSLA");
        });
    }

    @Test
    void testSearchSymbol_Success() {
        Map<String, Object> match1 = Map.of("1. symbol", "AAPL", "2. name", "Apple Inc.");
        Map<String, Object> match2 = Map.of("1. symbol", "MSFT", "2. name", "Microsoft");

        Map<String, Object> response = Map.of("bestMatches", List.of(match1, match2));

        mockWebClient(response);

        List<SymbolSearchResult> results = stockVantageService.searchSymbol("apple");

        assertEquals(2, results.size());
        assertEquals("AAPL", results.get(0).getSymbol());
        assertEquals("Microsoft", results.get(1).getName());
    }

    @Test
    void testSearchSymbol_NullResponse_ThrowsException() {
        mockWebClient(null);

        assertThrows(VantageApiException.class, () -> {
            stockVantageService.searchSymbol("xyz");
        });
    }

    @Test
    void testSearchSymbol_InvalidStructure_ThrowsException() {
        Map<String, Object> malformed = Map.of("unexpectedKey", "oops");

        mockWebClient(malformed);

        assertThrows(VantageApiException.class, () -> {
            stockVantageService.searchSymbol("bad");
        });
    }

    @Test
    void testSearchSymbol_WebClientFailure_ThrowsException() {
        mockWebClientException(new RuntimeException("Timeout"));

        assertThrows(VantageApiException.class, () -> {
            stockVantageService.searchSymbol("fail");
        });
    }
}
