package com.example.stockportfolio.controller;

import com.example.stockportfolio.model.Stock;
import com.example.stockportfolio.service.StockManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    @Autowired
    private StockManagementService stockManagementService;

    @PostMapping
    public Stock addOrUpdateStock(@RequestParam String symbol, @RequestParam int quantity) {
        return stockManagementService.addOrUpdateStock(symbol, quantity);
    }


    @GetMapping
    public List<Stock> getAllStocks() {
        return stockManagementService.getAllStocks();
    }

    @DeleteMapping
    public void removeStock(@RequestParam String symbol, @RequestParam int quantity) {
        stockManagementService.removeStock(symbol, quantity);
    }
}
