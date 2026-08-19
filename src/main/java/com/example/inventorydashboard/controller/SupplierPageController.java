package com.example.inventorydashboard.controller;

import com.example.inventorydashboard.model.Supplier;
import com.example.inventorydashboard.service.SupplierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SupplierPageController {

    @FXML private TableView<Supplier> supplierTable;
    @FXML private TableColumn<Supplier, String> nameCol;
    @FXML private TableColumn<Supplier, String> contactNameCol;
    @FXML private TableColumn<Supplier, String> emailCol;
    @FXML private TableColumn<Supplier, String> addressCol;
    @FXML private TextField nameField;
    @FXML private TextField contactNameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    private final SupplierService supplierService = new SupplierService();
    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        contactNameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getContactName()));
        emailCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
        addressCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAddress()));

        loadSuppliers();

        supplierTable.setOnMouseClicked(e -> {
            Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.getName());
                contactNameField.setText(selected.getContactName());
                emailField.setText(selected.getEmail());
                addressField.setText(selected.getAddress());
            }
        });
    }

    private void loadSuppliers() {
        supplierList.setAll(supplierService.getAllSuppliers());
        supplierTable.setItems(supplierList);
    }

    @FXML
    public void addSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName(nameField.getText());
        supplier.setContactName(contactNameField.getText());
        supplier.setEmail(emailField.getText());
        supplier.setAddress(addressField.getText());

        if (supplierService.addSupplier(supplier)) {
            loadSuppliers();
            clearForm();
        }
    }

    @FXML
    public void updateSupplier() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName(nameField.getText());
            selected.setContactName(contactNameField.getText());
            selected.setEmail(emailField.getText());
            selected.setAddress(addressField.getText());

            if (supplierService.updateSupplier(selected)) {
                loadSuppliers();
                clearForm();
            }
        }
    }

    @FXML
    public void deleteSupplier() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected != null && supplierService.deleteSupplier(selected.getSupplierId())) {
            loadSuppliers();
            clearForm();
        }
    }

    private void clearForm() {
        nameField.clear();
        contactNameField.clear();
        emailField.clear();
        addressField.clear();
        supplierTable.getSelectionModel().clearSelection();
    }
}
