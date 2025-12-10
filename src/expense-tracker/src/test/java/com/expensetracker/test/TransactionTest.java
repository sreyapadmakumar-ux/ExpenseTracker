package com.expensetracker.test;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.model.Income;
import com.expensetracker.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    @Test
    public void testIncomeTransaction() {
        Category category = new Category(1, "Salary");
        Transaction income = new Income("Monthly Salary", category, 3000.00, LocalDate.now(), "Salary for the month");

        assertEquals("income", income.getType());
        assertEquals(3000.00, income.calculateBalanceImpact());
        assertEquals("Monthly Salary", income.getName());
    }

    @Test
    public void testExpenseTransaction() {
        Category category = new Category(2, "Groceries");
        Transaction expense = new Expense("Weekly Groceries", category, 150.00, LocalDate.now(), "Groceries for the week");

        assertEquals("expense", expense.getType());
        assertEquals(-150.00, expense.calculateBalanceImpact());
        assertEquals("Weekly Groceries", expense.getName());
    }
}