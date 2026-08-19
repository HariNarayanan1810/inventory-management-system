package com.example.inventorydashboard.model;

import java.time.LocalDate;

public class SalesReturn {
    private int id;
    private String retailer;
    private String product;
    private int quantity;
    private String reason;
    private LocalDate date;

    public SalesReturn(int id, String retailer, String product, int quantity, String reason, LocalDate date) {
        this.id = id;
        this.retailer = retailer;
        this.product = product;
        this.quantity = quantity;
        this.reason = reason;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getRetailer() {
        return retailer;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getDate() {
        return date;
    }
}
