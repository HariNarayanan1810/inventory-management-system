package com.example.inventorydashboard.controller;

import  com.example.inventorydashboard.model.Purchase;
import com.example.inventorydashboard.service.PurchaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PurchasePageController {

    @FXML private TableView<Purchase> purchaseTable;
    @FXML private TableColumn<Purchase, Number> idCol;
    @FXML private TableColumn<Purchase, String> paymentCol;
    @FXML private TableColumn<Purchase, String> deliveryCol;

    @FXML private TextField supplierIdField;
    @FXML private TextField warehouseIdField;
    @FXML private ComboBox<String> paymentStatusBox;
    @FXML private ComboBox<String> deliveryStatusBox;

    private final PurchaseService purchaseService = new PurchaseService();
    private final ObservableList<Purchase> purchases = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPurchaseId()));
        paymentCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPaymentStatus()));
        deliveryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDeliveryStatus()));

        paymentStatusBox.getItems().addAll("paid", "unpaid");
        deliveryStatusBox.getItems().addAll("dispatched", "on the way", "delivered");

        loadPurchases();

        purchaseTable.setOnMouseClicked(e -> {
            Purchase selected = purchaseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                supplierIdField.setText(String.valueOf(selected.getSupplierId()));
                warehouseIdField.setText(String.valueOf(selected.getWarehouseId()));
                paymentStatusBox.setValue(selected.getPaymentStatus());
                deliveryStatusBox.setValue(selected.getDeliveryStatus());
            }
        });
    }

    private void loadPurchases() {
        purchases.setAll(purchaseService.getAllPurchaseOrders());
        purchaseTable.setItems(purchases);
    }

    @FXML
    public void addPurchase() {
        Purchase purchase = new Purchase();
        purchase.setSupplierId(Integer.parseInt(supplierIdField.getText()));
        purchase.setWarehouseId(Integer.parseInt(warehouseIdField.getText()));
        purchase.setPaymentStatus(paymentStatusBox.getValue());
        purchase.setDeliveryStatus(deliveryStatusBox.getValue());

        if (purchaseService.addPurchase(purchase)) {
            loadPurchases();
            clearForm();
        }
    }

    @FXML
    public void updatePurchase() {
        Purchase selected = purchaseTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setSupplierId(Integer.parseInt(supplierIdField.getText()));
            selected.setWarehouseId(Integer.parseInt(warehouseIdField.getText()));
            selected.setPaymentStatus(paymentStatusBox.getValue());
            selected.setDeliveryStatus(deliveryStatusBox.getValue());

            if (purchaseService.updatePurchase(selected)) {
                loadPurchases();
                clearForm();
            }
        }
    }

    @FXML
    public void deletePurchase() {
        Purchase selected = purchaseTable.getSelectionModel().getSelectedItem();
        if (selected != null && purchaseService.deletePurchase(selected.getPurchaseId())) {
            loadPurchases();
            clearForm();
        }
    }

    private void clearForm() {
        supplierIdField.clear();
        warehouseIdField.clear();
        paymentStatusBox.setValue(null);
        deliveryStatusBox.setValue(null);
        purchaseTable.getSelectionModel().clearSelection();
    }
}
