package com.expensetracker.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("INCOME")
@NoArgsConstructor
public class Income extends Transaction {

    public Income(String name, Category category, User user, Double amount, LocalDate date, String description) {
        super(name, category, user, amount, date, description);
    }

    @Override
    public double calculateBalanceImpact() {
        return getAmount();
    }
}