package com.example.stockportfolio.controller;

import com.example.stockportfolio.dto.ApiResponse;
import com.example.stockportfolio.dto.SymbolSearchResult;
import com.example.stockportfolio.model.Stock;
import com.example.stockportfolio.service.StockManagementService;
import com.example.stockportfolio.service.StockVantageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    @Autowired
    private StockManagementService stockManagementService;
    @Autowired
    private StockVantageService stockVantageService;

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @PostMapping
    public ResponseEntity<ApiResponse<Stock>> addOrUpdateStock(@RequestParam String symbol, @RequestParam int quantity) {
        logger.info("POST /api/stocks - symbol={}, quantity={}", symbol, quantity);
        Stock stock = stockManagementService.addOrUpdateStock(symbol, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock added", stock));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<Stock>>> getAllStocks() {
        logger.info("GET /api/stocks called");
        List<Stock> stocks = stockManagementService.getAllStocks();
        return ResponseEntity.ok(ApiResponse.success("Portfolio fetched", stocks));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> removeStock(@RequestParam String symbol, @RequestParam int quantity) {
        logger.info("DELETE /api/stocks - symbol={}, quantity={}", symbol, quantity);
        Stock stock = stockManagementService.removeStock(symbol, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock removed", stock));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSymbol(@RequestParam String keyword) {
        logger.info("GET /api/stocks/search - symbol={}", keyword);
        List<SymbolSearchResult> results = stockVantageService.searchSymbol(keyword);
        return ResponseEntity.ok(ApiResponse.success("Symbols fetched", results));
    }
}
