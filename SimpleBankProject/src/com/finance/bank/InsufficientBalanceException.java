package com.finance.bank;

/**
 * 自定義異常：餘額不足
 * 繼承RuntimeException extends RuntimeException程式碼簡潔
 */

public class InsufficientBalanceException extends RuntimeException{
	public InsufficientBalanceException(String message) {
		super(message);
	}
}
