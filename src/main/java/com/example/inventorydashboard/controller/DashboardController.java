package com.example.inventorydashboard.controller;

import com.example.inventorydashboard.model.DashboardCard;
import com.example.inventorydashboard.service.DashboardService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DashboardController {

    @FXML private GridPane cardGrid;
    @FXML private LineChart<Number, Number> salesChart;

    private final DashboardService dashboardService = new DashboardService();

    @FXML
    public void initialize() {
        setupCards();
        loadSalesChart();
    }

    private void setupCards() {
        Map<String, String> metrics = dashboardService.getMetrics();
        List<DashboardCard> cards = Arrays.asList(
                new DashboardCard("Total Stock", metrics.get("Total Stock"), "Stock", ""),
                new DashboardCard("Total Sales (This Month)", metrics.get("Total Sales (This Month)"), "Sales", ""),
                new DashboardCard("Total Purchase (This Month)", metrics.get("Total Purchase (This Month)"), "Purchase", ""),
                new DashboardCard("Profit/Loss", metrics.get("Profit/Loss"), "Profit", "up"),
                new DashboardCard("Low Stock Alerts", metrics.get("Low Stock Alerts"), "Alert", ""),
                new DashboardCard("Top-Selling Product", metrics.get("Top-Selling Product"), "Top", ""),
                new DashboardCard("Returns Summary", metrics.get("Returns Summary"), "Returns", ""),
                new DashboardCard("Active Suppliers", metrics.get("Active Suppliers"), "Suppliers", ""),
                new DashboardCard("Pending Retailer Orders", metrics.get("Pending Retailer Orders"), "Pending", ""),
                new DashboardCard("Total Inventory Value", metrics.get("Total Inventory Value"), "Value", "")
        );

        int col = 0;
        int row = 0;
        for (DashboardCard dashboardCard : cards) {
            cardGrid.add(createCard(dashboardCard), col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
    }

    private StackPane createCard(DashboardCard data) {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        box.setPrefSize(200, 100);
        box.setAlignment(javafx.geometry.Pos.CENTER);

        Label iconLabel = new Label(data.getIcon());
        iconLabel.setFont(new Font(16));

        Label title = new Label(data.getTitle());
        title.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        Label value = new Label(data.getValue());
        value.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");

        box.getChildren().addAll(iconLabel, title, value);
        return new StackPane(box);
    }

    private void loadSalesChart() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().addAll(
                new XYChart.Data<>(1, 120),
                new XYChart.Data<>(2, 145),
                new XYChart.Data<>(3, 110),
                new XYChart.Data<>(4, 180),
                new XYChart.Data<>(5, 130),
                new XYChart.Data<>(6, 160),
                new XYChart.Data<>(7, 155)
        );
        salesChart.getData().add(series);
    }
}
