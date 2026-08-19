
package com.example.model;


import jakarta.persistence.*;

@Entity
@Table(name = "category_margins")
public class Category {

    @Id
    @Column(name = "category_id", nullable = false)
    private Long categoryId; 
    
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "margin_percent", nullable = false)
    private Double marginPercent;

    // Getters and Setters
    public Long getcategoryId() {
        return categoryId;
    }

    public void setcategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getMarginPercent() {
        return marginPercent;
    }

    public void setMarginPercent(Double marginPercent) {
        this.marginPercent = marginPercent;
    }
}
