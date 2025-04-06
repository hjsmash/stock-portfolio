# 📈 Stock Portfolio Tracker

A full-stack web application to manage and monitor your stock portfolio with **real-time prices** using the Alpha Vantage API.

---

## 🧱 Project Structure

```
stock-portfolio/
├── stock-portfolio-service/     → Spring Boot backend service (Java + H2 DB)
├── stock-portfolio-ui-app/        → Angular 17 frontend (standalone components + Bootstrap)
└── README.md
```

---

## 🚀 Features

- Add stocks with symbols and quantities
- Prevent duplicate symbols — intelligently updates quantity instead
- Real-time stock prices fetched from [Alpha Vantage](https://www.alphavantage.co/)
- In-memory H2 database for development
- Bootstrap-powered Angular UI
- Modular microservice structure (frontend and backend split)

---

## 🧪 Tech Stack

| Layer     | Tech Used                                 |
|-----------|--------------------------------------------|
| Frontend  | Angular 17 (standalone), Bootstrap CSS     |
| Backend   | Spring Boot, Spring Web, H2 Database       |
| API       | [Alpha Vantage Stock API](https://www.alphavantage.co/documentation/) |
| Dev Tools | Postman, IntelliJ, VS Code                 |

---

## ⚙️ Getting Started

### 🖥 Backend Setup (Spring Boot)

```bash
cd backend-stock-service
./mvnw spring-boot:run
```

- Runs on `http://localhost:8080`
- H2 console at `http://localhost:8080/h2-console`  
  (JDBC URL: `jdbc:h2:mem:testdb`, username: `sa`, no password)

---

### 🌐 Frontend Setup (Angular)

```bash
cd frontend-stock-app
npm install
ng serve
```

- Runs on `http://localhost:4200`
- Uses standalone Angular components
- Fetches backend data from `http://localhost:8080`

---

## 🔐 API Key Setup

1. Get a free API key from [Alpha Vantage](https://www.alphavantage.co/support/#api-key)
2. Add it in `StockService.java` (backend):

```java
private final String API_KEY = "your_actual_key_here";
```

---

## 📁 Future Improvements

- Add user login with portfolio ownership
- Move to PostgreSQL or MongoDB for persistence
- Error handling improvements and loading spinners
- CI/CD pipeline with GitHub Actions
- Deploy backend (e.g., Render/Railway) & frontend (e.g., Netlify/Vercel)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

## 🙌 Acknowledgements

- [Alpha Vantage](https://www.alphavantage.co/) for real-time stock data
- Spring Boot & Angular teams
