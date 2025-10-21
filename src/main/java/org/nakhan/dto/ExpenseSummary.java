package org.nakhan.dto;

import org.nakhan.model.Category;
import java.math.BigDecimal;

/**
 * DTO for expense summary by category
 */
public class ExpenseSummary {

    private Category category;
    private BigDecimal totalAmount;
    private Long expenseCount;

    // Default constructor
    public ExpenseSummary() {}

    // Constructor
    public ExpenseSummary(Category category, BigDecimal totalAmount, Long expenseCount) {
        this.category = category;
        this.totalAmount = totalAmount;
        this.expenseCount = expenseCount;
    }

    // Getters and Setters
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(Long expenseCount) {
        this.expenseCount = expenseCount;
    }

    @Override
    public String toString() {
        return "ExpenseSummary{" +
                "category=" + category +
                ", totalAmount=" + totalAmount +
                ", expenseCount=" + expenseCount +
                '}';
    }
}
