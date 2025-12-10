package com.expensetracker.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("EXPENSE")
@NoArgsConstructor
public class Expense extends Transaction {

    public Expense(String name, Category category, User user, Double amount, LocalDate date, String description) {
        super(name, category, user, amount, date, description);
    }

    @Override
    public double calculateBalanceImpact() {
        return -getAmount();
    }
}