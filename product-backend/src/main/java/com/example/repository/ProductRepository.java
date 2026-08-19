package com.example.repository;

import com.example.dto.ProductDTO;
import com.example.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {

    // --- Methods for fetching products (active or archived) ---

    // Corrected native query with added product_id and is_archived, and filtering by is_archived.
    // Ensured product_id and is_archived are in the GROUP BY.
    // Retained standard syntax for optional list parameters.
    @Query(value = """
        SELECT
            p.product_id AS id,
            p.image_url AS imageUrl,
            p.name AS productName,
            p.sku AS sku,
            s.name AS supplierName,
            p.category AS category,
            p.product_rating AS productRating,
            p.unit_price AS unitPrice,
            p.default_selling_price AS sellingPrice,
            COALESCE(SUM(ps.quantity), 0) AS stockLevel,
            cm.margin_percent AS marginPercent,
        p.is_archived::boolean AS isArchived -- Add explicit cast here
        FROM products p
        LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
        LEFT JOIN product_stock ps ON p.product_id = ps.product_id
        LEFT JOIN warehouses w ON ps.warehouse_id = w.warehouse_id
        LEFT JOIN category_margins cm ON p.category = cm.category
        WHERE (:search IS NULL OR (p.name ILIKE CONCAT('%', :search, '%') OR p.sku ILIKE CONCAT('%', :search, '%')))
          AND (:categoryIds IS NULL OR cm.category_id IN (:categoryIds))
          AND (:supplierIds IS NULL OR s.supplier_id IN (:supplierIds))
          AND (:warehouseIds IS NULL OR w.warehouse_id IN (:warehouseIds))
          AND (:minPrice IS NULL OR p.default_selling_price >= :minPrice)
          AND (:maxPrice IS NULL OR p.default_selling_price <= :maxPrice)
          AND p.is_archived = :isArchived -- Filter by archived status
        GROUP BY
            p.product_id,
            p.image_url, p.name, p.sku, s.name, cm.category_id, p.category,
            p.product_rating, p.unit_price, p.default_selling_price, cm.margin_percent,
            p.is_archived
        """,
        nativeQuery = true)
    List<ProductDTO> findFilteredProducts(
        @Param("search") String search,
        @Param("categoryIds") List<Long> categoryIds,
        @Param("supplierIds") List<Long> supplierIds,
        @Param("warehouseIds") List<Long> warehouseIds,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("isArchived") boolean isArchived, // Added isArchived parameter
        Pageable pageable
    );

    @Query(value = """
        SELECT COUNT(DISTINCT p.product_id)
            FROM products p
            LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
            LEFT JOIN product_stock ps ON p.product_id = ps.product_id
            LEFT JOIN warehouses w ON ps.warehouse_id = w.warehouse_id
            LEFT JOIN category_margins cm ON p.category = cm.category
            WHERE (:search IS NULL OR (p.name ILIKE CONCAT('%', :search, '%') OR p.sku ILIKE CONCAT('%', :search, '%')))
              AND (:categoryIds IS NULL OR cm.category_id IN (:categoryIds))
              AND (:supplierIds IS NULL OR s.supplier_id IN (:supplierIds))
              AND (:warehouseIds IS NULL OR w.warehouse_id IN (:warehouseIds))
              AND (:minPrice IS NULL OR p.default_selling_price >= :minPrice)
              AND (:maxPrice IS NULL OR p.default_selling_price <= :maxPrice)
              AND p.is_archived = :isArchived -- Filter by archived status
        """,
        nativeQuery = true)
    Long countFilteredProducts(
        @Param("search") String search,
        @Param("categoryIds") List<Long> categoryIds,
        @Param("supplierIds") List<Long> supplierIds,
        @Param("warehouseIds") List<Long> warehouseIds,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("isArchived") boolean isArchived // Added isArchived parameter for count
    );

    // --- Methods for Edit/Archive/Unarchive ---

    // Method to find a product by ID (for update/details fetching)
    Optional<Product> findById(Long productId);

    // Method to save/update a Product entity
    Product save(Product product);

    // Native query to update the is_archived status directly
    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET is_archived = :isArchived, updated_at = CURRENT_TIMESTAMP WHERE product_id = :productId", nativeQuery = true)
    void updateIsArchivedStatus(@Param("productId") Long productId, @Param("isArchived") boolean isArchived);
}