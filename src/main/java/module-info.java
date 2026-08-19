module com.example.inventorydashboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;       // JDBC access
    requires itextpdf;       // iText PDF library 
    
    opens com.example.inventorydashboard.model to javafx.base;
    opens com.example.inventorydashboard to javafx.graphics;
    opens com.example.inventorydashboard.controller to javafx.fxml;

    exports com.example.inventorydashboard;
    exports com.example.inventorydashboard.controller;
    exports com.example.inventorydashboard.service;
}
