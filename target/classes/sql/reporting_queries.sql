-- Reporting and Analytics SQL Commands

-- Parameters: set these before running or edit values as needed
SET @user_id := 1;
SET @start := '2023-09-01';
SET @end := '2023-09-30';

-- 1. Monthly Income vs Expense Report
SELECT 
    YEAR(date) as year,
    MONTH(date) as month,
    SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as total_income,
    SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as total_expense,
    SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) as net_savings
FROM transactions
WHERE user_id = @user_id
GROUP BY YEAR(date), MONTH(date)
ORDER BY year DESC, month DESC;

-- 2. Category-wise Spending Analysis
SELECT 
    c.name as category,
    COUNT(*) as transaction_count,
    SUM(t.amount) as total_amount,
    AVG(t.amount) as average_amount,
    MIN(t.amount) as min_amount,
    MAX(t.amount) as max_amount
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = @user_id 
AND t.type = 'EXPENSE'
AND t.date BETWEEN @start AND @end
GROUP BY c.name
ORDER BY total_amount DESC;

-- 3. Daily Spending Pattern
SELECT 
    DAYNAME(date) as day_of_week,
    COUNT(*) as transaction_count,
    AVG(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as avg_expense
FROM transactions
WHERE user_id = @user_id
GROUP BY DAYNAME(date)
ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday');

-- 4. Monthly Budget Analysis
WITH monthly_totals AS (
    SELECT 
        YEAR(date) as year,
        MONTH(date) as month,
        SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as total_expense
    FROM transactions
    WHERE user_id = @user_id
    GROUP BY YEAR(date), MONTH(date)
)
SELECT 
    year,
    month,
    total_expense,
    AVG(total_expense) OVER (ORDER BY year, month ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) as three_month_avg,
    total_expense - LAG(total_expense, 1) OVER (ORDER BY year, month) as month_over_month_change
FROM monthly_totals
ORDER BY year DESC, month DESC;

-- 5. Income Source Analysis
SELECT 
    c.name as income_source,
    COUNT(*) as frequency,
    SUM(t.amount) as total_income,
    AVG(t.amount) as average_income
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = @user_id 
AND t.type = 'INCOME'
GROUP BY c.name
ORDER BY total_income DESC;

-- 6. Expense Trend Analysis
SELECT 
    DATE_FORMAT(date, '%Y-%m') as month,
    c.name as category,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount,
    SUM(amount) / COUNT(*) as average_amount
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = @user_id 
AND t.type = 'EXPENSE'
AND t.date >= DATE_SUB(CURRENT_DATE, INTERVAL 6 MONTH)
GROUP BY DATE_FORMAT(date, '%Y-%m'), c.name
ORDER BY month DESC, total_amount DESC;

-- 7. Savings Rate Calculation
SELECT 
    YEAR(date) as year,
    MONTH(date) as month,
    SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as total_income,
    SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as total_expense,
    SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) as net_savings,
    (SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) / 
     SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) * 100) as savings_rate
FROM transactions
WHERE user_id = @user_id
GROUP BY YEAR(date), MONTH(date)
HAVING total_income > 0
ORDER BY year DESC, month DESC;

-- 8. Large Transaction Analysis
SELECT 
    t.*,
    c.name as category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = @user_id
AND t.amount > (
    SELECT AVG(amount) + (2 * STDDEV(amount))
    FROM transactions
    WHERE user_id = @user_id AND type = t.type
)
ORDER BY t.amount DESC;