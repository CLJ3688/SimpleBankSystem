package com.finance.bank;

import java.math.BigDecimal;

public class Main {

	public static void main(String[] args) {
		
		// --- 1 : 環境初始化 ---
		// 程式啟動時，確保資料庫表格已建立
		DatabaseManager.initializeDatabase();
		
		AccountDao dao = new AccountDao();
		BankService bankService = new BankService();
		
		// --- 2 : 準備測試數據 ---
		Account userA = new Account("userA_001", "1000.0");
		Account userB = new Account("userB_001", "500.0");
		
		System.out.println("[初始狀態]");
		System.out.println("A餘額：" + userA.getBalance());
		System.out.println("B餘額：" + userB.getBalance());
		System.out.println("------------");
		
		// --- 3 : 執行正常轉帳及例外處理 ---
		try {
			System.out.println("[執行轉帳] A 轉帳200.50 元給 B ...");
			bankService.transfer(userA, userB, new BigDecimal("200.50"));
			
			System.out.println("[執行轉帳] B 轉帳100.00 元給 A ...");
			bankService.transfer(userB, userA, new BigDecimal("100.00"));
			
			System.out.println("[執行轉帳] A 轉帳50.00 元給 B ...");
			bankService.transfer(userA, userB, new BigDecimal("50.00"));
			
			System.out.println("[執行轉帳] A 轉帳2000.00 元給 B ...");
			System.out.println("---嘗試超額轉帳---");
			bankService.transfer(userA, userB, new BigDecimal("2000.0"));
			
			System.out.println("[執行轉帳] A 轉帳50.00 元給 A ...");
			System.out.println("---嘗試轉帳給自己---");
			bankService.transfer(userA, userA, new BigDecimal("50.0"));
		} catch (Exception e) {
			System.err.println("交易過程中發生錯誤：" + e.getMessage());
		}
		
		// --- 4 : 最終驗證，從資料庫重新抓取 ---
		System.out.println("--- 交易結束報告 ---");
		System.out.println("[資料庫驗證結果]");
		
		Account dbA = dao.getAccount("userA_001");
		Account dbB = dao.getAccount("userB_001");
		
		if(dbA != null && dbB != null) {
			System.out.println("資料庫中A的最新餘額：" + dbA.getBalance());
			System.out.println("資料庫中B的最新餘額：" + dbB.getBalance());
		}
		
		userA.printHistory();
	}

}
