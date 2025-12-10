package com.expensetracker.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDTO {
    private Integer id;
    private String name;
    private String type;
    private BigDecimal amount;
    private Date date;
    private String description;
    private String categoryName;
    private Integer categoryId;
    private Integer userId;
}

@Data
public class DashboardSummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netSavings;
    private BigDecimal savingsRate;
    private List<MonthlyDataDTO> monthlyData;
    private List<CategoryDataDTO> categoryData;
}

@Data
public class MonthlyDataDTO {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal savings;
}

@Data
public class CategoryDataDTO {
    private String category;
    private BigDecimal amount;
    private Integer transactionCount;
}