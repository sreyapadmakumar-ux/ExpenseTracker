package com.expensetracker.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String name;
    private String contact;
    private List<Transaction> transactionList;

    public User(int userId, String name, String contact) {
        this.userId = userId;
        this.name = name;
        this.contact = contact;
        this.transactionList = new ArrayList<>();
    }
    
    public int getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public List<Transaction> getTransactionList() {
        return this.transactionList;
    }

    public void addTransaction(Transaction transaction) {
        this.transactionList.add(transaction);
        System.out.printf("Transaction '%s' added for user '%s'.\n", transaction.getName(), this.name);
    }

    public void viewTransactions() {
        System.out.printf("\n--- Transactions for %s ---\n", this.name);
        if (this.transactionList.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction trans : this.transactionList) {
                System.out.println(trans.getDetails());
            }
        }
        System.out.println("----------------------------------\n");
    }

    public boolean deleteTransaction(String transactionName) {
        boolean removed = this.transactionList.removeIf(trans -> trans.getName().equals(transactionName));
        if (removed) {
            System.out.printf("Transaction '%s' deleted successfully.\n", transactionName);
        } else {
            System.out.printf("Transaction '%s' not found.\n", transactionName);
        }
        return removed;
    }

    public List<Transaction> searchTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        return transactionList.stream()
            .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
            .collect(Collectors.toList());
    }

    public List<Transaction> searchTransactionsByCategory(String categoryName) {
        return transactionList.stream()
            .filter(t -> t.getCategory().getCategoryName().equals(categoryName))
            .collect(Collectors.toList());
    }

    public List<Transaction> searchTransactionsByType(String type) {
        return transactionList.stream()
            .filter(t -> t.getType().equals(type.toLowerCase()))
            .collect(Collectors.toList());
    }
}