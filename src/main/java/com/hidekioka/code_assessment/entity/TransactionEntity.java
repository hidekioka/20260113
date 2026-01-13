package com.hidekioka.code_assessment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class TransactionEntity {

    // Unique identifier: must uniquely identify the purchase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    // Description: must not exceed 50 characters
    String description;

    // Transaction date: must be a valid date format
    LocalDate transactionDate;

    // Purchase amount: must be a valid positive amount rounded to the nearest cent in USD
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
