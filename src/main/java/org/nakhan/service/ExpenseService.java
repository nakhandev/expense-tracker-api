package org.nakhan.service;

import org.nakhan.dto.ExpenseRequest;
import org.nakhan.dto.ExpenseResponse;
import org.nakhan.dto.ExpenseSummary;
import org.nakhan.model.Category;
import org.nakhan.model.Expense;
import org.nakhan.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for expense-related business logic
 */
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    /**
     * Create a new expense
     */
    public ExpenseResponse createExpense(ExpenseRequest request) {
        // Parse date string to LocalDate
        LocalDate date = parseDate(request.getDate());

        // Create expense entity
        Expense expense = new Expense(
            request.getAmount(),
            request.getDescription(),
            request.getCategory(),
            date
        );

        // Save to database
        Expense savedExpense = expenseRepository.save(expense);

        // Return response
        return mapToResponse(savedExpense);
    }

    /**
     * Get all expenses
     */
    public List<ExpenseResponse> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                      .map(this::mapToResponse)
                      .collect(Collectors.toList());
    }

    /**
     * Get expense by ID
     */
    public Optional<ExpenseResponse> getExpenseById(Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.map(this::mapToResponse);
    }

    /**
     * Update expense
     */
    public Optional<ExpenseResponse> updateExpense(Long id, ExpenseRequest request) {
        Optional<Expense> existingExpense = expenseRepository.findById(id);

        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();

            // Update fields
            expense.setAmount(request.getAmount());
            expense.setDescription(request.getDescription());
            expense.setCategory(request.getCategory());
            expense.setDate(parseDate(request.getDate()));

            // Save updated expense
            Expense updatedExpense = expenseRepository.save(expense);

            return Optional.of(mapToResponse(updatedExpense));
        }

        return Optional.empty();
    }

    /**
     * Delete expense
     */
    public boolean deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get expenses by category
     */
    public List<ExpenseResponse> getExpensesByCategory(Category category) {
        List<Expense> expenses = expenseRepository.findByCategory(category);
        return expenses.stream()
                      .map(this::mapToResponse)
                      .collect(Collectors.toList());
    }

    /**
     * Get expenses within date range
     */
    public List<ExpenseResponse> getExpensesByDateRange(String startDate, String endDate) {
        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);

        List<Expense> expenses = expenseRepository.findByDateBetween(start, end);
        return expenses.stream()
                      .map(this::mapToResponse)
                      .collect(Collectors.toList());
    }

    /**
     * Get expenses by category within date range
     */
    public List<ExpenseResponse> getExpensesByCategoryAndDateRange(Category category, String startDate, String endDate) {
        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);

        List<Expense> expenses = expenseRepository.findByCategoryAndDateBetween(category, start, end);
        return expenses.stream()
                      .map(this::mapToResponse)
                      .collect(Collectors.toList());
    }

    /**
     * Get expense summary by category
     */
    public List<ExpenseSummary> getExpenseSummaryByCategory() {
        return expenseRepository.getExpenseSummaryByCategory();
    }

    /**
     * Get expense summary by category within date range
     */
    public List<ExpenseSummary> getExpenseSummaryByCategoryAndDateRange(String startDate, String endDate) {
        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);

        return expenseRepository.getExpenseSummaryByCategoryAndDateRange(start, end);
    }

    /**
     * Get total amount spent in date range
     */
    public BigDecimal getTotalAmountByDateRange(String startDate, String endDate) {
        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);

        BigDecimal total = expenseRepository.getTotalAmountByDateRange(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Get total amount spent by category in date range
     */
    public BigDecimal getTotalAmountByCategoryAndDateRange(Category category, String startDate, String endDate) {
        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);

        BigDecimal total = expenseRepository.getTotalAmountByCategoryAndDateRange(category, start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Parse date string to LocalDate
     */
    private LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd format.");
        }
    }

    /**
     * Map Expense entity to ExpenseResponse DTO
     */
    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            expense.getCategory(),
            expense.getDate(),
            expense.getCreatedAt(),
            expense.getUpdatedAt()
        );
    }
}
