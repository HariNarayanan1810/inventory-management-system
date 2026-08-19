package com.example.controller;

import com.example.dto.ProductDTO; // Import ProductDTO
import com.example.dto.ProductResponseDTO;
import com.example.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus; // Import HttpStatus for response codes
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- Modified endpoint for fetching products (active or archived) ---
    // Added @RequestParam(defaultValue = "false") boolean isArchived
    @GetMapping("/products")
    public ProductResponseDTO getFilteredProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) List<Long> supplierIds,
            @RequestParam(required = false) List<Long> warehouseIds,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "false") boolean isArchived, // Added parameter
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        // Call the service method, passing the isArchived flag
        return productService.getFilteredProducts(search, categoryIds, supplierIds, warehouseIds, minPrice, maxPrice, isArchived, page, size);
    }

    // --- New endpoint to UPDATE a product ---
    @PutMapping("/products/{id}") // Matches the frontend ProductAPI
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        System.out.println("Received update request for product ID: " + id);
        // Basic validation: check if the ID in path matches the ID in the DTO (good practice)
        if (productDTO.getId() == null || !productDTO.getId().equals(id)) {
             // Or handle this validation in service/global exception handler
             return ResponseEntity.badRequest().build(); // Return 400 Bad Request if IDs don't match
        }

        try {
            productService.updateProduct(id, productDTO);
            // Return 200 OK or 204 No Content on success
            return ResponseEntity.ok().build(); // Or ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            System.err.println("Update failed: Product with ID " + id + " not found.");
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        } catch (Exception e) {
            System.err.println("An error occurred during product update for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
             // Return 500 Internal Server Error for other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- New endpoint to ARCHIVE a product ---
    @PatchMapping("/products/{id}/archive") // Matches the frontend ProductAPI
    public ResponseEntity<Void> archiveProduct(@PathVariable Long id) {
        System.out.println("Received archive request for product ID: " + id);
        try {
             productService.archiveProduct(id);
             // Return 200 OK or 204 No Content on success
             return ResponseEntity.ok().build(); // Or ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            System.err.println("Archive failed: Product with ID " + id + " not found.");
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        } catch (Exception e) {
            System.err.println("An error occurred during product archive for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

     // --- New endpoint to UNARCHIVE a product ---
    @PatchMapping("/products/{id}/unarchive") // Matches the frontend ProductAPI
    public ResponseEntity<Void> unarchiveProduct(@PathVariable Long id) {
        System.out.println("Received unarchive request for product ID: " + id);
        try {
             productService.unarchiveProduct(id);
             // Return 200 OK or 204 No Content on success
             return ResponseEntity.ok().build(); // Or ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
             System.err.println("Unarchive failed: Product with ID " + id + " not found.");
             return ResponseEntity.notFound().build(); // Return 404 Not Found
        } catch (Exception e) {
            System.err.println("An error occurred during product unarchive for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}