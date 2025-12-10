shpackage com.expensetracker.dao;

import com.expensetracker.model.Category;
import com.expensetracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {
    // SQL Queries
    private static final String INSERT_CATEGORY = 
        "INSERT INTO categories (name) VALUES (?)";
    
    private static final String FIND_BY_ID = 
        "SELECT * FROM categories WHERE category_id = ?";
    
    private static final String FIND_BY_NAME = 
        "SELECT * FROM categories WHERE name = ?";
    
    private static final String GET_ALL_CATEGORIES = 
        "SELECT * FROM categories ORDER BY name";
    
    private static final String UPDATE_CATEGORY = 
        "UPDATE categories SET name = ? WHERE category_id = ?";
    
    private static final String DELETE_CATEGORY = 
        "DELETE FROM categories WHERE category_id = ?";
    
    private static final String CHECK_CATEGORY_USAGE = 
        "SELECT COUNT(*) FROM transactions WHERE category_id = ?";
    
    private static final String GET_CATEGORY_STATS = 
        "SELECT c.category_id, c.name, " +
        "COUNT(t.transaction_id) as usage_count, " +
        "SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as total_expenses, " +
        "SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as total_income " +
        "FROM categories c " +
        "LEFT JOIN transactions t ON c.category_id = t.category_id " +
        "WHERE c.category_id = ? " +
        "GROUP BY c.category_id, c.name";

    public CategoryDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO categories (category_id, name) VALUES (?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, category.getCategoryId());
            pstmt.setString(2, category.getCategoryName());
            
            pstmt.executeUpdate();
        }
    }

    public Optional<Category> findById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name")
                    );
                    return Optional.of(category);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Category> findByName(String categoryName) throws SQLException {
        String sql = "SELECT * FROM categories WHERE name = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, categoryName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name")
                    );
                    return Optional.of(category);
                }
            }
        }
        return Optional.empty();
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Category category = new Category(
                    rs.getInt("category_id"),
                    rs.getString("name")
                );
                categories.add(category);
            }
        }
        return categories;
    }

    public void updateCategory(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ? WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getCategoryName());
            pstmt.setInt(2, category.getCategoryId());
            
            pstmt.executeUpdate();
        }
    }

    public boolean deleteCategory(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}