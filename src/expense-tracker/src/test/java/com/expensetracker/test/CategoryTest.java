package com.expensetracker.test;

import com.expensetracker.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(1, "Groceries");
    }

    @Test
    public void testGetCategoryId() {
        assertEquals(1, category.getCategoryId());
    }

    @Test
    public void testGetCategoryName() {
        assertEquals("Groceries", category.getCategoryName());
    }

    @Test
    public void testSetCategoryName() {
        category.setCategoryName("Utilities");
        assertEquals("Utilities", category.getCategoryName());
    }

    @Override
    public String toString() {
        return "CategoryTest{" +
                "category=" + category +
                '}';
    }
}