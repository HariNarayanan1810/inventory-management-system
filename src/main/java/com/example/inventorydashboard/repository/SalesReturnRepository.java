package com.example.inventorydashboard.repository;

import com.example.inventorydashboard.config.DatabaseConfig;
import com.example.inventorydashboard.model.SalesReturn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class SalesReturnRepository {

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    public boolean save(SalesReturn salesReturn) {
        lastError = "";
        String sql = "INSERT INTO sales_returns (sale_id, product_id, quantity_returned, reason, refund_amount, return_status, return_date, warehouse_id) " +
                "VALUES ((SELECT sale_id FROM sales WHERE customer_name = ? ORDER BY sale_id DESC LIMIT 1), " +
                "(SELECT product_id FROM products WHERE name = ? LIMIT 1), ?, ?, ?, ?, ?, " +
                "(SELECT warehouse_id FROM sales WHERE customer_name = ? ORDER BY sale_id DESC LIMIT 1))";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, salesReturn.getRetailer());
            pstmt.setString(2, salesReturn.getProduct());
            pstmt.setInt(3, salesReturn.getQuantity());
            pstmt.setString(4, salesReturn.getReason());
            pstmt.setDouble(5, 0.0);
            pstmt.setString(6, "pending");
            pstmt.setDate(7, Date.valueOf(salesReturn.getDate()));
            pstmt.setString(8, salesReturn.getRetailer());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public List<SalesReturn> findAll() {
        List<SalesReturn> salesReturns = new ArrayList<>();

        String sql = "SELECT sr.return_id, s.customer_name, p.name AS product_name, sr.quantity_returned, sr.reason, sr.return_date " +
                "FROM sales_returns sr " +
                "LEFT JOIN sales s ON sr.sale_id = s.sale_id " +
                "LEFT JOIN products p ON sr.product_id = p.product_id " +
                "ORDER BY sr.return_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("return_id");
                int quantity = rs.getInt("quantity_returned");
                String reason = rs.getString("reason");
                LocalDate date = rs.getDate("return_date").toLocalDate();
                String retailer = rs.getString("customer_name");
                String product = rs.getString("product_name");

                SalesReturn sr = new SalesReturn(id, retailer, product, quantity, reason, date);
                salesReturns.add(sr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesReturns;
    }

    public List<String> findRetailerNames() {
        return findStrings("SELECT DISTINCT customer_name FROM sales ORDER BY customer_name");
    }

    public List<String> findProductNames() {
        return findStrings("SELECT name FROM products WHERE COALESCE(is_archived, FALSE) = FALSE ORDER BY name");
    }

    private List<String> findStrings(String sql) {
        List<String> values = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                values.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }
}
