/*
*
* Copyright (C) 2011-2014 Wang Shiliang
* All rights reserved
* filename : User.java
* description : The class for user
* 
* created by Wang Shiliang at 5/4/2012 20:19:50
*
*/
package org.seedsofempowerment.farmsim;

public class User {
	private String name;
	private int age;
	private int gender;
	private int balance;
	private double time;
	private int totalEarning;
	private int borrowed;
	private int score;

	public User(){
		name = "";
		age = 0;
		gender = 0;
		balance = 0;
		time = 0;
		totalEarning = 0;
		borrowed = 0;
		score = 0;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public int getGender() {
		return gender;
	}
	
	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public double getTime() {
		return time;
	}
	
	public void setTime(double time) {
		this.time = time;
	}
	
	public int getTotalEarning() {
		return totalEarning;
	}
	
	public void setTotalEarning(int totalEarning) {
		this.totalEarning = totalEarning;
	}
	
	public int getBorrowed() {
		return borrowed;
	}
	
	public void setBorrowed(int borrowed) {
		this.borrowed = borrowed;
	}
	
	public int getScore() {
		if(time * GameInfo.speedChange < 10){
			score = (int) ((balance + borrowed ) / time / GameInfo.speedChange) / 10;
		}
		else{
			score = (int) ((balance + borrowed ) / time / GameInfo.speedChange);
		}
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
