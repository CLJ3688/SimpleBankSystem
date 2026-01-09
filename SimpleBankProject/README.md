# Simple Bank System (Java 實作模擬銀行帳務系統) 
這是一個展示 Java 後端核心技術的作品，模擬銀行轉帳、餘額管理與交易追蹤功能。 

## 技術亮點
 - **金融級運算**：使用 `BigDecimal` 處理帳戶餘額，避免傳統浮點數 (double/float) 的捨入誤差。
 - **異常處理機制**：自定義 `InsufficientBalanceException`，精確捕捉並處理業務邏輯錯誤。
 - **事務一致性**：透過 `BankService` 統一管理轉帳邏輯，確保資產轉移的原子性。
 - **自動化審計紀錄**：實作 `Transaction` 類別，自動追蹤並儲存所有歷史交易明細。

##  功能展示
 - 帳戶存款與提款
 - 多個帳戶間的資金轉移
 - 餘額不足防錯處理 - 查詢歷史明細 (Transaction History)