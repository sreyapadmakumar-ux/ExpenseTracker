package com.expensetracker.model;

import java.time.LocalDate;

/**
 * Represents an expense transaction.
 */
public class Expense extends Transaction {
    public Expense(String name, Category category, double amount, LocalDate date, String description) {
        super(name, category, amount, date, description);
    }

    @Override
    public double calculateBalanceImpact() {
        return -amount;
    }

    @Override
    public String getType() {
        return "expense";
    }
}