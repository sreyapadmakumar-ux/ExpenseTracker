# Expense Tracker Application

## Overview
The Expense Tracker application is a Java-based application that allows users to manage their income and expenses efficiently. It provides features for adding users, recording transactions, generating reports, and searching through transactions. The application integrates with a MySQL database for data storage.

## Features
- Add new users
- Add income and expense transactions
- View user transactions
- Generate financial reports
- Search transactions by category or type

## Project Structure
```
expense-tracker
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   └── expensetracker
│   │   │   │       ├── app
│   │   │   │       │   └── ExpenseTrackerApp.java
│   │   │   │       ├── dao
│   │   │   │       │   ├── CategoryDAO.java
│   │   │   │       │   ├── TransactionDAO.java
│   │   │   │       │   └── UserDAO.java
│   │   │   │       ├── model
│   │   │   │       │   ├── Category.java
│   │   │   │       │   ├── Transaction.java
│   │   │   │       │   ├── Income.java
│   │   │   │       │   ├── Expense.java
│   │   │   │       │   └── User.java
│   │   │   │       ├── util
│   │   │   │       │   └── DatabaseConnection.java
│   │   │   │       └── service
│   │   │   │           └── ExpenseTrackerService.java
│   │   └── resources
│   │       └── config.properties
│   └── test
│       └── java
│           └── com
│               └── expensetracker
│                   └── test
│                       ├── CategoryTest.java
│                       ├── TransactionTest.java
│                       └── UserTest.java
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd expense-tracker
   ```

2. **Configure the database**:
   - Update the `config.properties` file with your MySQL database connection details.

3. **Build the project**:
   - Use Maven to build the project:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   - Execute the main class `ExpenseTrackerApp` to start the application.

## Usage
- Follow the on-screen instructions to navigate through the application.
- Use the menu options to add users, transactions, and generate reports.

## License
This project is licensed under the MIT License.