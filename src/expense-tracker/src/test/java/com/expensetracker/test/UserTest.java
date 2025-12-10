package com.expensetracker.test;

import com.expensetracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "John Doe", "john.doe@example.com");
    }

    @Test
    void testGetUserId() {
        assertEquals(1, user.getUserId());
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testGetContact() {
        assertEquals("john.doe@example.com", user.getContact());
    }

    @Test
    void testAddTransaction() {
        // Assuming Transaction is a valid class and we can create an instance
        // Transaction transaction = new Transaction("Salary", category, 1000, LocalDate.now(), "Monthly Salary");
        // user.addTransaction(transaction);
        // assertEquals(1, user.getTransactionList().size());
    }

    @Test
    void testViewTransactions() {
        // This test would require mocking or implementing the viewTransactions method
        // user.viewTransactions();
        // assertTrue(true); // Placeholder assertion
    }

    @Test
    void testDeleteTransaction() {
        // Assuming we have a method to delete a transaction
        // user.deleteTransaction("Salary");
        // assertEquals(0, user.getTransactionList().size());
    }
}