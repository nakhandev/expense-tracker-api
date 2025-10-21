package org.nakhan.repository;

import org.nakhan.model.Category;
import org.nakhan.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Expense entity
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    /**
     * Find expenses by category
     */
    List<Expense> findByCategory(Category category);

    /**
     * Find expenses by date
     */
    List<Expense> findByDate(LocalDate date);

    /**
     * Find expenses by category and date
     */
    List<Expense> findByCategoryAndDate(Category category, LocalDate date);

    /**
     * Find expenses within date range
     */
    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find expenses by category within date range
     */
    List<Expense> findByCategoryAndDateBetween(Category category, LocalDate startDate, LocalDate endDate);

    /**
     * Get expense summary by category
     */
    @Query("SELECT new org.nakhan.dto.ExpenseSummary(e.category, SUM(e.amount), COUNT(e)) " +
           "FROM Expense e " +
           "GROUP BY e.category " +
           "ORDER BY SUM(e.amount) DESC")
    List<org.nakhan.dto.ExpenseSummary> getExpenseSummaryByCategory();

    /**
     * Get expense summary by category within date range
     */
    @Query("SELECT new org.nakhan.dto.ExpenseSummary(e.category, SUM(e.amount), COUNT(e)) " +
           "FROM Expense e " +
           "WHERE e.date BETWEEN :startDate AND :endDate " +
           "GROUP BY e.category " +
           "ORDER BY SUM(e.amount) DESC")
    List<org.nakhan.dto.ExpenseSummary> getExpenseSummaryByCategoryAndDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    /**
     * Get total amount spent in date range
     */
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.date BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalAmountByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    /**
     * Get total amount spent by category in date range
     */
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.category = :category AND e.date BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalAmountByCategoryAndDateRange(
        @Param("category") Category category,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    /**
     * Count expenses in date range
     */
    long countByDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Count expenses by category in date range
     */
    long countByCategoryAndDateBetween(Category category, LocalDate startDate, LocalDate endDate);
}
