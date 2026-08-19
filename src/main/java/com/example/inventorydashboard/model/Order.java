/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.enterpriseproject.model;
package com.example.inventorydashboard.model;

/**
 *
 * @author GEETHA
 */


import javafx.beans.property.*;

public class Order {
    private final Product product;
    private final IntegerProperty quantity;

    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }
}
