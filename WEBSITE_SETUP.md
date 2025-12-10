# 🌐 Expense Tracker Website Setup Guide

## 📋 Overview
Your Expense Tracker application is ready to run! This guide will help you set up and access the website in different ways.

## 🚀 Quick Start Options

### Option 1: Standalone Demo (No Backend Required)
The easiest way to see your expense tracker in action:

1. **Open the demo file**: Navigate to `ExpenseTracker/standalone-demo.html`
2. **Double-click** the file to open it in your default browser
3. **Try the features**: Add transactions, view balance, and explore the interface

**Features available in demo:**
- ✅ Add income and expense transactions
- ✅ View transaction history
- ✅ Calculate running balance
- ✅ Sample data generator
- ✅ Responsive design with Bootstrap

### Option 2: Full Spring Boot Application (Recommended)
For the complete experience with database persistence and API:

#### Prerequisites
- **Java 17+** ✅ (You have Java 25 installed)
- **Maven** (Install from: https://maven.apache.org/download.cgi)

#### Setup Steps

1. **Install Maven**:
   - Download Maven from https://maven.apache.org/download.cgi
   - Extract to a folder (e.g., `C:\apache-maven-3.9.6`)
   - Add to PATH: `C:\apache-maven-3.9.6\bin`

2. **Build the Application**:
   ```powershell
   cd ExpenseTracker
   mvn clean package
   ```

3. **Run the Application**:
   ```powershell
   mvn spring-boot:run
   ```
   OR
   ```powershell
   java -jar target/expense-tracker-1.0-SNAPSHOT.jar
   ```

4. **Access the Website**:
   - Open your browser
   - Navigate to: `http://localhost:8080`
   - The web interface will be available at: `http://localhost:8080/index.html`

#### Full Application Features
- 🔐 User authentication and management
- 💾 Data persistence with H2 database
- 🌐 RESTful API endpoints
- 📊 Advanced reporting and analytics
- 🏷️ Category management
- 🔍 Transaction search and filtering
- 📈 Dashboard with summaries

## 🎯 Available Endpoints

When running the full application, these API endpoints are available:

### Users
- `POST /api/users` - Create user
- `GET /api/users/{id}/transactions` - Get user transactions
- `GET /api/users/{id}/balance` - Get user balance

### Transactions
- `POST /api/transactions/income` - Add income
- `POST /api/transactions/expense` - Add expense
- `GET /api/transactions/user/{userId}` - Get user transactions
- `DELETE /api/transactions/{id}` - Delete transaction

### Categories
- `GET /api/categories` - Get all categories
- `POST /api/categories` - Create category

### Dashboard
- `GET /api/dashboard/summary/{userId}` - Get dashboard summary
- `GET /api/dashboard/balance/{userId}` - Get user balance

## 🗄️ Database Information

The application uses **H2 in-memory database** for easy setup:
- **Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:expense_tracker`
- **Username**: `sa`
- **Password**: (empty)

## 🎨 Web Interface Features

### Modern UI Components
- **Bootstrap 5** for responsive design
- **Hero section** with feature highlights
- **Card-based layout** for organized content
- **Color-coded transactions** (green for income, red for expenses)
- **Real-time balance updates**
- **Sample data generator** for testing

### User Experience
- 📱 **Mobile responsive** design
- ⚡ **Fast loading** with CDN resources
- 🎯 **Intuitive forms** with validation
- 📊 **Visual feedback** for all actions
- 🔄 **Real-time updates** without page refresh

## 🛠️ Troubleshooting

### Common Issues

1. **"Maven not found" error**:
   - Install Maven and add to PATH
   - Or use an IDE like IntelliJ IDEA or Eclipse

2. **Port 8080 already in use**:
   - Change port in `application.properties`
   - Or stop other applications using port 8080

3. **Java version issues**:
   - Ensure Java 17+ is installed
   - Check JAVA_HOME environment variable

4. **Database connection issues**:
   - H2 database starts automatically
   - Check console at http://localhost:8080/h2-console

### Getting Help

If you encounter issues:
1. Check the console output for error messages
2. Verify all prerequisites are installed
3. Try the standalone demo first to test the UI
4. Use the H2 console to inspect database data

## 🎉 Success!

Once running, you'll have a fully functional expense tracking application with:
- Beautiful, modern web interface
- Complete backend API
- Database persistence
- Real-time updates
- Professional-grade architecture

**Happy expense tracking! 💰**



