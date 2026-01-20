package com.finance.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public void saveTransaction(String accountId, Transaction tx) {
		String sql = "INSERT INTO transactions (account_id, type, amount, description, transaction_time) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, accountId);
			pstmt.setString(2, tx.getType());
			pstmt.setBigDecimal(3, tx.getAmount());
			pstmt.setString(4, tx.getDescription());
			pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(tx.getTimeStamp()));
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[Error]儲存資料發生錯誤" + e.getMessage());
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
	
	public List<Transaction> getTransactions(String accountId) {
		List<Transaction> list = new ArrayList<>();
		String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_time DESC";
		
		try(Connection conn = DatabaseManager.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, accountId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Transaction tx = new Transaction(
						rs.getString("type"),
						rs.getBigDecimal("amount"),
						rs.getString("description"));
				tx.setTimestamp(rs.getTimestamp("transaction_time").toLocalDateTime());
				list.add(tx);
			}
		} catch (SQLException e) {
			System.out.println("[ERROR] 取得資料庫transactions時發生錯誤" + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

}
