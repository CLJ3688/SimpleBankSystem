package com.finance.bank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		// --- 1 : 環境初始化 ---
		DatabaseManager.initializeDatabase();
		AccountDao dao = new AccountDao();
		BankService bankService = new BankService();
		
		// --- 2-a : UserA，資料庫中搜尋紀錄，若無則新增 ---
		Account userA = dao.getAccount("userA_001");
		if(userA == null) {
			userA = new Account("userA_001","1000.0");
			dao.saveAccount(userA);
			System.out.println("[系統] 首次執行：已為 userA 建立初始帳戶（1000.0）");
		} else {
			System.out.println("[系統] 歡迎回來！userA 目前餘額為：" + userA.getBalance());
		}
		
		// --- 2-b : UserB，資料庫中搜尋紀錄，若無則新增 ---
		Account userB = dao.getAccount("userB_001");
		if(userB == null) {
			userB = new Account("userB_001","500.0");
			dao.saveAccount(userB);
			System.out.println("[系統] 首次執行：已為 userB 建立初始帳戶（500.0）");
		} else {
			System.out.println("[系統] 歡迎回來！userB 目前餘額為：" + userB.getBalance());
		}
		
		// --- 3 : 執行正常轉帳及例外處理 ---
		try {
			System.out.println("[執行轉帳] A 轉帳200.00 元給 B ...");
			bankService.transfer(userA, userB, new BigDecimal("200.00"));
			
			System.out.println("[執行存款] A 存入500.00 元 ...");
			bankService.deposit(userA, new BigDecimal("500.00"));
			
			System.out.println("[執行提款] A 提領300.00 元 ...");
			bankService.withdraw(userA, new BigDecimal("300.00"));
			
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
		
		System.out.println("\n============= 從資料庫讀取交易明細 =============");
		System.out.println("             銀行交易對帳單(userA_001)         ");
		System.out.println("=============================================");
		
		List<Transaction> txs = dao.getTransactions("userA_001");
		txs.forEach(tx -> {
			System.out.println(tx.toString());
		});
		
		System.out.println("=============================================");
		System.out.println("列印完畢，共" + txs.size() + " 筆資料");
	}

}
