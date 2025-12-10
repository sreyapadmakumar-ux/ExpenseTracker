package com.expensetracker.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CategoryDataDTO {
    private Integer categoryId;
    private String category;
    private BigDecimal amount;
    private Integer count;
}