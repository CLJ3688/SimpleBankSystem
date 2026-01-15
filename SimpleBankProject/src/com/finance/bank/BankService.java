package com.finance.bank;

import java.math.BigDecimal;

/**
 * 銀行服務類別
 * 負責處理涉及多個帳戶的業務邏輯，如：轉帳
 */
public class BankService {
	
	private AccountDao accountDao = new AccountDao();

	/**
	 * 執行轉帳操作
	 * @param from		來源帳戶
	 * @param to		目標帳戶
	 * @param amount	轉帳金額
	 */
	public void transfer(Account from, Account to, BigDecimal amount) throws Exception {
		// --- STEP 1.檢查轉出、轉入帳號是否為null ---
		if(from == null || to == null) {
			throw new IllegalArgumentException("帳號資料不可空白");
		}
		
		// --- STEP 2.檢查轉出轉入帳號是否相同 ---
		if(from.getAccountNumber().equals(to.getAccountNumber())) {
			throw new IllegalArgumentException("來源帳號與目標帳號不可相同");
		}
		
		// --- STEP 3.檢查轉出帳號餘額是否足夠 ---
		if(from.getBalance().compareTo(amount) < 0) {
			throw new Exception("餘額不足，無法轉帳！");
		}
		
		try {
			// --- STEP 4.執行轉帳 ---
			from.withdraw(amount);
			to.deposit(amount);
			
			// --- STEP 5.轉帳執行成功後，存入資料庫 ---
			accountDao.saveAccount(from);
			accountDao.saveAccount(to);
			
			System.out.println("--- 交易成功且已寫進資料庫 ---");
			System.out.println("轉出帳號：" + from.getAccountNumber() + " | 餘額：" + from.getBalance());
			System.out.println("轉入帳號：" + to.getAccountNumber() + " | 餘額：" + to.getBalance());
		
		} catch (InsufficientBalanceException e) {
			//自定義異常處理
			System.err.println("交易失敗，原因為：" + e.getMessage());
		} catch (Exception e) {
			//其他異常處理
			System.err.println("系統異常，請聯絡客服");
			throw e;
		}
	
	}
}
