package com.expensetracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate date;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Transaction(String name, Category category, User user, Double amount, LocalDate date, String description) {
        this.name = name;
        this.category = category;
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public abstract double calculateBalanceImpact();
}

@Entity
@DiscriminatorValue("EXPENSE")
@Data
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

@Entity
@DiscriminatorValue("INCOME")
@Data
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