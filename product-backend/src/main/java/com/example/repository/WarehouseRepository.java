

package com.example.repository;

import com.example.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
