package com.expensetracker.controller;

import com.expensetracker.dto.*;
import com.expensetracker.service.ExpenseTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${app.cors.allowed-origins:*}")
public class ExpenseTrackerController {

    @Autowired
    private ExpenseTrackerService service;

    // Transaction endpoints
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(service.createTransaction(transactionDTO));
    }

    @PutMapping("/transactions")
    public ResponseEntity<TransactionDTO> updateTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(service.updateTransaction(transactionDTO));
    }

    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer transactionId) {
        service.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/transactions/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getUserTransactions(userId));
    }

    @GetMapping("/transactions/user/{userId}/dateRange")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.getTransactionsByDateRange(userId, startDate, endDate));
    }

    @GetMapping("/transactions/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCategory(
            @PathVariable Integer userId,
            @PathVariable Integer categoryId) {
        return ResponseEntity.ok(service.getTransactionsByCategory(userId, categoryId));
    }

    // Dashboard endpoints
    @GetMapping("/dashboard/summary/{userId}")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getDashboardSummary(userId));
    }

    @GetMapping("/dashboard/balance/{userId}")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getUserBalance(userId));
    }

    // Category endpoints
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestParam String name) {
        Category category = service.createCategory(name);
        return ResponseEntity.ok(category);
    }

    // Transaction endpoints
    @PostMapping("/transactions/expense")
    public ResponseEntity<Transaction> addExpense(@RequestParam String name,
                                                @RequestParam Long categoryId,
                                                @RequestParam Long userId,
                                                @RequestParam Double amount,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                @RequestParam(required = false) String description) {
        return service.getCategoryById(categoryId)
                .flatMap(category -> service.getUserById(userId)
                        .map(user -> service.createExpense(name, category, user, amount, date, description)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/transactions/income")
    public ResponseEntity<Transaction> addIncome(@RequestParam String name,
                                               @RequestParam Long categoryId,
                                               @RequestParam Long userId,
                                               @RequestParam Double amount,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                               @RequestParam(required = false) String description) {
        return service.getCategoryById(categoryId)
                .flatMap(category -> service.getUserById(userId)
                        .map(user -> service.createIncome(name, category, user, amount, date, description)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
        return service.getUserById(userId)
                .map(user -> ResponseEntity.ok(service.getUserTransactions(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/transactions/search")
    public ResponseEntity<List<Transaction>> searchTransactions(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.getUserById(userId)
                .map(user -> ResponseEntity.ok(service.getTransactionsByDateRange(user, startDate, endDate)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/balance")
    public ResponseEntity<Double> getUserBalance(@PathVariable Long userId) {
        return service.getUserById(userId)
                .map(user -> ResponseEntity.ok(service.getUserBalance(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}