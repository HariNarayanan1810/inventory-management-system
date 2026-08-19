package com.example.repository;

import com.example.dto.FilterDTO;
import com.example.model.Category;
import com.example.model.Supplier;
import com.example.model.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FilterRepository {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public FilterDTO getFilters() {
        FilterDTO filterDTO = new FilterDTO();

        List<FilterDTO.FilterItem> warehouses = warehouseRepository.findAll()
                .stream()
                .map(w -> new FilterDTO.FilterItem(w.getWarehouseId(), w.getName()))
                .collect(Collectors.toList());

        List<FilterDTO.FilterItem> suppliers = supplierRepository.findAll()
                .stream()
                .map(s -> new FilterDTO.FilterItem(s.getSupplierId(), s.getName()))
                .collect(Collectors.toList());

        List<FilterDTO.FilterItem> categories = categoryRepository.findAll()
                .stream()
                .map(c -> new FilterDTO.FilterItem(c.getcategoryId(), c.getCategory()))
                .collect(Collectors.toList());

        filterDTO.setWarehouses(warehouses);
        filterDTO.setSuppliers(suppliers);
        filterDTO.setCategories(categories);

        return filterDTO;
    }
}
