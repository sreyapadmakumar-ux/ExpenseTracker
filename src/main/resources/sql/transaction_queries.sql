-- Transaction Table SQL Commands

-- 1. Create Transaction Table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT
);

-- 2. Create Indexes for Better Performance
CREATE INDEX idx_transactions_user ON transactions(user_id);
CREATE INDEX idx_transactions_category ON transactions(category_id);
CREATE INDEX idx_transactions_date ON transactions(date);
CREATE INDEX idx_transactions_type ON transactions(type);

-- 3. Insert Transaction
INSERT INTO transactions (user_id, category_id, name, type, amount, date, description) 
VALUES (?, ?, ?, ?, ?, ?, ?);

-- 4. Get User's Transactions
SELECT 
    t.*,
    c.name as category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
ORDER BY t.date DESC;

-- 5. Get Transactions by Date Range
SELECT 
    t.*,
    c.name as category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ? 
AND t.date BETWEEN ? AND ?
ORDER BY t.date DESC;

-- 6. Get Transactions by Category
SELECT 
    t.*,
    c.name as category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ? 
AND t.category_id = ?
ORDER BY t.date DESC;

-- 7. Get User's Balance
SELECT 
    COALESCE(SUM(
        CASE 
            WHEN type = 'INCOME' THEN amount 
            WHEN type = 'EXPENSE' THEN -amount 
        END
    ), 0) as balance
FROM transactions
WHERE user_id = ?;

-- 8. Delete Transaction
DELETE FROM transactions 
WHERE transaction_id = ? AND user_id = ?;

-- 9. Get Monthly Summary
SELECT 
    YEAR(date) as year,
    MONTH(date) as month,
    type,
    SUM(amount) as total_amount,
    COUNT(*) as transaction_count
FROM transactions
WHERE user_id = ?
GROUP BY YEAR(date), MONTH(date), type
ORDER BY year DESC, month DESC;

-- 10. Get Category-wise Summary
SELECT 
    c.name as category,
    t.type,
    SUM(t.amount) as total_amount,
    COUNT(*) as transaction_count
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
AND t.date BETWEEN ? AND ?
GROUP BY c.name, t.type
ORDER BY total_amount DESC;

-- 11. Get Recent Transactions
SELECT 
    t.*,
    c.name as category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
ORDER BY t.created_at DESC
LIMIT 10;

-- 12. Search Transactions
SELECT 
    t.*,
    c.name as category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
AND (
    t.name LIKE CONCAT('%', ?, '%')
    OR t.description LIKE CONCAT('%', ?, '%')
    OR c.name LIKE CONCAT('%', ?, '%')
)
ORDER BY t.date DESC;