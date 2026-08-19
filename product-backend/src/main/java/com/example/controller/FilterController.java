

package com.example.controller;

import com.example.dto.FilterDTO;
import com.example.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/filters")
public class FilterController {

    @Autowired
    private FilterService filterService;

    // Endpoint to get all filters
    @GetMapping
    public ResponseEntity<FilterDTO> getFilters() {
        FilterDTO filters = filterService.getAllFilters();
        return ResponseEntity.ok(filters);
    }
}
