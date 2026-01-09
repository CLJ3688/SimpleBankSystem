package com.finance.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 交易紀錄類別
 */

public class Transaction {
	private LocalDateTime timeStamp;	//交易時間
	private String type;	//交易類型：提款、存款、轉帳
	private BigDecimal amount;	//金額
	private String description;	//備註
	
	public Transaction(String type, BigDecimal amount, String description) {
		this.timeStamp = LocalDateTime.now();	//紀錄物件建立當下時間
		this.type = type;
		this.amount = amount;
		this.description = description;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return String.format("[%s] %-4s | 金額：%10s | 備註：%s", timeStamp.format(formatter), type, amount, description);
	}
	
}
