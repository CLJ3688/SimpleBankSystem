package com.finance.bank;

import java.math.BigDecimal;

/**
 * 銀行服務類別
 * 負責處理涉及多個帳戶的業務邏輯，如：轉帳
 */
public class BankService {

	/**
	 * 執行轉帳操作
	 * @param from		來源帳戶
	 * @param to		目標帳戶
	 * @param amount	轉帳金額
	 */
	
	public void transfer(Account from, Account to, BigDecimal amount) {
		//檢查轉出、轉入帳號是否為null
		if(from == null) {
			throw new IllegalArgumentException("請填寫轉出帳號");
		}
		if(to == null) {
			throw new IllegalArgumentException("請填寫轉入帳號");
		}
		
		//檢查轉出轉入帳號是否相同
		if(from.getAccountNumber().equals(to.getAccountNumber())) {
			throw new IllegalArgumentException("來源帳號與目標帳號不可相同");
		}
		
		try {
			//先扣款，若餘額不足會拋出InsufficientBalanceException
			from.withdraw(amount);
			//扣款成功後才存款
			to.deposit(amount);
			
			System.out.println("---交易成功---");
			System.out.println("轉出帳號：" + from.getAccountNumber());
			System.out.println("轉入帳號：" + to.getAccountNumber());
			System.out.println("交易金額：" + amount);
		} catch (InsufficientBalanceException e) {
			//商業邏輯異常處理
			System.err.println("交易失敗，原因為：" + e.getMessage());
		} catch (Exception e) {
			//其他異常處理
			System.err.println("系統異常，請聯絡客服");
			throw e;
		}
	
	}
}
