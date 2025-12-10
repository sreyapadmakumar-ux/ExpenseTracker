-- Category Table SQL Commands

-- 1. Create Category Table
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Insert Default Categories
INSERT INTO categories (name) VALUES 
('Salary'),
('Groceries'),
('Transport'),
('Utilities'),
('Entertainment'),
('Healthcare'),
('Education'),
('Rent'),
('Investment'),
('Miscellaneous');

-- 3. Insert New Category
INSERT INTO categories (name) 
VALUES (?);

-- 4. Find Category by ID
SELECT * FROM categories 
WHERE category_id = ?;

-- 5. Find Category by Name
SELECT * FROM categories 
WHERE name = ?;

-- 6. Update Category
UPDATE categories 
SET name = ? 
WHERE category_id = ?;

-- 7. Delete Category (with safety check)
DELETE FROM categories 
WHERE category_id = ? 
AND NOT EXISTS (
    SELECT 1 FROM transactions 
    WHERE category_id = ?
);

-- 8. Get All Categories
SELECT * FROM categories 
ORDER BY name;

-- 9. Get Category Usage Count
SELECT 
    c.category_id,
    c.name,
    COUNT(t.transaction_id) as usage_count,
    SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as total_expenses,
    SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as total_income
FROM categories c
LEFT JOIN transactions t ON c.category_id = t.category_id
GROUP BY c.category_id, c.name
ORDER BY usage_count DESC;

-- 10. Get User's Most Used Categories
SELECT 
    c.category_id,
    c.name,
    COUNT(*) as usage_count,
    SUM(t.amount) as total_amount
FROM categories c
JOIN transactions t ON c.category_id = t.category_id
WHERE t.user_id = ?
GROUP BY c.category_id, c.name
ORDER BY usage_count DESC
LIMIT 5;