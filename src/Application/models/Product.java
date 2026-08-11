package Application.models;

public class Product {
    private int id;
    private String productName;
    private String category;
    private String unit;
    private double purchasePrice;
    private double sellingPrice;
    private int stockQuantity;
    private int reorderLevel;
    private String description;

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product(String productName, String category, String unit, double purchasePrice, double sellingPrice, int stockQuantity, int reorderLevel, String description) {
        this.productName = productName;
        this.category = category;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.description = description;
    }

    public Product(
            int id,
            String productName,
            String category,
            String unit,
            double purchasePrice,
            double sellingPrice,
            int stockQuantity,
            int reorderLevel,
            String description
    ) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.description = description;
    }
}
