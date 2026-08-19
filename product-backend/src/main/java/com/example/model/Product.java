

package com.example.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private Supplier supplier;

    @Column(name = "product_rating")
    private Double productRating;

    @Column(name = "reorder_point", nullable = false)
    private Integer reorderPoint;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "default_selling_price")
    private Double defaultSellingPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_archived", nullable = false)
    private boolean isArchived = false;

    public Product() {
    }

    // Add a constructor for creating new products (excluding generated fields like ID, timestamps)
    public Product(String name, String description, String sku, String category, Double unitPrice, Supplier supplier, Double productRating, Integer reorderPoint, Double defaultSellingPrice, String imageUrl) {
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.category = category;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.productRating = productRating;
        this.reorderPoint = reorderPoint;
        this.defaultSellingPrice = defaultSellingPrice;
        this.imageUrl = imageUrl;
        // Timestamps are often handled automatically or in a @PrePersist/@PreUpdate listener
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isArchived = false; // Default value
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Double getProductRating() {
        return productRating;
    }

    public void setProductRating(Double productRating) {
        this.productRating = productRating;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getDefaultSellingPrice() {
        return defaultSellingPrice;
    }

    public void setDefaultSellingPrice(Double defaultSellingPrice) {
        this.defaultSellingPrice = defaultSellingPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

     public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
