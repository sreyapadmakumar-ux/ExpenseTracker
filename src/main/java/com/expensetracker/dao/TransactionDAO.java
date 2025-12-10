package com.expensetracker.dao;

import com.expensetracker.model.*;
import com.expensetracker.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAO {
    private final DatabaseConnection dbConnection;
    private final CategoryDAO categoryDAO;

    public TransactionDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.categoryDAO = new CategoryDAO();
    }

    public void addTransaction(Transaction transaction, int userId) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, category_id, name, type, amount, date, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, transaction.getCategory().getCategoryId());
            pstmt.setString(3, transaction.getName());
            pstmt.setString(4, transaction.getType());
            pstmt.setDouble(5, transaction.calculateBalanceImpact());
            pstmt.setDate(6, Date.valueOf(transaction.getDate()));
            pstmt.setString(7, transaction.getDescription());
            
            pstmt.executeUpdate();
        }
    }

    public List<Transaction> getUserTransactions(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Optional<Category> category = categoryDAO.findById(rs.getInt("category_id"));
                    if (category.isPresent()) {
                        Transaction transaction = createTransactionFromResultSet(rs, category.get());
                        if (transaction != null) {
                            transactions.add(transaction);
                        }
                    }
                }
            }
        }
        return transactions;
    }

    public List<Transaction> searchTransactionsByDate(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND date BETWEEN ? AND ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Optional<Category> category = categoryDAO.findById(rs.getInt("category_id"));
                    if (category.isPresent()) {
                        Transaction transaction = createTransactionFromResultSet(rs, category.get());
                        if (transaction != null) {
                            transactions.add(transaction);
                        }
                    }
                }
            }
        }
        return transactions;
    }

    public List<Transaction> searchTransactionsByCategory(int userId, String categoryName) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.* FROM transactions t " +
                    "JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND c.name = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, categoryName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Optional<Category> category = categoryDAO.findById(rs.getInt("category_id"));
                    if (category.isPresent()) {
                        Transaction transaction = createTransactionFromResultSet(rs, category.get());
                        if (transaction != null) {
                            transactions.add(transaction);
                        }
                    }
                }
            }
        }
        return transactions;
    }

    public boolean deleteTransaction(int transactionId, int userId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ? AND user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private Transaction createTransactionFromResultSet(ResultSet rs, Category category) throws SQLException {
        String type = rs.getString("type");
        String name = rs.getString("name");
        double amount = Math.abs(rs.getDouble("amount")); // Store amount as absolute value
        LocalDate date = rs.getDate("date").toLocalDate();
        String description = rs.getString("description");

        if ("INCOME".equals(type)) {
            return new Income(name, category, amount, date, description);
        } else if ("EXPENSE".equals(type)) {
            return new Expense(name, category, amount, date, description);
        }
        return null;
    }
}