package com.finance.dto;

import com.finance.model.FinancialRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class DashboardSummary {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;                    // income - expenses
    private Map<String, BigDecimal> categoryTotals;   // e.g., {"Rent": 5000, "Salary": 50000}
    private List<FinancialRecord> recentTransactions; // last 10
	public BigDecimal getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
	public BigDecimal getTotalExpenses() {
		return totalExpenses;
	}
	public void setTotalExpenses(BigDecimal totalExpenses) {
		this.totalExpenses = totalExpenses;
	}
	public BigDecimal getNetBalance() {
		return netBalance;
	}
	public void setNetBalance(BigDecimal netBalance) {
		this.netBalance = netBalance;
	}
	public Map<String, BigDecimal> getCategoryTotals() {
		return categoryTotals;
	}
	public void setCategoryTotals(Map<String, BigDecimal> categoryTotals) {
		this.categoryTotals = categoryTotals;
	}
	public List<FinancialRecord> getRecentTransactions() {
		return recentTransactions;
	}
	public void setRecentTransactions(List<FinancialRecord> recentTransactions) {
		this.recentTransactions = recentTransactions;
	}
    
    
}