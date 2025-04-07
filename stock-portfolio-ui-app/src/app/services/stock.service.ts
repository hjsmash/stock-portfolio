import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Stock } from '../models/stock';

@Injectable({
  providedIn: 'root'
})
export class StockService {
  private apiUrl = 'http://localhost:8081/api/stocks';

  constructor(private http: HttpClient) {}

  getAllStocks(): Observable<Stock[]> {
    return this.http.get<Stock[]>(this.apiUrl);
  }

  addStock(symbol: string, quantity: number): Observable<Stock> {
    return this.http.post<Stock>(`${this.apiUrl}?symbol=${symbol}&quantity=${quantity}`, {});
  }

  removeStock(symbol: string, quantity: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}?symbol=${symbol}&quantity=${quantity}`);
  }
  searchSymbol(keyword: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/search?keyword=${keyword}`);
  }
}
