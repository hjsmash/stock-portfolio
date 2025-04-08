package com.example.stockportfolio.controller;

import com.example.stockportfolio.dto.ApiResponse;
import com.example.stockportfolio.model.Stock;
import com.example.stockportfolio.service.StockManagementService;
import com.example.stockportfolio.service.StockVantageService;
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

    @PostMapping
    public ResponseEntity<ApiResponse<Stock>> addOrUpdateStock(@RequestParam String symbol, @RequestParam int quantity) {
        Stock stock = stockManagementService.addOrUpdateStock(symbol, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock added successfully", stock));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<Stock>>> getAllStocks() {
        List<Stock> stocks = stockManagementService.getAllStocks();
        return ResponseEntity.ok(ApiResponse.success("Portfolio fetched successfully", stocks));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> removeStock(@RequestParam String symbol, @RequestParam int quantity) {
        Stock stock = stockManagementService.removeStock(symbol, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock removed successfully", stock));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSymbol(@RequestParam String keyword) {
        return stockVantageService.searchSymbol(keyword);
    }
}
