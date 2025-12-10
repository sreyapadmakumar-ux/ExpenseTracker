package com.expensetracker.model;

import java.time.LocalDate;

public class Income extends Transaction {
    public Income(String name, Category category, double amount, LocalDate date, String description) {
        super(name, category, amount, date, description);
    }

    @Override
    public double calculateBalanceImpact() {
        return amount;
    }

    @Override
    public String getType() {
        return "income";
    }
}