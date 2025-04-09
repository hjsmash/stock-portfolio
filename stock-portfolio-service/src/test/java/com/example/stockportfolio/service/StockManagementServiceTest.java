package com.example.stockportfolio.service;

import com.example.stockportfolio.exception.InvalidStockRemovalException;
import com.example.stockportfolio.exception.StockNotFoundException;
import com.example.stockportfolio.model.Stock;
import com.example.stockportfolio.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StockManagementServiceTest {

    @InjectMocks
    private StockManagementService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockVantageService stockVantageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewStock() {
        String symbol = "AAPL";
        int quantity = 5;

        when(stockRepository.findBySymbol(symbol)).thenReturn(Optional.empty());

        Stock newStock = new Stock();
        newStock.setSymbol(symbol);
        newStock.setQuantity(quantity);

        when(stockRepository.save(any(Stock.class))).thenReturn(newStock);

        Stock result = stockService.addOrUpdateStock(symbol, quantity);

        assertEquals(symbol, result.getSymbol());
        assertEquals(quantity, result.getQuantity());
        verify(stockRepository).save(any(Stock.class));
    }


    @Test
    void testUpdateExistingStock() {
        String symbol = "AAPL";
        Stock existing = new Stock();
        existing.setSymbol(symbol);
        existing.setQuantity(10);

        when(stockRepository.findBySymbol(symbol)).thenReturn(Optional.of(existing));
        when(stockRepository.save(existing)).thenReturn(existing);

        Stock result = stockService.addOrUpdateStock(symbol, 5);

        assertEquals(15, result.getQuantity());
        verify(stockRepository).save(existing);
    }


    @Test
    void testGetAllStocksWithPrices() {
        Stock stock1 = new Stock();
        stock1.setSymbol("AAPL");
        stock1.setQuantity(10);
        Stock stock2 = new Stock();
        stock2.setSymbol("MSFT");
        stock2.setQuantity(5);
        when(stockRepository.findAll()).thenReturn(List.of(stock1, stock2));
        when(stockVantageService.getStockPrice("AAPL")).thenReturn(150.0);
        when(stockVantageService.getStockPrice("MSFT")).thenReturn(250.0);

        List<Stock> stocks = stockService.getAllStocks();

        assertEquals(2, stocks.size());
        assertEquals(150.0, stocks.get(0).getPrice());
        assertEquals(250.0, stocks.get(1).getPrice());
    }


    @Test
    void testRemoveStock_ValidQuantity() {
        String symbol = "GOOG";
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setQuantity(5);
        when(stockRepository.findBySymbol(symbol)).thenReturn(Optional.of(stock));

        Stock result = stockService.removeStock(symbol, 2);

        assertEquals(3, result.getQuantity());
        verify(stockRepository).save(stock);
    }


    @Test
    void testRemoveStock_Completely() {
        String symbol = "GOOG";
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setQuantity(2);
        when(stockRepository.findBySymbol(symbol)).thenReturn(Optional.of(stock));

        Stock result = stockService.removeStock(symbol, 2);

        assertEquals(0, result.getQuantity());
        verify(stockRepository).delete(stock);
    }


    @Test
    void testRemoveStock_InvalidQuantity_ThrowsException() {
        String symbol = "AMZN";
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setQuantity(3);
        when(stockRepository.findBySymbol(symbol)).thenReturn(Optional.of(stock));

        assertThrows(InvalidStockRemovalException.class, () -> {
            stockService.removeStock(symbol, 5);
        });
    }


    @Test
    void testRemoveStock_NotFound_ThrowsException() {
        String symbol = "NFLX";
        when(stockRepository.findBySymbol(symbol)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> {
            stockService.removeStock(symbol, 1);
        });
    }
}
