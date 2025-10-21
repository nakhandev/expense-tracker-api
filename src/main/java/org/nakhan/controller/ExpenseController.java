package org.nakhan.controller;

import jakarta.validation.Valid;
import org.nakhan.dto.ExpenseRequest;
import org.nakhan.dto.ExpenseResponse;
import org.nakhan.dto.ExpenseSummary;
import org.nakhan.model.Category;
import org.nakhan.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for expense management
 */
@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*") // Allow all origins for development
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    /**
     * Create a new expense
     */
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        try {
            ExpenseResponse response = expenseService.createExpense(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all expenses
     */
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    /**
     * Get expense by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        Optional<ExpenseResponse> expense = expenseService.getExpenseById(id);

        if (expense.isPresent()) {
            return ResponseEntity.ok(expense.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update expense
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        try {
            Optional<ExpenseResponse> updatedExpense = expenseService.updateExpense(id, request);

            if (updatedExpense.isPresent()) {
                return ResponseEntity.ok(updatedExpense.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete expense
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        boolean deleted = expenseService.deleteExpense(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get expenses by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(@PathVariable Category category) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByCategory(category);
        return ResponseEntity.ok(expenses);
    }

    /**
     * Get expenses within date range
     */
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
            return ResponseEntity.ok(expenses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get expenses by category within date range
     */
    @GetMapping("/filter/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategoryAndDateRange(
            @PathVariable Category category,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<ExpenseResponse> expenses = expenseService.getExpensesByCategoryAndDateRange(
                category, startDate, endDate);
            return ResponseEntity.ok(expenses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get expense summary by category
     */
    @GetMapping("/summary")
    public ResponseEntity<List<ExpenseSummary>> getExpenseSummaryByCategory() {
        List<ExpenseSummary> summary = expenseService.getExpenseSummaryByCategory();
        return ResponseEntity.ok(summary);
    }

    /**
     * Get expense summary by category within date range
     */
    @GetMapping("/summary/filter")
    public ResponseEntity<List<ExpenseSummary>> getExpenseSummaryByCategoryAndDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<ExpenseSummary> summary = expenseService.getExpenseSummaryByCategoryAndDateRange(
                startDate, endDate);
            return ResponseEntity.ok(summary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get total amount spent in date range
     */
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalAmountByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            BigDecimal total = expenseService.getTotalAmountByDateRange(startDate, endDate);
            return ResponseEntity.ok(total);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get total amount spent by category in date range
     */
    @GetMapping("/total/{category}")
    public ResponseEntity<BigDecimal> getTotalAmountByCategoryAndDateRange(
            @PathVariable Category category,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            BigDecimal total = expenseService.getTotalAmountByCategoryAndDateRange(
                category, startDate, endDate);
            return ResponseEntity.ok(total);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
