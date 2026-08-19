package com.example.inventorydashboard.repository;

import com.example.inventorydashboard.config.DatabaseConfig;
import com.example.inventorydashboard.model.PurchaseReturn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PurchaseReturnRepository {

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    public List<PurchaseReturn> findAll() {
        List<PurchaseReturn> returns = new ArrayList<>();
        String sql = "SELECT return_id, purchase_id, product_id, quantity_returned, reason, return_status, return_date " +
                "FROM purchase_returns ORDER BY return_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                returns.add(new PurchaseReturn(
                        rs.getInt("return_id"),
                        rs.getInt("purchase_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity_returned"),
                        rs.getString("reason"),
                        rs.getString("return_status"),
                        rs.getDate("return_date").toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returns;
    }

    public boolean save(PurchaseReturn purchaseReturn) {
        lastError = "";
        String sql = "INSERT INTO purchase_returns (purchase_id, product_id, quantity_returned, reason, refund_amount, return_status, return_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, purchaseReturn.getPurchaseId());
            stmt.setInt(2, purchaseReturn.getProductId());
            stmt.setInt(3, purchaseReturn.getQuantity());
            stmt.setString(4, purchaseReturn.getReason());
            stmt.setDouble(5, 0.0);
            stmt.setString(6, purchaseReturn.getStatus());
            stmt.setDate(7, Date.valueOf(purchaseReturn.getDate()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
}
