package org.nakhan.dto;

import jakarta.validation.constraints.*;
import org.nakhan.model.Category;
import java.math.BigDecimal;

/**
 * DTO for expense creation and update requests
 */
public class ExpenseRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 digits before decimal and 2 after")
    private BigDecimal amount;

    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Date is required")
    private String date; // Using String to accept date in YYYY-MM-DD format

    // Default constructor
    public ExpenseRequest() {}

    // Constructor
    public ExpenseRequest(BigDecimal amount, String description, Category category, String date) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ExpenseRequest{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", date='" + date + '\'' +
                '}';
    }
}
