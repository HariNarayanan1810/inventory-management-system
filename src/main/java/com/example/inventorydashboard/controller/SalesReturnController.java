package com.example.inventorydashboard.controller;

import com.example.inventorydashboard.repository.SalesReturnRepository;
import com.example.inventorydashboard.model.SalesReturn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class SalesReturnController {

    @FXML
    private ComboBox<String> retailerComboBox;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TextArea reasonArea;

    @FXML
    private TableView<SalesReturn> returnsTable;

    @FXML
    private TableColumn<SalesReturn, Integer> colId;

    @FXML
    private TableColumn<SalesReturn, String> colRetailer;

    @FXML
    private TableColumn<SalesReturn, String> colProduct;

    @FXML
    private TableColumn<SalesReturn, Integer> colQuantity;

    @FXML
    private TableColumn<SalesReturn, String> colReason;

    @FXML
    private TableColumn<SalesReturn, LocalDate> colDate;

    private ObservableList<SalesReturn> salesReturnList = FXCollections.observableArrayList();

    private final SalesReturnRepository repository = new SalesReturnRepository();

    @FXML
    public void initialize() {
        // Bind model properties to table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetailer.setCellValueFactory(new PropertyValueFactory<>("retailer"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Load data from database
        salesReturnList.addAll(repository.findAll());
        returnsTable.setItems(salesReturnList);

        retailerComboBox.getItems().setAll(repository.findRetailerNames());
        productComboBox.getItems().setAll(repository.findProductNames());
    }

    @FXML
    private void handleSubmitReturn() {
        try {
            String retailer = retailerComboBox.getValue();
            String product = productComboBox.getValue();
            String quantityText = quantityField.getText().trim();
            String reason = reasonArea.getText().trim();

            if (retailer == null || product == null || quantityText.isEmpty() || reason.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
                return;
            }

            int quantity = Integer.parseInt(quantityText);

            // Create a new return with dummy ID (real ID comes from DB)
            SalesReturn newReturn = new SalesReturn(0, retailer, product, quantity, reason, LocalDate.now());

            if (repository.save(newReturn)) {
                salesReturnList.setAll(repository.findAll());
                retailerComboBox.setValue(null);
                productComboBox.setValue(null);
                quantityField.clear();
                reasonArea.clear();

                showAlert(Alert.AlertType.INFORMATION, "Sales return submitted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Sales return was not saved: " + repository.getLastError());
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Quantity must be a valid number.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error while saving to database: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
