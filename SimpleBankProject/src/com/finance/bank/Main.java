package com.finance.bank;

import java.math.BigDecimal;

public class Main {

	public static void main(String[] args) {
		//1. 初始化帳戶
		Account userA = new Account("userA_001", "1000.0");
		Account userB = new Account("userB_001", "500.0");
		BankService bankService = new BankService();
		
		System.out.println("交易前A餘額為：" + userA.getBalance());
		System.out.println("交易前B餘額為：" + userB.getBalance());

		//2. 測試正常轉帳
		try {
			bankService.transfer(userA, userB, new BigDecimal("200.50"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		//3. 嘗試超額轉帳
		try {
			System.out.println("---嘗試超額轉帳---");
			bankService.transfer(userA, userB, new BigDecimal("2000.0"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("交易後A餘額為：" + userA.getBalance());
		System.out.println("交易後B餘額為：" + userB.getBalance());
	}

}
