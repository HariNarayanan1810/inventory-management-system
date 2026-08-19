package com.example.inventorydashboard.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane mainContentPane;

    @FXML
    public void initialize() {
        loadDashboard(); // Load dashboard by default
    }

    @FXML
    public void loadDashboard() {
        loadPage("/com/example/inventorydashboard/dashboard.fxml");
    }

    @FXML
    public void loadSalesReturn() {
        loadPage("/com/example/inventorydashboard/salesreturn.fxml");
    }

    @FXML
    public void loadInventory() {
        loadPage("/com/example/inventorydashboard/inventory.fxml");
    }

    @FXML
    public void loadPurchaseReturn() {
        loadPage("/com/example/inventorydashboard/purchase_return.fxml");
    }
    
    @FXML
    public void handleSalesPage() {
    loadPage("/com/example/inventorydashboard/SalesPage.fxml");
    }

    public void goToPurchasePage() {
    loadPage("/com/example/inventorydashboard/purchase_page.fxml"); 
    }

    public void goToSupplierPage() {
    loadPage("/com/example/inventorydashboard/supplier_page.fxml");
    }



    private void loadPage(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            mainContentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
