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
			
			// --- STEP 6.儲存交易紀錄 ---
			accountDao.saveTransaction(from.getAccountNumber(), new Transaction("[轉出]", amount, "轉帳給" + to.getAccountNumber()));
			accountDao.saveTransaction(to.getAccountNumber(), new Transaction("[轉入]" , amount, "收到來自" + from.getAccountNumber() + "的轉帳"));
			
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
	
	public void deposit(Account account, BigDecimal amount) throws Exception {
		// --- STEP 1.檢查account參數正確性
		if(account == null) {
			throw new IllegalArgumentException("帳戶不能為空");
		}
		// --- STEP 2.執行存款
		//在Account.deposit()內會檢查amount的正確性
		account.deposit(amount);
		
		// --- STEP 3.更新後餘額存入資料庫
		accountDao.saveAccount(account);
		
		// --- STEP 4.交易紀錄存入資料庫
		Transaction tx = new Transaction("[存款]", amount, "櫃檯/ATM 存款");
		accountDao.saveTransaction(account.getAccountNumber(), tx);
		
		System.out.println("--- 交易成功且已寫進資料庫 ---");
		System.out.println("[Service] 存款成功且已存入資料庫");
	}
	
	public void withdraw(Account account, BigDecimal amount) throws Exception{
		// --- STEP 1.檢查account參數正確性
		if(account == null) {
			throw new IllegalArgumentException("帳戶不能為空"); 
		}
		// --- STEP 2.執行提款
		// 在Account.withdraw()裡面會檢查是否可正確執行以及amount參數正確性
		account.withdraw(amount);
		
		// --- STEP 3.更新後餘額存入資料庫
		accountDao.saveAccount(account);
		
		// --- STEP 4.交易紀錄存入資料庫
		Transaction tx = new Transaction("[提款]", amount, "櫃檯/ATM 提款");
		accountDao.saveTransaction(account.getAccountNumber(), tx);
		
		System.out.println("--- 交易成功且已寫進資料庫 ---");
		System.out.println("[Service] 提款成功且已存入資料庫");
	}
}
