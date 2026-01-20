package com.finance.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	private static final String URL = "jdbc:h2:./bankdb;DB_CLOSE_DELAY=-1";
	private static final String USER = "sa";
	private static final String PASSWORD = "";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static void initializeDatabase() {
		 
		/**
		 * 建立accounts表格
		 */
		String sqlAccounts = "CREATE TABLE IF NOT EXISTS accounts (" +
					"id VARCHAR(20) PRIMARY KEY, " +
					"balance DECIMAL(20,2))";
		String sqlTransactions = "CREATE TABLE IF NOT EXISTS transactions (" +
								"id INT AUTO_INCREMENT PRIMARY KEY, " +
								"account_id VARCHAR(20), " +
								"type VARCHAR(10), "+
								"amount DECIMAL(20,2), " +
								"description VARCHAR(100), "+
								"transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
		
		try (Connection conn = getConnection();
			Statement stmt = conn.createStatement()) {
			// stmt.execute("DROP TABLE IF EXISTS transactions");
			// stmt.execute("DROP TABLE IF EXISTS accounts");
			stmt.execute(sqlAccounts);
			stmt.execute(sqlTransactions);
			System.out.println("[DB] 初始化成功：accounts,transactions 表格已就緒");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
