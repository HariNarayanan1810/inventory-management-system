package com.example.dto;

import java.util.List;

public class FilterDTO {
    private List<FilterItem> warehouses;
    private List<FilterItem> suppliers;
    private List<FilterItem> categories;

    // Getters and setters
    public List<FilterItem> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<FilterItem> warehouses) {
        this.warehouses = warehouses;
    }

    public List<FilterItem> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<FilterItem> suppliers) {
        this.suppliers = suppliers;
    }

    public List<FilterItem> getCategories() {
        return categories;
    }

    public void setCategories(List<FilterItem> categories) {
        this.categories = categories;
    }

    // Inner static class
    public static class FilterItem {
        private Long id;
        private String name;

        // Constructors
        public FilterItem() {}

        public FilterItem(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
