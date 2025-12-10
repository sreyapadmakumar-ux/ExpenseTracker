package com.expensetracker.service;

import com.expensetracker.model.*;
import com.expensetracker.repository.*;
import com.expensetracker.dto.*;
import com.expensetracker.exception.ExpenseTrackerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional
public class ExpenseTrackerService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    // User operations
    public User createUser(String name, String contact, String email) {
        User user = new User(name, contact, email);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Category operations
    public Category createCategory(String name) {
        Category category = new Category(name);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    // Transaction operations with DTO support
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        validateTransactionDTO(transactionDTO);
        
        User user = userRepository.findById(Long.valueOf(transactionDTO.getUserId()))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("User", transactionDTO.getUserId()));
        
        Category category = categoryRepository.findById(Long.valueOf(transactionDTO.getCategoryId()))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("Category", transactionDTO.getCategoryId()));
        
        try {
            Transaction transaction = convertToEntity(transactionDTO, user, category);
            Transaction saved = transactionRepository.save(transaction);
            return convertToDTO(saved);
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to create transaction", "TRANSACTION_CREATE_ERROR", e);
        }
    }

    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO.getId() == null) {
            throw new ExpenseTrackerException.ValidationException("Transaction ID must be provided for update");
        }
        validateTransactionDTO(transactionDTO);

        User user = userRepository.findById(Long.valueOf(transactionDTO.getUserId()))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("User", transactionDTO.getUserId()));
        
        Category category = categoryRepository.findById(Long.valueOf(transactionDTO.getCategoryId()))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("Category", transactionDTO.getCategoryId()));
        
        try {
            Transaction transaction = convertToEntity(transactionDTO, user, category);
            Transaction updated = transactionRepository.save(transaction);
            return convertToDTO(updated);
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to update transaction", "TRANSACTION_UPDATE_ERROR", e);
        }
    }

    public void deleteTransaction(Integer transactionId) {
        try {
            transactionRepository.deleteById(Long.valueOf(transactionId));
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to delete transaction", "TRANSACTION_DELETE_ERROR", e);
        }
    }

    public List<TransactionDTO> getUserTransactions(Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("User", userId));
        try {
            List<Transaction> transactions = transactionRepository.findUserTransactionsOrderByDateDesc(user);
            return convertToDTOList(transactions);
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to retrieve user transactions", "TRANSACTION_FETCH_ERROR", e);
        }
    }

    public List<TransactionDTO> getTransactionsByDateRange(Integer userId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ExpenseTrackerException.ValidationException("Start date and end date must be provided");
        }
        if (startDate.isAfter(endDate)) {
            throw new ExpenseTrackerException.ValidationException("Start date cannot be after end date");
        }

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("User", userId));
        
        try {
            List<Transaction> transactions = transactionRepository.findByUserAndDateBetween(user, startDate, endDate);
            return convertToDTOList(transactions);
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to retrieve transactions by date range", "TRANSACTION_FETCH_ERROR", e);
        }
    }

    public List<TransactionDTO> getTransactionsByCategory(Integer userId, Integer categoryId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("User", userId));
        
        Category category = categoryRepository.findById(Long.valueOf(categoryId))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("Category", categoryId));
        
        try {
            List<Transaction> transactions = transactionRepository.findByUserAndCategory(user, category);
            return convertToDTOList(transactions);
        } catch (Exception e) {
            throw new ExpenseTrackerException("Failed to retrieve transactions by category", "TRANSACTION_FETCH_ERROR", e);
        }
    }

    public BigDecimal getUserBalance(Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ExpenseTrackerException.EntityNotFoundException("User", userId));
        Double balance = transactionRepository.calculateUserBalance(user);
        return BigDecimal.valueOf(balance);
    }

    // Dashboard methods
    public DashboardSummaryDTO getDashboardSummary(Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Transaction> transactions = transactionRepository.findUserTransactionsOrderByDateDesc(user);
        
        DashboardSummaryDTO summary = new DashboardSummaryDTO();
        
        // Calculate totals
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t instanceof Income)
                .map(t -> BigDecimal.valueOf(t.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t instanceof Expense)
                .map(t -> BigDecimal.valueOf(t.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.setTotalIncome(totalIncome);
        summary.setTotalExpenses(totalExpenses);
        summary.setNetSavings(totalIncome.subtract(totalExpenses));

        // Calculate savings rate
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal savingsRate = summary.getNetSavings()
                    .divide(totalIncome, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            summary.setSavingsRate(savingsRate);
        }

        // Get monthly and category data
        summary.setMonthlyData(getMonthlyData(transactions));
        summary.setCategoryData(getCategoryData(transactions));

        return summary;
    }

    private List<MonthlyDataDTO> getMonthlyData(List<Transaction> transactions) {
        Map<YearMonth, MonthlyDataDTO> monthlyMap = new HashMap<>();
        
        transactions.forEach(transaction -> {
            YearMonth yearMonth = YearMonth.from(transaction.getDate());
            MonthlyDataDTO monthData = monthlyMap.computeIfAbsent(yearMonth, k -> {
                MonthlyDataDTO dto = new MonthlyDataDTO();
                dto.setMonth(yearMonth.toString());
                dto.setIncome(BigDecimal.ZERO);
                dto.setExpenses(BigDecimal.ZERO);
                return dto;
            });

            BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());
            if (transaction instanceof Income) {
                monthData.setIncome(monthData.getIncome().add(amount));
            } else {
                monthData.setExpenses(monthData.getExpenses().add(amount));
            }
        });

        return monthlyMap.values().stream()
                .sorted((a, b) -> b.getMonth().compareTo(a.getMonth()))
                .collect(Collectors.toList());
    }

    private List<CategoryDataDTO> getCategoryData(List<Transaction> transactions) {
        Map<Category, CategoryDataDTO> categoryMap = new HashMap<>();
        
        transactions.forEach(transaction -> {
            Category category = transaction.getCategory();
            CategoryDataDTO categoryData = categoryMap.computeIfAbsent(category, k -> {
                CategoryDataDTO dto = new CategoryDataDTO();
                dto.setCategoryId(k.getId().intValue());
                dto.setCategory(k.getName());
                dto.setAmount(BigDecimal.ZERO);
                dto.setCount(0);
                return dto;
            });

            categoryData.setAmount(categoryData.getAmount().add(BigDecimal.valueOf(transaction.getAmount())));
            categoryData.setCount(categoryData.getCount() + 1);
        });

        return new ArrayList<>(categoryMap.values());
    }

    private void validateTransactionDTO(TransactionDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            errors.add("Transaction name is required");
        }

        if (dto.getType() == null) {
            errors.add("Transaction type is required");
        } else {
            String type = dto.getType().toUpperCase();
            if (!type.equals("EXPENSE") && !type.equals("INCOME")) {
                errors.add("Invalid transaction type. Must be either EXPENSE or INCOME");
            }
        }

        if (dto.getAmount() == null) {
            errors.add("Amount is required");
        } else if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Amount must be greater than zero");
        }

        if (dto.getDate() == null) {
            errors.add("Date is required");
        }

        if (dto.getCategoryId() == null) {
            errors.add("Category ID is required");
        }

        if (dto.getUserId() == null) {
            errors.add("User ID is required");
        }

        if (!errors.isEmpty()) {
            throw new ExpenseTrackerException.ValidationException(String.join(", ", errors));
        }
    }
    }

    // DTO Conversion methods
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId().intValue());
        dto.setName(transaction.getName());
        dto.setType(transaction instanceof Expense ? "EXPENSE" : "INCOME");
        dto.setAmount(BigDecimal.valueOf(transaction.getAmount()));
        dto.setDate(Date.from(transaction.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dto.setDescription(transaction.getDescription());
        dto.setCategoryId(transaction.getCategory().getId().intValue());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setUserId(transaction.getUser().getId().intValue());
        return dto;
    }

    private Transaction convertToEntity(TransactionDTO dto, User user, Category category) {
        Transaction transaction;
        if ("EXPENSE".equals(dto.getType())) {
            transaction = new Expense();
        } else {
            transaction = new Income();
        }
        if (dto.getId() != null) {
            transaction.setId(Long.valueOf(dto.getId()));
        }
        transaction.setName(dto.getName());
        transaction.setAmount(dto.getAmount().doubleValue());
        transaction.setDate(dto.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        transaction.setDescription(dto.getDescription());
        transaction.setCategory(category);
        transaction.setUser(user);
        return transaction;
    }

    private List<TransactionDTO> convertToDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }
}