package com.example.inventorydashboard.service;

import com.example.inventorydashboard.config.DatabaseConfig;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SalesService {

    public boolean checkStock(int productId, int quantity) {
        String sql = "SELECT quantity FROM product_stock WHERE product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("quantity");
                return stock >= quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    public boolean saveOrder(String customerName, int productId, int warehouseId, int quantity) {
        lastError = "";
        String sql = "INSERT INTO sales (customer_name, product_id, warehouse_id, quantity, payment_status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            ensureSalesColumns(conn);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, customerName);
                stmt.setInt(2, productId);
                stmt.setInt(3, warehouseId);
                stmt.setInt(4, quantity);
                stmt.setString(5, "Pending");
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    private void ensureSalesColumns(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS customer_name VARCHAR(140)");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS product_id INTEGER");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS warehouse_id INTEGER");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS quantity INTEGER DEFAULT 1");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS payment_status VARCHAR(40) DEFAULT 'Pending'");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        }

        if (!hasColumn(conn, "sales", "product_id")) {
            throw new SQLException("Required database column sales.product_id is missing. Apply database/schema.sql to the inventory_management database.");
        }
    }

    private boolean hasColumn(Connection conn, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getColumns(null, null, tableName, columnName)) {
            return rs.next();
        }
    }

    public Path generateInvoicePDF(String customerName, String filePath) {
        lastError = "";
        Document document = new Document();
        String sql =
                "SELECT s.sale_id, s.customer_name, p.name AS product_name, p.supplier_id, " +
                        "s.quantity, s.payment_status " +
                        "FROM sales s " +
                        "JOIN products p ON s.product_id = p.product_id " +
                        "WHERE s.customer_name = ? " +
                        "ORDER BY s.sale_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();

            Path invoicePath = Paths.get(filePath).toAbsolutePath().normalize();
            Path parent = invoicePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            PdfWriter.getInstance(document, new FileOutputStream(invoicePath.toFile()));
            document.open();
            document.add(new Paragraph("Invoice"));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.addCell("Customer Name");
            table.addCell("Product Name");
            table.addCell("Supplier ID");
            table.addCell("Quantity");
            table.addCell("Payment Status");
            table.addCell("Date");

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                table.addCell(rs.getString("customer_name"));
                table.addCell(rs.getString("product_name"));
                table.addCell(rs.getString("supplier_id"));
                table.addCell(rs.getString("quantity"));
                table.addCell(rs.getString("payment_status"));
                table.addCell(java.time.LocalDate.now().toString());
            }

            if (!hasRows) {
                table.addCell(customerName);
                table.addCell("N/A");
                table.addCell("N/A");
                table.addCell("N/A");
                table.addCell("N/A");
                table.addCell(java.time.LocalDate.now().toString());
            }

            document.add(table);
            return invoicePath;
        } catch (DocumentException | IOException | SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return null;
        } finally {
            document.close();
        }
    }
}
