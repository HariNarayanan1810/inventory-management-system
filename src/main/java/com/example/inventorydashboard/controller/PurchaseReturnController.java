package com.example.inventorydashboard.controller;

import com.example.inventorydashboard.model.PurchaseReturn;
import com.example.inventorydashboard.repository.PurchaseReturnRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class PurchaseReturnController {

    @FXML private TableView<PurchaseReturn> returnsTable;
    @FXML private TableColumn<PurchaseReturn, Integer> idCol;
    @FXML private TableColumn<PurchaseReturn, Integer> purchaseIdCol;
    @FXML private TableColumn<PurchaseReturn, Integer> productIdCol;
    @FXML private TableColumn<PurchaseReturn, Integer> quantityCol;
    @FXML private TableColumn<PurchaseReturn, String> reasonCol;
    @FXML private TableColumn<PurchaseReturn, String> statusCol;
    @FXML private TableColumn<PurchaseReturn, LocalDate> dateCol;

    @FXML private TextField purchaseIdField;
    @FXML private TextField productIdField;
    @FXML private TextField quantityField;
    @FXML private TextArea reasonArea;
    @FXML private ComboBox<String> statusBox;

    private final PurchaseReturnRepository repository = new PurchaseReturnRepository();
    private final ObservableList<PurchaseReturn> returns = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchaseIdCol.setCellValueFactory(new PropertyValueFactory<>("purchaseId"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusBox.getItems().addAll("pending", "processed", "rejected");
        statusBox.setValue("pending");
        loadReturns();
    }

    @FXML
    public void submitReturn() {
        try {
            PurchaseReturn purchaseReturn = new PurchaseReturn();
            purchaseReturn.setPurchaseId(Integer.parseInt(purchaseIdField.getText().trim()));
            purchaseReturn.setProductId(Integer.parseInt(productIdField.getText().trim()));
            purchaseReturn.setQuantity(Integer.parseInt(quantityField.getText().trim()));
            purchaseReturn.setReason(reasonArea.getText().trim());
            purchaseReturn.setStatus(statusBox.getValue());
            purchaseReturn.setDate(LocalDate.now());

            if (purchaseReturn.getReason().isBlank()) {
                showAlert(Alert.AlertType.WARNING, "Reason is required.");
                return;
            }

            if (repository.save(purchaseReturn)) {
                loadReturns();
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Purchase return submitted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Purchase return was not saved: " + repository.getLastError());
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Purchase ID, Product ID, and quantity must be valid numbers.");
        }
    }

    @FXML
    public void clearForm() {
        purchaseIdField.clear();
        productIdField.clear();
        quantityField.clear();
        reasonArea.clear();
        statusBox.setValue("pending");
    }

    private void loadReturns() {
        returns.setAll(repository.findAll());
        returnsTable.setItems(returns);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
