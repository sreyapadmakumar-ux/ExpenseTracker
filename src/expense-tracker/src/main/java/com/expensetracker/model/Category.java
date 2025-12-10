public class Category {
    private int categoryId;
    private String name;

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return name;
    }

    public void setCategoryName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}