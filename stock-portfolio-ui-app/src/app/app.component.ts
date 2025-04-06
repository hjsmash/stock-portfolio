import { Component } from '@angular/core';
import { StockListComponent } from './components/stock-list/stock-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [StockListComponent],
  template: `<app-stock-list></app-stock-list>`,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'stock-portfolio-frontend';
}
