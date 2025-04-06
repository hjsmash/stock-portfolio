import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { StockService } from '../../services/stock.service';
import { HttpClientModule } from '@angular/common/http';
import { Stock } from '../../models/stock';

@Component({
  selector: 'app-stock-list',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './stock-list.component.html',
  styleUrls: ['./stock-list.component.css']
})
export class StockListComponent {
  stocks: Stock[] = [];
  symbol: string = '';
  quantity: number = 0;

  constructor(private stockService: StockService) {}

  ngOnInit(): void {
    this.loadStocks();
  }

  loadStocks(): void {
    this.stockService.getAllStocks().subscribe(data => this.stocks = data);
  }

  addStock(): void {
    if (this.symbol && this.quantity > 0) {
      this.stockService.addStock(this.symbol, this.quantity).subscribe(() => {
        this.symbol = '';
        this.quantity = 0;
        this.loadStocks();
      });
    }
  }

  removeStock(stock: Stock): void {
    if (stock.quantity > 0) {
      const qtyToRemove = prompt(`How many ${stock.symbol} stocks to remove?`, '1');
      const quantity = parseInt(qtyToRemove || '0', 10);
      if (quantity > 0 && quantity <= stock.quantity) {
        this.stockService.removeStock(stock.symbol, quantity).subscribe(() => this.loadStocks());
      }
    }
  }
}
