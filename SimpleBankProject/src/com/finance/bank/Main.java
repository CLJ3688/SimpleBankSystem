package com.finance.bank;

import java.math.BigDecimal;

public class Main {

	public static void main(String[] args) {
		
		// --- STEP 1 : 資料庫基礎建設 ---
		DatabaseManager.initializeDatabase();
		AccountDao dao = new AccountDao();
		
		// --- STEP 2 : 初始化帳戶物件 ---
		Account userA = new Account("userA_001", "1000.0");
		Account userB = new Account("userB_001", "500.0");
		
		// [新增] 同步初始資料到資料庫：確保DB有這兩個帳戶
		dao.saveAccount(userA);
		dao.saveAccount(userB);
		
		BankService bankService = new BankService();
		
		System.out.println("交易前A餘額為：" + userA.getBalance());
		System.out.println("交易前B餘額為：" + userB.getBalance());

		// --- STEP 3 : 執行轉帳邏輯 ---
		try {
			bankService.transfer(userA, userB, new BigDecimal("200.50"));
			//每做完一筆交易，同步回資料庫
			dao.saveAccount(userA);
			dao.saveAccount(userB);
			
			bankService.transfer(userB, userA, new BigDecimal("100.00"));
			bankService.transfer(userA, userB, new BigDecimal("50.00"));
			
			dao.saveAccount(userA);
			dao.saveAccount(userB);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// --- STEP 4 : 嘗試超額轉帳 ---
		try {
			System.out.println("---嘗試超額轉帳---");
			bankService.transfer(userA, userB, new BigDecimal("2000.0"));
		} catch (Exception e) {
			System.out.println("預期中的失敗：" + e.getMessage());
		}
		
		// --- STEP 5 : 最終結果驗證 ---
		System.out.println("--- 交易結束報告 ---");
		System.out.println("交易後A餘額為：" + userA.getBalance());
		System.out.println("交易後B餘額為：" + userB.getBalance());
		
		//[新增] 從資料庫『重新讀取』來驗證是否真的存進去了
		Account dbUserA = dao.getAccount("userA_001");
		if(dbUserA != null) {
			System.out.println("資料庫A餘額為：" + dbUserA.getBalance() + "（驗證成功！）");
		}
		
		userA.printHistory();
	}

}
