package com.example.inventorydashboard.service;

import com.example.inventorydashboard.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaService {

    public void ensureCompatibleSchema() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS customer_name VARCHAR(140)");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS product_id INTEGER");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS warehouse_id INTEGER");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS quantity INTEGER DEFAULT 1");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS payment_status VARCHAR(40) DEFAULT 'Pending'");
            stmt.executeUpdate("ALTER TABLE sales ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");

            stmt.executeUpdate("ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS refund_amount NUMERIC(12, 2) DEFAULT 0");
            stmt.executeUpdate("ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS return_status VARCHAR(40) DEFAULT 'pending'");
            stmt.executeUpdate("ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS return_date DATE DEFAULT CURRENT_DATE");
            stmt.executeUpdate("ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS warehouse_id INTEGER");

            stmt.executeUpdate("ALTER TABLE purchase_returns ADD COLUMN IF NOT EXISTS refund_amount NUMERIC(12, 2) DEFAULT 0");
            stmt.executeUpdate("ALTER TABLE purchase_returns ALTER COLUMN refund_amount SET DEFAULT 0");
            stmt.executeUpdate("UPDATE purchase_returns SET refund_amount = 0 WHERE refund_amount IS NULL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
