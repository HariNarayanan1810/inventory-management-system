package com.example.dto;

public class ProductDTO {
    private Long id;
    private String imageUrl; // Matches SQL alias
    private String productName; // Matches SQL alias
    private String sku;
    private String supplierName; // Matches SQL alias
    private String category;
    private double productRating; // Matches SQL alias
    private double unitPrice; // Matches SQL alias
    private double sellingPrice; // Matches SQL alias
    private Long stockLevel; // Matches SQL alias
    private double marginPercent; // Matches SQL alias
    private boolean isArchived; // Matches SQL alias

    // Default constructor (essential for mapping)
    public ProductDTO() {
    }

    // Parameterized constructor - Match fields selected by native query
    public ProductDTO(Long id, String imageUrl, String productName, String sku, String supplierName, String category,
                      double productRating, double unitPrice, double sellingPrice, Long stockLevel, double marginPercent, boolean isArchived) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.sku = sku;
        this.supplierName = supplierName;
        this.category = category;
        this.productRating = productRating;
        this.unitPrice = unitPrice;
        this.sellingPrice = sellingPrice;
        this.stockLevel = stockLevel;
        this.marginPercent = marginPercent;
        this.isArchived = isArchived;
    }

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getProductRating() { return productRating; }
    public void setProductRating(double productRating) { this.productRating = productRating; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }

    public Long getStockLevel() { return stockLevel; }
    public void setStockLevel(Long stockLevel) { this.stockLevel = stockLevel; }

    public double getMarginPercent() { return marginPercent; }
    public void setMarginPercent(double marginPercent) { this.marginPercent = marginPercent; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { this.isArchived = archived; }
}