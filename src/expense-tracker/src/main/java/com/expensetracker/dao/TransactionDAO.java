package com.expensetracker.dao;

import com.expensetracker.model.Transaction;
import com.expensetracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private Connection connection;

    public TransactionDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (name, category_id, amount, date, description, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getName());
            statement.setInt(2, transaction.getCategory().getCategoryId());
            statement.setDouble(3, transaction.getAmount());
            statement.setDate(4, Date.valueOf(transaction.getDate()));
            statement.setString(5, transaction.getDescription());
            statement.setString(6, transaction.getType());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                // Assuming you have a method to create a Transaction object from the result set
                Transaction transaction = createTransactionFromResultSet(resultSet);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void deleteTransaction(int transactionId) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transactionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Transaction createTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        // Implement this method to create a Transaction object from the result set
        return null; // Placeholder return statement
    }
}