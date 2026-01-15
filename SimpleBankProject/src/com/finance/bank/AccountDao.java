package com.finance.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {

	/**
	 * 將Account物件持久化到資料庫
	 * @param account
	 */
	public void saveAccount(Account account) {
		String sql = "MERGE INTO accounts (id, balance) KEY(id) VALUES(?, ?)";
		try (Connection conn = DatabaseManager.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, account.getAccountNumber());
			pstmt.setBigDecimal(2, account.getBalance());
			pstmt.executeUpdate();
			
			System.out.println("[DB] 系統已同步存入" + account.getAccountNumber());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Account getAccount(String id) {
		String sql = "SELECT id, balance FROM accounts WHERE id = ?";
		try (Connection conn = DatabaseManager.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//從資料庫抓出餘額，並建立一個新的Account物件回傳
				return new Account(rs.getString("id"),rs.getBigDecimal("balance").toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
