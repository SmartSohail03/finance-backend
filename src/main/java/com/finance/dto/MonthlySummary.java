package com.finance.dto;

import java.math.BigDecimal;

public class MonthlySummary {

    private int month;
    private int year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;

    
    public MonthlySummary(int month, int year, BigDecimal totalIncome, BigDecimal totalExpenses,
			BigDecimal netBalance) {
		
		this.month = month;
		this.year = year;
		this.totalIncome = totalIncome;
		this.totalExpenses = totalExpenses;
		this.netBalance = netBalance;
	}


	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
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
    
    
    
    
}
