package com.expensetracker.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardSummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netSavings;
    private BigDecimal savingsRate;
    private List<MonthlyDataDTO> monthlyData;
    private List<CategoryDataDTO> categoryData;
}