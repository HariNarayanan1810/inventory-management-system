package com.example.inventorydashboard.service;

import com.example.inventorydashboard.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardService {

    public Map<String, String> getMetrics() {
        Map<String, String> metrics = new LinkedHashMap<>();
        metrics.put("Total Stock", String.valueOf(readLong("SELECT COALESCE(SUM(quantity), 0) FROM product_stock")));
        metrics.put("Total Sales (This Month)", formatCurrency(readDouble(
                "SELECT COALESCE(SUM(s.quantity * p.default_selling_price), 0) " +
                        "FROM sales s JOIN products p ON s.product_id = p.product_id " +
                        "WHERE date_trunc('month', s.created_at) = date_trunc('month', CURRENT_DATE)")));
        metrics.put("Total Purchase (This Month)", formatCurrency(readDouble(
                "SELECT COALESCE(SUM(total_amount), 0) FROM purchase_orders " +
                        "WHERE date_trunc('month', purchase_date) = date_trunc('month', CURRENT_DATE)")));
        metrics.put("Profit/Loss", formatCurrency(readDouble(
                "SELECT COALESCE(SUM(s.quantity * (p.default_selling_price - p.unit_price)), 0) " +
                        "FROM sales s JOIN products p ON s.product_id = p.product_id " +
                        "WHERE date_trunc('month', s.created_at) = date_trunc('month', CURRENT_DATE)")));
        metrics.put("Low Stock Alerts", readLong("SELECT COUNT(*) FROM product_stock WHERE quantity <= low_stock_threshold") + " Items");
        metrics.put("Top-Selling Product", readText(
                "SELECT p.name FROM sales s JOIN products p ON s.product_id = p.product_id " +
                        "GROUP BY p.name ORDER BY SUM(s.quantity) DESC LIMIT 1", "N/A"));
        metrics.put("Returns Summary", readLong("SELECT COUNT(*) FROM sales_returns") + " Sales / " +
                readLong("SELECT COUNT(*) FROM purchase_returns") + " Purchase");
        metrics.put("Active Suppliers", String.valueOf(readLong("SELECT COUNT(*) FROM suppliers")));
        metrics.put("Pending Retailer Orders", String.valueOf(readLong("SELECT COUNT(*) FROM sales WHERE LOWER(payment_status) = 'pending'")));
        metrics.put("Total Inventory Value", formatCurrency(readDouble(
                "SELECT COALESCE(SUM(ps.quantity * p.unit_price), 0) FROM product_stock ps JOIN products p ON ps.product_id = p.product_id")));
        return metrics;
    }

    private long readLong(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    private double readDouble(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (SQLException e) {
            return 0.0;
        }
    }

    private String readText(String sql, String defaultValue) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getString(1) : defaultValue;
        } catch (SQLException e) {
            return defaultValue;
        }
    }

    private String formatCurrency(double amount) {
        return "Rs. " + String.format("%.2f", amount);
    }
}
