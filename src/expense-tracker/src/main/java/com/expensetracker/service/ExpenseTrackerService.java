package com.expensetracker.service;

import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.dao.TransactionDAO;
import com.expensetracker.dao.UserDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.User;

import java.util.List;

public class ExpenseTrackerService {
    private final UserDAO userDAO;
    private final TransactionDAO transactionDAO;
    private final CategoryDAO categoryDAO;

    public ExpenseTrackerService() {
        this.userDAO = new UserDAO();
        this.transactionDAO = new TransactionDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public void addUser(User user) {
        userDAO.addUser(user);
    }

    public User getUser(int userId) {
        return userDAO.findUser(userId).orElse(null);
    }

    public void addTransaction(Transaction transaction) {
        transactionDAO.addTransaction(transaction);
    }

    public List<Transaction> getUserTransactions(int userId) {
        return transactionDAO.getTransactionsByUserId(userId);
    }

    public void addCategory(Category category) {
        categoryDAO.addCategory(category);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public void deleteUser(int userId) {
        userDAO.removeUser(userId);
    }

    public void deleteTransaction(int transactionId) {
        transactionDAO.removeTransaction(transactionId);
    }

    public void deleteCategory(int categoryId) {
        categoryDAO.removeCategory(categoryId);
    }
}