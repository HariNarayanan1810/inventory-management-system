/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.inventorydashboard.model;

/**
 *
 * @author GEETHA
 */
import java.util.List;

public class Invoice {
    private final String customerName;
    private final List<Order> orders;

    public Invoice(String customerName, List<Order> orders) {
        this.customerName = customerName;
        this.orders = orders;
    }

    public String getCustomerName() { return customerName; }
    public List<Order> getOrders() { return orders; }
}
