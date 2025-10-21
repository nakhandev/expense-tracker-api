# 💰 Expense Tracker API

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![H2 Database](https://img.shields.io/badge/H2-Memory-DB-red.svg)](https://www.h2database.com/)
[![GitHub](https://img.shields.io/badge/GitHub-Repository-blue.svg)](https://github.com/nakhandev/expense-tracker-api)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive REST API for tracking personal and household expenses with advanced filtering, categorization, and summary features. Built with Spring Boot 3.x and H2 in-memory database for quick testing and development.

## ✨ Features

### 🚀 Core Functionality
- **Complete CRUD Operations** - Create, Read, Update, Delete expenses
- **Advanced Filtering** - Filter by date ranges, categories, or both
- **Category Management** - 9 predefined expense categories
- **Summary Reports** - Total spending per category with counts
- **Data Validation** - Comprehensive input validation and error handling
- **H2 Database Console** - Web-based database inspection tool

### 📊 Expense Categories
- 🍕 **FOOD** - Restaurants, groceries, dining
- 🚗 **TRANSPORT** - Fuel, public transport, maintenance
- 🎬 **ENTERTAINMENT** - Movies, games, subscriptions
- ⚡ **UTILITIES** - Electricity, water, internet, phone
- 🏥 **HEALTHCARE** - Medical, pharmacy, insurance
- 📚 **EDUCATION** - Books, courses, training
- 🛍️ **SHOPPING** - Clothes, electronics, general shopping
- ✈️ **TRAVEL** - Hotels, flights, vacation expenses
- 📦 **OTHER** - Miscellaneous expenses

## 🛠️ Prerequisites

- **Java**: 17 or higher
- **Maven**: 3.6 or higher
- **Git**: For cloning the repository
- **cURL**: For API testing (optional)

## 🚀 Quick Start

### One-Command Setup
```bash
git clone https://github.com/nakhandev/expense-tracker-api.git
cd expense-tracker-api
./start.sh
```

The API will be available at **http://localhost:8080**

## 📦 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/nakhandev/expense-tracker-api.git
cd expense-tracker-api
```

### 2. Verify Prerequisites
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

### 3. Build the Project
```bash
mvn clean compile
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

### 5. Verify Installation
- API: http://localhost:8080/api/expenses
- H2 Console: http://localhost:8080/h2-console

## 📚 Usage

### Creating Your First Expense

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 25.50,
    "description": "Lunch at cafe",
    "category": "FOOD",
    "date": "2024-01-15"
  }'
```

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/expenses` | Create new expense |
| `GET` | `/api/expenses` | Get all expenses |
| `GET` | `/api/expenses/{id}` | Get expense by ID |
| `PUT` | `/api/expenses/{id}` | Update expense |
| `DELETE` | `/api/expenses/{id}` | Delete expense |
| `GET` | `/api/expenses/category/{category}` | Get expenses by category |
| `GET` | `/api/expenses/filter?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` | Filter by date range |
| `GET` | `/api/expenses/summary` | Get category summaries |
| `GET` | `/api/expenses/total?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` | Get total spending |

### Example API Calls

#### Create Expense
```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50.00,
    "description": "Gas station fill-up",
    "category": "TRANSPORT",
    "date": "2024-01-16"
  }'
```

#### Get All Expenses
```bash
curl -X GET http://localhost:8080/api/expenses
```

#### Filter by Category
```bash
curl -X GET http://localhost:8080/api/expenses/category/FOOD
```

#### Filter by Date Range
```bash
curl -X GET "http://localhost:8080/api/expenses/filter?startDate=2024-01-01&endDate=2024-01-31"
```

#### Get Summary
```bash
curl -X GET http://localhost:8080/api/expenses/summary
```

#### Update Expense
```bash
curl -X PUT http://localhost:8080/api/expenses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 55.00,
    "description": "Gas station fill-up (updated)",
    "category": "TRANSPORT",
    "date": "2024-01-16"
  }'
```

#### Delete Expense
```bash
curl -X DELETE http://localhost:8080/api/expenses/1
```

## ⚙️ Configuration

### Application Properties
The application can be configured via `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:h2:mem:expense_tracker_db
spring.datasource.username=sa
spring.datasource.password=password

# H2 Console (Development)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Environment Variables
You can override configuration using environment variables:

```bash
export SERVER_PORT=9090
export SPRING_PROFILES_ACTIVE=prod
```

## 🗄️ Database

### H2 Console Access
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:expense_tracker_db`
- **Username**: `sa`
- **Password**: `password`

### Database Schema
The application automatically creates the following table:

```sql
CREATE TABLE expenses (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL CHECK (category IN ('FOOD','TRANSPORT','ENTERTAINMENT','UTILITIES','HEALTHCARE','EDUCATION','SHOPPING','TRAVEL','OTHER')),
    date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    PRIMARY KEY (id)
);
```

## 🧪 Testing

### Manual Testing
Test the API endpoints using cURL or any REST client:

```bash
# Health Check
curl -X GET http://localhost:8080/api/expenses

# Create test data
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"amount": 10.50, "description": "Coffee", "category": "FOOD", "date": "2024-01-15"}'

# Verify data
curl -X GET http://localhost:8080/api/expenses
```

### Automated Testing
```bash
# Run tests
mvn test

# Run with coverage (if configured)
mvn test jacoco:report
```

## 🔧 Development

### Project Structure
```
expense-tracker-api/
├── src/main/java/org/nakhan/
│   ├── controller/         # REST Controllers
│   ├── service/           # Business Logic
│   ├── repository/        # Data Access Layer
│   ├── model/             # JPA Entities
│   ├── dto/               # Data Transfer Objects
│   └── exception/         # Exception Handlers
├── src/main/resources/
│   └── application.properties
├── src/test/              # Test files
├── target/                # Build output
├── pom.xml               # Maven configuration
├── start.sh              # Startup script
└── README.md             # This file
```

### Adding New Categories
1. Update the `Category` enum in `model/Category.java`
2. Update the database check constraint if needed
3. Restart the application

### Custom Validation
Add validation annotations to the `Expense` entity or use `@Valid` in controllers for custom validation.

## 🚨 Troubleshooting

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change the port in application.properties
server.port=9090
```

### Application Won't Start
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Clean and rebuild
mvn clean compile

# Check for compilation errors
mvn compile
```

### Database Connection Issues
1. Verify H2 dependency in `pom.xml`
2. Check database configuration in `application.properties`
3. Access H2 console at http://localhost:8080/h2-console

### Memory Issues
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=512m"
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java naming conventions
- Add unit tests for new features
- Update documentation for API changes
- Ensure all tests pass before submitting PR

## 📄 API Response Formats

### Success Response
```json
{
  "id": 1,
  "amount": 25.50,
  "description": "Lunch at cafe",
  "category": "FOOD",
  "date": "2024-01-15",
  "createdAt": "2025-10-21T15:51:12.431769",
  "updatedAt": "2025-10-21T15:51:12.431786"
}
```

### Error Response
```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Validation failed for one or more fields",
  "fieldErrors": {
    "amount": "Amount must be greater than 0",
    "description": "Description is required"
  },
  "timestamp": "2025-10-21T15:51:12.431769"
}
```

### Summary Response
```json
[
  {
    "category": "FOOD",
    "totalAmount": 75.50,
    "expenseCount": 3
  }
]
```

## 🔐 Security

This API is designed for development and testing. For production use:

- Implement authentication and authorization
- Use HTTPS in production
- Configure proper CORS policies
- Add rate limiting
- Use environment-specific configurations

## 📈 Performance

- **In-Memory Database**: Fast H2 database for quick testing
- **Connection Pooling**: HikariCP for optimal database connections
- **Lazy Loading**: JPA lazy loading for better performance
- **Minimal Dependencies**: Only essential dependencies included

## 🏷️ Version History

- **v1.0.0** - Initial release with full CRUD functionality
  - Complete expense management
  - Category-based filtering
  - Date range filtering
  - Summary reports
  - Input validation
  - H2 database integration

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review the API documentation

## 📋 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Made with ❤️ using Spring Boot and Java**
