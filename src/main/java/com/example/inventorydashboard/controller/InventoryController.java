package com.example.inventorydashboard.controller;

import com.example.inventorydashboard.model.Product;
import com.example.inventorydashboard.service.InventoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, String> skuCol;
    @FXML private TableColumn<Product, String> categoryCol;
    @FXML private TableColumn<Product, Integer> supplierCol;
    @FXML private TableColumn<Product, Double> unitPriceCol;
    @FXML private TableColumn<Product, Double> sellingPriceCol;
    @FXML private TableColumn<Product, Integer> stockCol;
    @FXML private TableColumn<Product, Integer> reorderCol;
    @FXML private TableColumn<Product, String> statusCol;

    @FXML private TextField nameField;
    @FXML private TextField skuField;
    @FXML private TextField categoryField;
    @FXML private TextField supplierIdField;
    @FXML private TextField unitPriceField;
    @FXML private TextField sellingPriceField;
    @FXML private TextField stockField;
    @FXML private TextField reorderPointField;

    private final InventoryService inventoryService = new InventoryService();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        skuCol.setCellValueFactory(new PropertyValueFactory<>("sku"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        sellingPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        reorderCol.setCellValueFactory(new PropertyValueFactory<>("reorderPoint"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                fillForm(selected);
            }
        });

        loadProducts();
    }

    @FXML
    public void addProduct() {
        Product product = readForm();
        if (product != null && inventoryService.addProduct(product)) {
            loadProducts();
            clearForm();
        }
    }

    @FXML
    public void updateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select a product to update.");
            return;
        }

        Product product = readForm();
        if (product != null) {
            product.setId(selected.getId());
            if (inventoryService.updateProduct(product)) {
                loadProducts();
                clearForm();
            }
        }
    }

    @FXML
    public void archiveProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select a product to archive.");
            return;
        }

        if (inventoryService.archiveProduct(selected.getId())) {
            loadProducts();
            clearForm();
        }
    }

    @FXML
    public void clearForm() {
        nameField.clear();
        skuField.clear();
        categoryField.clear();
        supplierIdField.clear();
        unitPriceField.clear();
        sellingPriceField.clear();
        stockField.clear();
        reorderPointField.clear();
        productTable.getSelectionModel().clearSelection();
    }

    private void loadProducts() {
        products.setAll(inventoryService.getAllProducts());
        productTable.setItems(products);
    }

    private void fillForm(Product product) {
        nameField.setText(product.getName());
        skuField.setText(product.getSku());
        categoryField.setText(product.getCategory());
        supplierIdField.setText(String.valueOf(product.getSupplierId()));
        unitPriceField.setText(String.valueOf(product.getUnitPrice()));
        sellingPriceField.setText(String.valueOf(product.getSellingPrice()));
        stockField.setText(String.valueOf(product.getStock()));
        reorderPointField.setText(String.valueOf(product.getReorderPoint()));
    }

    private Product readForm() {
        try {
            Product product = new Product();
            product.setName(nameField.getText().trim());
            product.setSku(skuField.getText().trim());
            product.setCategory(categoryField.getText().trim());
            product.setSupplierId(Integer.parseInt(supplierIdField.getText().trim()));
            product.setUnitPrice(Double.parseDouble(unitPriceField.getText().trim()));
            product.setSellingPrice(Double.parseDouble(sellingPriceField.getText().trim()));
            product.setStock(Integer.parseInt(stockField.getText().trim()));
            product.setReorderPoint(Integer.parseInt(reorderPointField.getText().trim()));

            if (product.getName().isBlank() || product.getSku().isBlank() || product.getCategory().isBlank()) {
                showAlert(Alert.AlertType.WARNING, "Name, SKU, and category are required.");
                return null;
            }
            return product;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Supplier, price, stock, and reorder point must be valid numbers.");
            return null;
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
