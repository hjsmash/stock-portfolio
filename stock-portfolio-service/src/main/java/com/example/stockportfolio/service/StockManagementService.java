package com.example.stockportfolio.service;

import com.example.stockportfolio.model.Stock;
import com.example.stockportfolio.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockManagementService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockVantageService stockVantageService;

    public Stock addOrUpdateStock(String symbol, int quantity) {
        Optional<Stock> existingStock = stockRepository.findBySymbol(symbol);

        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            return stockRepository.save(stock);
        } else {
            Stock newStock = new Stock();
            newStock.setSymbol(symbol);
            newStock.setQuantity(quantity);
            return stockRepository.save(newStock);
        }
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = stockRepository.findAll();

        // Fetch live price for each stock
        stocks.forEach(stock -> {
            double price = stockVantageService.getStockPrice(stock.getSymbol());
            stock.setPrice(price);
        });

        return stocks;
    }


    public void removeStock(String symbol, int quantity) {
        Optional<Stock> existingStock = stockRepository.findBySymbol(symbol);

        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();

            if (stock.getQuantity() < quantity) {
                throw new IllegalArgumentException("Cannot remove more stocks than available. Current quantity: " + stock.getQuantity());
            }

            stock.setQuantity(stock.getQuantity() - quantity);

            if (stock.getQuantity() == 0) {
                stockRepository.delete(stock);
            } else {
                stockRepository.save(stock);
            }
        } else {
            throw new IllegalArgumentException("Stock with symbol " + symbol + " not found.");
        }
    }
}
