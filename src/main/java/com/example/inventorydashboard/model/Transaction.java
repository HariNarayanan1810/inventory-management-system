package com.example.inventorydashboard.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty type;
    private final SimpleStringProperty date;
    private final SimpleDoubleProperty amount;

    public Transaction(int id, String type, String date, double amount) {
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(date);
        this.amount = new SimpleDoubleProperty(amount);
    }

    public int getId() {
        return id.get();
    }
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getType() {
        return type.get();
    }
    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getDate() {
        return date.get();
    }
    public SimpleStringProperty dateProperty() {
        return date;
    }

    public double getAmount() {
        return amount.get();
    }
    public SimpleDoubleProperty amountProperty() {
        return amount;
    }
}
