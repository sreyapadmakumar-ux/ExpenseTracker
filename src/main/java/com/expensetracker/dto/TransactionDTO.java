package com.expensetracker.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDTO {
    private Integer id;

    @NotBlank(message = "Transaction name is required")
    @Size(min = 1, max = 100, message = "Transaction name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Transaction type is required")
    @Pattern(regexp = "^(EXPENSE|INCOME)$", message = "Transaction type must be either EXPENSE or INCOME")
    private String type;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private Date date;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String categoryName;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotNull(message = "User ID is required")
    private Integer userId;
}