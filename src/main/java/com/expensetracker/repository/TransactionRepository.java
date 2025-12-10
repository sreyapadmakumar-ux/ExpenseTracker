package com.expensetracker.repository;

import com.expensetracker.model.Transaction;
import com.expensetracker.model.User;
import com.expensetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    List<Transaction> findByUserAndCategory(User user, Category category);
    
    @Query("SELECT t FROM Transaction t WHERE t.user = ?1 ORDER BY t.date DESC")
    List<Transaction> findUserTransactionsOrderByDateDesc(User user);
    
    @Query("SELECT SUM(CASE WHEN TYPE(t) = Income THEN t.amount ELSE -t.amount END) FROM Transaction t WHERE t.user = ?1")
    Double calculateUserBalance(User user);
}