package com.hidekioka.code_assessment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO implements Serializable {

    // Unique identifier: must uniquely identify the purchase
    @Null(message = "Id field should not be present on 'create' operation")
    Integer id;

    // Description: must not exceed 50 characters
    @Size(max = 50, message = "Description: must not exceed 50 character")
    String description;

    // Transaction date: must be a valid date format
    @NotNull(message = "Transaction date: must be a valid date format")
    @PastOrPresent(message = "Transaction date: cannot be in the future")
    LocalDate transactionDate;

    // Purchase amount: must be a valid positive amount rounded to the nearest cent in USD
    @NotNull(message = "Purchase amount: must be a valid positive amount")
    @DecimalMin(value = "0.0", inclusive = false, message = "Purchase amount: must be a valid positive amount")
    BigDecimal purchaseAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}
