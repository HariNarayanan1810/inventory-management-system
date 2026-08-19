package com.example.inventorydashboard.service;

import com.example.inventorydashboard.config.DatabaseConfig;
import com.example.inventorydashboard.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.sku, p.category, COALESCE(p.supplier_id, 0) AS supplier_id, " +
                "p.unit_price, p.default_selling_price, p.reorder_point, COALESCE(SUM(ps.quantity), 0) AS stock " +
                "FROM products p " +
                "LEFT JOIN product_stock ps ON p.product_id = ps.product_id " +
                "WHERE COALESCE(p.is_archived, FALSE) = FALSE " +
                "GROUP BY p.product_id, p.name, p.sku, p.category, p.supplier_id, p.unit_price, p.default_selling_price, p.reorder_point " +
                "ORDER BY p.product_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, sku, category, supplier_id, unit_price, default_selling_price, reorder_point, created_at, updated_at, is_archived) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, FALSE)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDateTime now = LocalDateTime.now();
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getSku());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getSupplierId());
            stmt.setDouble(5, product.getUnitPrice());
            stmt.setDouble(6, product.getSellingPrice());
            stmt.setInt(7, product.getReorderPoint());
            stmt.setTimestamp(8, Timestamp.valueOf(now));
            stmt.setTimestamp(9, Timestamp.valueOf(now));

            if (stmt.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    saveStock(conn, keys.getInt(1), 1, product.getStock(), product.getReorderPoint());
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, sku = ?, category = ?, supplier_id = ?, unit_price = ?, " +
                "default_selling_price = ?, reorder_point = ?, updated_at = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getSku());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getSupplierId());
            stmt.setDouble(5, product.getUnitPrice());
            stmt.setDouble(6, product.getSellingPrice());
            stmt.setInt(7, product.getReorderPoint());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(9, product.getId());

            boolean updated = stmt.executeUpdate() > 0;
            if (updated) {
                saveStock(conn, product.getId(), 1, product.getStock(), product.getReorderPoint());
            }
            return updated;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean archiveProduct(int productId) {
        String sql = "UPDATE products SET is_archived = TRUE, updated_at = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveStock(Connection conn, int productId, int warehouseId, int quantity, int lowStockThreshold) throws SQLException {
        String sql = "INSERT INTO product_stock (product_id, warehouse_id, quantity, low_stock_threshold) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (product_id, warehouse_id) DO UPDATE SET quantity = EXCLUDED.quantity, low_stock_threshold = EXCLUDED.low_stock_threshold";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, warehouseId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, lowStockThreshold);
            stmt.executeUpdate();
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setSku(rs.getString("sku"));
        product.setCategory(rs.getString("category"));
        product.setSupplierId(rs.getInt("supplier_id"));
        product.setUnitPrice(rs.getDouble("unit_price"));
        product.setSellingPrice(rs.getDouble("default_selling_price"));
        product.setReorderPoint(rs.getInt("reorder_point"));
        product.setStock(rs.getInt("stock"));
        return product;
    }
}
