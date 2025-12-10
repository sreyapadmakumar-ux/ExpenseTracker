-- User Table SQL Commands

-- 1. Create User Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Insert User
INSERT INTO users (name, contact, email) 
VALUES (?, ?, ?);

-- 3. Find User by ID
SELECT * FROM users 
WHERE user_id = ?;

-- 4. Find User by Email
SELECT * FROM users 
WHERE email = ?;

-- 5. Update User
UPDATE users 
SET name = ?, contact = ?, email = ? 
WHERE user_id = ?;

-- 6. Delete User
DELETE FROM users 
WHERE user_id = ?;

-- 7. Get All Users
SELECT * FROM users 
ORDER BY created_at DESC;

-- 8. Check if Email Exists
SELECT COUNT(*) FROM users 
WHERE email = ?;

-- 9. Get User Summary
SELECT 
    u.user_id,
    u.name,
    u.email,
    COUNT(t.transaction_id) as total_transactions,
    SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as total_income,
    SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as total_expenses
FROM users u
LEFT JOIN transactions t ON u.user_id = t.user_id
WHERE u.user_id = ?
GROUP BY u.user_id, u.name, u.email;