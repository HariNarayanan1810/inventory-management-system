    package com.example.model;
    import jakarta.persistence.*;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "suppliers")
    public class Supplier {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        
        @Column(name = "supplier_id", nullable = false)
        private Long supplierId;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "contact_name")
        private String contactName;

        @Column(name = "phone")
        private String phone;

        @Column(name = "email")
        private String email;

        @Column(name = "address")
        private String address;

        @Column(name = "supplier_rating")
        private Double supplierRating;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        // Getters and Setters
        public Long getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(Long supplierId) {
            this.supplierId = supplierId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Double getSupplierRating() {
            return supplierRating;
        }

        public void setSupplierRating(Double supplierRating) {
            this.supplierRating = supplierRating;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
