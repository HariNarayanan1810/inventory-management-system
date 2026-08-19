package com.example.inventorydashboard.model;

public class DashboardCard {
    private String title;
    private String value;
    private String icon; // path to icon or symbol (optional)
    private String status; // up/down/both

    public DashboardCard(String title, String value, String icon, String status) {
        this.title = title;
        this.value = value;
        this.icon = icon;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getValue() { return value; }
    public String getIcon() { return icon; }
    public String getStatus() { return status; }
}
