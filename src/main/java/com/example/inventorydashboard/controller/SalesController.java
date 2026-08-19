/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.inventorydashboard.controller;

/**
 *
 * @author GEETHA
 */

import com.example.inventorydashboard.service.SalesService;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.file.Path;

public class SalesController {

    @FXML private TextField customerNameField;
    @FXML private TextField warehouseIdField;
    @FXML private TextField productIdField;
    @FXML private TextField quantityField;
    @FXML private Label stockStatusLabel;

    private final SalesService salesService = new SalesService();

    @FXML
    private void onCheckStock() {
        try {
            int productId = Integer.parseInt(productIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            boolean inStock = salesService.checkStock(productId, quantity);
            stockStatusLabel.setText(inStock ? "In Stock" : "Not Enough Stock");
        } catch (NumberFormatException e) {
            stockStatusLabel.setText("Invalid input");
        }
    }

    @FXML
    private void onSaveOrder() {
        String customerName = customerNameField.getText();
        try {
            int productId = Integer.parseInt(productIdField.getText());
            int warehouseId = Integer.parseInt(warehouseIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            if (salesService.saveOrder(customerName, productId, warehouseId, quantity)) {
                stockStatusLabel.setText("Order Saved");
            } else {
                stockStatusLabel.setText("Order not saved: " + salesService.getLastError());
            }
        } catch (NumberFormatException e) {
            stockStatusLabel.setText("Invalid input");
        }
    }

    @FXML
    private void onGenerateInvoice() {
        String customerName = customerNameField.getText();
        String safeName = customerName == null || customerName.isBlank()
                ? "customer"
                : customerName.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        String filePath = "invoices/invoice_" + safeName + ".pdf";

        Path savedPath = salesService.generateInvoicePDF(customerName, filePath);
        if (savedPath != null) {
            stockStatusLabel.setText("Invoice PDF Saved: " + savedPath);
        } else {
            stockStatusLabel.setText("Invoice not saved: " + salesService.getLastError());
        }
    }
}
