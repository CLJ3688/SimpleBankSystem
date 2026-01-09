package com.finance.bank;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 銀行帳戶類別
 * 負責維護帳戶基本資訊及餘額狀態。
 */
public class Account {
	private String accountNumber;
	
	/**
	 * 使用BigDecimal以避免金融運算中的浮點數精度誤差
	 */
	private BigDecimal balance;
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public Account(String accountNumber, String initialBalance) {
		this.accountNumber = accountNumber;
		this.balance = new BigDecimal(initialBalance);
	}
	
	/**
	 * 執行提款操作
	 * @param amount 提款金額
	 * @throws InsufficientBalanceException 當餘額不足時拋出異常
	 */
	
	public void withdraw(BigDecimal amount) {
		
		if(amount == null) {
			throw new IllegalArgumentException("提款金額不能為空");
		}
		//檢查amount正確性
		if(amount.compareTo(BigDecimal.ZERO)  <= 0) {
			throw new IllegalArgumentException("提款金額必須為正數");
		}
		
		//檢查餘額是否足以支付
		if(balance.compareTo(amount) < 0) {
			throw new InsufficientBalanceException("餘額不足，無法提款");
		}
		
		this.balance = this.balance.subtract(amount);
		
		//採四捨五入，確保金額維持小數點後兩位
		this.balance = this.balance.setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * 執行存款操作
	 * @param amount 存款金額
	 * @throws IllegalArgumentException 金額小於等於零時拋出
	 */
	
	public void deposit(BigDecimal amount) {
		
		if(amount == null) {
			throw new IllegalArgumentException("存款金額不能為空");
		}
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("金額必須為正數");
		}
		
		this.balance = this.balance.add(amount);
		
		//採四捨五入，確保金額維持小數點後兩位
		this.balance = this.balance.setScale(2, RoundingMode.HALF_UP);
	}

}
