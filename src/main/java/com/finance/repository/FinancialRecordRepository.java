package com.finance.repository;

import com.finance.model.FinancialRecord;
import com.finance.model.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Filter by type (INCOME or EXPENSE)
    List<FinancialRecord> findByType(RecordType type);

    // Filter by category (e.g., "Rent", "Salary")
    List<FinancialRecord> findByCategory(String category);

    // Filter by exact date
    List<FinancialRecord> findByDate(LocalDate date);

    // Filter by date range (e.g., this month)
    List<FinancialRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // --- Dashboard queries ---

    // Sum all amounts by type (used for total income / total expenses)
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = :type")
    BigDecimal sumByType(RecordType type);

    // Get totals grouped by category
    @Query("SELECT r.category, SUM(r.amount) FROM FinancialRecord r GROUP BY r.category")
    List<Object[]> sumByCategory();

    // Get last N records ordered by date (used for recent transactions)
    List<FinancialRecord> findTop10ByOrderByDateDesc();

    // Get records for a specific month and year
    @Query("SELECT r FROM FinancialRecord r WHERE MONTH(r.date) = :month AND YEAR(r.date) = :year")
    List<FinancialRecord> findByMonthAndYear(int month, int year);
}