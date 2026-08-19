package com.example.service;

import com.example.dto.ProductDTO;
import com.example.dto.ProductResponseDTO;
import com.example.model.Product; // Import Product
import com.example.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import jakarta.persistence.EntityNotFoundException; // Import EntityNotFoundException

import java.time.LocalDateTime; // Import LocalDateTime
import java.util.List;
import java.util.Optional; // Import Optional

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- Modified method for fetching products (active or archived) ---
    // Added boolean isArchived parameter
    public ProductResponseDTO getFilteredProducts(
            String search,
            List<Long> categoryIds,
            List<Long> supplierIds,
            List<Long> warehouseIds,
            Double minPrice,
            Double maxPrice,
            boolean isArchived, // Added: Parameter for archived status
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Keep the existing null handling logic from your working code
        if (categoryIds == null) {
            categoryIds = List.of();
        }
        if (supplierIds == null) {
            supplierIds = List.of();
        }
        if (warehouseIds == null) {
            warehouseIds = List.of();
        }

        List<ProductDTO> products = productRepository.findFilteredProducts(
                search,
                categoryIds,
                supplierIds,
                warehouseIds,
                minPrice,
                maxPrice,
                isArchived, // Added: Pass isArchived to repository
                pageable
        );

        long totalCount = productRepository.countFilteredProducts(
                search,
                categoryIds,
                supplierIds,
                warehouseIds,
                minPrice,
                maxPrice,
                isArchived // Added: Pass isArchived to repository count method
        );

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProducts(products);
        responseDTO.setTotalCount(totalCount);

        return responseDTO;
    }

    // --- Methods for Edit/Archive/Unarchive (Copied from previous steps) ---

    /**
     * Updates an existing product.
     * Fetches the entity, updates its fields from the DTO, and saves it.
     * @param productId The ID of the product to update.
     * @param updatedProductDTO The DTO containing the updated product data.
     * @return The updated Product entity.
     * @throws EntityNotFoundException if the product with the given ID is not found.
     */
     @Transactional // Ensure the update operation is transactional
    public Product updateProduct(Long productId, ProductDTO updatedProductDTO) {
        // 1. Fetch the existing Product entity by ID
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("Product with ID " + productId + " not found.");
        }

        Product productToUpdate = productOptional.get();

        // 2. Update the fields of the entity from the DTO
        // Note: Only update fields that are meant to be editable via the UI
        // ID, createdAt, etc., should generally not be updated here.
        // isArchived is handled by separate archive/unarchive methods.
        productToUpdate.setImageUrl(updatedProductDTO.getImageUrl());
        productToUpdate.setName(updatedProductDTO.getProductName());
        productToUpdate.setSku(updatedProductDTO.getSku());
        // Assuming category is updated by string name - make sure your Entity/DB supports this
        productToUpdate.setCategory(updatedProductDTO.getCategory());
        // TODO: If supplier is selected from a list, you'd look up the Supplier entity
        // and set the supplier field on productToUpdate: productToUpdate.setSupplier(...);
        // For now, ignoring supplierName from DTO as it's not on the Product entity
        // productToUpdate.setSupplierId(...); // Or update supplierId if using ID

        productToUpdate.setProductRating(updatedProductDTO.getProductRating());
        productToUpdate.setUnitPrice(updatedProductDTO.getUnitPrice());
        productToUpdate.setDefaultSellingPrice(updatedProductDTO.getSellingPrice()); // Default selling price maps to sellingPrice in DTO

        // Removed: productToUpdate.setMarginPercent(updatedProductDTO.getMarginPercent());
        // marginPercent is derived, not stored directly on the Product entity

        // Update the updated_at timestamp
        productToUpdate.setUpdatedAt(LocalDateTime.now());

        // 3. Save the updated entity
        return productRepository.save(productToUpdate);
    }

    /**
     * Archives a product by setting its is_archived flag to TRUE.
     * @param productId The ID of the product to archive.
     * @throws EntityNotFoundException if the product with the given ID is not found (optional check).
     */
     @Transactional // Ensure the archive operation is transactional
    public void archiveProduct(Long productId) {
        // Optional: Check if the product exists first if you want to throw an error
        // productRepository.findByProductId(productId).orElseThrow(() -> new EntityNotFoundException("Product with ID " + productId + " not found for archiving."));

        // Use the repository's native query method for direct update
        productRepository.updateIsArchivedStatus(productId,true);
    }

    /**
     * Unarchives a product by setting its is_archived flag to FALSE.
     * @param productId The ID of the product to unarchive.
     * @throws EntityNotFoundException if the product with the given ID is not found (optional check).
     */
    @Transactional // Ensure the unarchive operation is transactional
    public void unarchiveProduct(Long productId) {
         // Optional: Check if the product exists first
         // productRepository.findByProductId(productId).orElseThrow(() -> new EntityNotFoundException("Product with ID " + productId + " not found for unarchiving."));

        // Use the repository's native query method for direct update
        productRepository.updateIsArchivedStatus(productId,false);
    }
}