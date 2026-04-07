package com.finance.service;

import com.finance.dto.DashboardSummary;
import com.finance.dto.MonthlySummary;
import com.finance.dto.RecordRequest;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.FinancialRecord;
import com.finance.model.RecordType;
import com.finance.repository.FinancialRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;

    public FinancialRecordService(FinancialRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    // --- Create Record ---
    public FinancialRecord createRecord(RecordRequest request) {
        FinancialRecord record = toEntity(request);
        return recordRepository.save(record);
    }

    // --- Get All Records ---
    public List<FinancialRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    // --- Get Record By ID ---
    public FinancialRecord getRecordById(Long id) {
        return findRecordById(id);
    }

    // --- Update Record ---
    public FinancialRecord updateRecord(Long id, RecordRequest request) {
        FinancialRecord existing = findRecordById(id);

        // Update all fields
        existing.setAmount(request.getAmount());
        existing.setType(request.getType());
        existing.setCategory(request.getCategory());
        existing.setDate(request.getDate());
        existing.setDescription(request.getDescription());

        return recordRepository.save(existing);
    }

    // --- Delete Record ---
    public void deleteRecord(Long id) {
        FinancialRecord record = findRecordById(id);
        recordRepository.delete(record);
    }

    // --- Filter Records ---
    // Each filter is a separate method — simple and clean
    public List<FinancialRecord> filterByType(String type) {
        try {
            RecordType recordType = RecordType.valueOf(type.toUpperCase());
            return recordRepository.findByType(recordType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type: " + type + ". Must be INCOME or EXPENSE");
        }
    }

    public List<FinancialRecord> filterByCategory(String category) {
        return recordRepository.findByCategory(category);
    }

    public List<FinancialRecord> filterByDate(LocalDate date) {
        return recordRepository.findByDate(date);
    }

    public List<FinancialRecord> filterByDateRange(LocalDate startDate, LocalDate endDate) {
        return recordRepository.findByDateBetween(startDate, endDate);
    }

    // --- Dashboard: Full Summary ---
    public DashboardSummary getDashboardSummary() {

        BigDecimal totalIncome = recordRepository.sumByType(RecordType.INCOME);
        BigDecimal totalExpenses = recordRepository.sumByType(RecordType.EXPENSE);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        // Convert Object[] list → Map<category, total>
        List<Object[]> rawCategoryTotals = recordRepository.sumByCategory();
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        for (Object[] row : rawCategoryTotals) {
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            categoryTotals.put(category, total);
        }

        List<FinancialRecord> recentTransactions = recordRepository.findTop10ByOrderByDateDesc();

        DashboardSummary summary = new DashboardSummary();
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpenses(totalExpenses);
        summary.setNetBalance(netBalance);
        summary.setCategoryTotals(categoryTotals);
        summary.setRecentTransactions(recentTransactions);

        return summary;
    }

    // --- Dashboard: Monthly Summary ---
    public MonthlySummary getMonthlySummary(int month, int year) {

        List<FinancialRecord> records = recordRepository.findByMonthAndYear(month, year);

        // Calculate income and expenses from filtered records
        BigDecimal totalIncome = records.stream()
                .filter(r -> r.getType() == RecordType.INCOME)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = records.stream()
                .filter(r -> r.getType() == RecordType.EXPENSE)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return new MonthlySummary(month, year, totalIncome, totalExpenses, netBalance);
    }

    // --- Helper: find record or throw 404 ---
    private FinancialRecord findRecordById(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
    }

    // --- Helper: convert RecordRequest DTO → FinancialRecord entity ---
    private FinancialRecord toEntity(RecordRequest request) {
        FinancialRecord record = new FinancialRecord();
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setDescription(request.getDescription());
        return record;
    }
}
