/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : Loan.java
* description : The class for Loan
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

//This class is used for the loan information
public class Loan {
	private int loanDay;               //the day when the monthly loan should return
	private int loanMonth;             //the last month time when the loan is return;        	   
	private int principle;
	private double interest;
	private double amount;
	private int month;
	public int monthPast;
	private int monthLeft;
 	
	public Loan(int principle, double interest, int month){
		this.principle = principle;
		this.interest = interest;
		this.month = month;
		amount = principle * interest + principle;
	}
	
	public Loan(){
		loanDay = 0;
		loanMonth = 0;
		principle = 0;
		interest = 0;
		amount = 0;
		month = 0;
		monthPast = 0;
		monthLeft = 0;
	}
	
	public static double roundChange(double a,int b){ 
		if(b < 0){
			return a; 
		}
		int k = 1; 
		for(int i = 0; i < b; i++){ 
			k = k * 10; 
		} 
		return ((double)Math.round(a*k))/k; 
	} 
	
	public int getMonthLeft(){
		monthLeft = month - monthPast;
		return monthLeft;
	}
	
	public double getAmount(){
		return this.amount;
	}
	
	public void setNextMonth(){
		++loanMonth;
		if(loanMonth > 12){
			loanMonth = 1;
		}
	}
	
	public double getMonthPaid(){
		double result = roundChange(amount / month, 1);
		return result;
	}
	
	public int getLoanDay(){
		return this.loanDay;
	}
	
	public int getLoanMonth(){
		return this.loanMonth;
	}
	
	
	public int getMounth(){
		return this.month;
	}
	
	public int getMounthPast(){
		return this.monthPast;
	}
	
	public double getAmountPaidBack(){
		double paidBack = getMonthPaid() * monthPast;
		double result = roundChange(paidBack, 1);
		return result;
	}
	
	public int getPrinciple(){
		return principle;
	}
	
	public double getInterest(){
		return principle * interest;
	}
	
	public void setLoanDay(int loanDay){
		this.loanDay = loanDay;
	}
	
	public void setLoanMonth(int loanMonth){
		this.loanMonth = loanMonth;
	}
	
	public void setMounthPast(int mounthPast){
		this.monthPast = mounthPast;
	}
}
