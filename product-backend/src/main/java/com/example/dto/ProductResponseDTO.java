

package com.example.dto;

import java.util.List;

public class ProductResponseDTO {
    private List<ProductDTO> products;
    private Long totalCount;

    // Getters and Setters
    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
