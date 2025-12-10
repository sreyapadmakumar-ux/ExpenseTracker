package com.expensetracker.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyDataDTO {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal savings;
}