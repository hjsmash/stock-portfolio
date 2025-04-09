package com.example.stockportfolio.service;

import com.example.stockportfolio.exception.InvalidStockRemovalException;
import com.example.stockportfolio.exception.StockNotFoundException;
import com.example.stockportfolio.model.Stock;
import com.example.stockportfolio.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(StockManagementService.class);


    public Stock addOrUpdateStock(String symbol, int quantity) {
        logger.info("Adding {} units of symbol '{}'", quantity, symbol);
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
        logger.info("Fetching all stocks from repository...");
        List<Stock> stocks = stockRepository.findAll();

        stocks.forEach(stock -> {
            double price = stockVantageService.getStockPrice(stock.getSymbol());
            stock.setPrice(price);
        });

        return stocks;
    }


    public Stock removeStock(String symbol, int removalQuantity) {
        logger.info("Removing {} units of symbol '{}'", removalQuantity, symbol);
        Optional<Stock> existingStock = stockRepository.findBySymbol(symbol);

        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();

            if (stock.getQuantity() < removalQuantity) {
                throw new InvalidStockRemovalException(symbol, stock.getQuantity(), removalQuantity);
            }

            stock.setQuantity(stock.getQuantity() - removalQuantity);

            if (stock.getQuantity() == 0) {
                stockRepository.delete(stock);
            } else {
                stockRepository.save(stock);
            }
            return stock;
        } else {
            throw new StockNotFoundException("Stock with symbol '" + symbol + "' not found in portfolio.");
        }
    }
}
