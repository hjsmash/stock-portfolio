import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockService } from '../../services/stock.service';
import { Stock } from '../../models/stock';

@Component({
  selector: 'app-stock-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stock-list.component.html',
  styleUrls: ['./stock-list.component.css']
})
export class StockListComponent {
  stocks: Stock[] = [];
  symbol: string = '';
  quantity?: number;
  searchTerm: string = '';
  symbolResults: any[] = [];

  onSearchSymbol(): void {
    //TODO update this length condition to 3 for using vantage API
    if (this.searchTerm.length < 30) {
      this.symbolResults = [];
      return;
    }

    this.stockService.searchSymbol(this.searchTerm).subscribe((data: any) => {
      this.symbolResults = data['bestMatches'] || [];
    });
  }

  selectSymbol(searchTerm: string): void {
    this.symbol = searchTerm;
    this.searchTerm = searchTerm;
    this.symbolResults = [];
  }
  constructor(private stockService: StockService) { }

  ngOnInit(): void {
    this.loadStocks();
  }

  loadStocks(): void {
    this.stockService.getAllStocks().subscribe(data => this.stocks = data);
  }
  portfolioValue(): String{
    return this.stocks.reduce((acc, stock) => acc + (stock.price * stock.quantity), 0).toFixed(2);
  }

  addStock(): void {
    //TemporaryFix to avoid issues if vantageApi is down
    if ((this.symbol || this.searchTerm) && this.quantity && this.quantity>0) {
      if (!this.symbol)
        this.symbol = this.searchTerm
      this.stockService.addStock(this.symbol, this.quantity).subscribe(() => {
        this.symbol = '';
        this.searchTerm = '';
        this.quantity = undefined;
        this.loadStocks();
      });
    }
  }

  removeStock(stock: Stock): void {
    if (stock.changeQuantity > 0 && stock.changeQuantity <= stock.quantity) {
      this.stockService.removeStock(stock.symbol, stock.changeQuantity).subscribe(() => this.loadStocks());
    }
  }
  addStockQuantity(stock: Stock): void {
    if (stock.changeQuantity > 0 && stock.changeQuantity <= stock.quantity) {
      this.stockService.addStock(stock.symbol, stock.changeQuantity).subscribe(() => this.loadStocks());
    }
  }
}
