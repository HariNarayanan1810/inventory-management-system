/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.inventorydashboard.service;

/**
 *
 * @author GEETHA
 */



import com.example.inventorydashboard.model.Purchase;
import com.example.inventorydashboard.config.DatabaseConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PurchaseService {

    public List<Purchase> getAllPurchaseOrders() {
        List<Purchase> orders = new ArrayList<>();
        String sql = "SELECT * FROM purchase_orders ORDER BY purchase_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Purchase order = new Purchase();
                order.setPurchaseId(rs.getInt("purchase_id"));
                order.setSupplierId(rs.getInt("supplier_id"));
                order.setWarehouseId(rs.getInt("warehouse_id"));
                order.setPaymentStatus(rs.getString("payment_status"));
                order.setDeliveryStatus(rs.getString("delivery_status"));
                Timestamp timestamp = rs.getTimestamp("purchase_date");
                if (timestamp != null) {
                    order.setPurchaseDate(timestamp.toLocalDateTime());
                }
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public boolean addPurchase(Purchase purchase) {
        String sql = "INSERT INTO purchase_orders (supplier_id, warehouse_id, purchase_date, payment_status, delivery_status, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDateTime now = LocalDateTime.now();

            stmt.setInt(1, purchase.getSupplierId());
            stmt.setInt(2, purchase.getWarehouseId());
            stmt.setTimestamp(3, Timestamp.valueOf(now));
            stmt.setString(4, purchase.getPaymentStatus());
            stmt.setString(5, purchase.getDeliveryStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(now));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePurchase(Purchase purchase) {
        String sql = "UPDATE purchase_orders SET supplier_id = ?, warehouse_id = ?, payment_status = ?, delivery_status = ?, updated_at = ? WHERE purchase_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, purchase.getSupplierId());
            stmt.setInt(2, purchase.getWarehouseId());
            stmt.setString(3, purchase.getPaymentStatus());
            stmt.setString(4, purchase.getDeliveryStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(6, purchase.getPurchaseId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePurchase(int purchaseId) {
        String sql = "DELETE FROM purchase_orders WHERE purchase_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, purchaseId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Integer> getSupplierIds() {
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT supplier_id FROM suppliers";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(rs.getInt("supplier_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }

    public ObservableList<Integer> getWarehouseIds() {
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT warehouse_id FROM warehouses";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(rs.getInt("warehouse_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }
}
