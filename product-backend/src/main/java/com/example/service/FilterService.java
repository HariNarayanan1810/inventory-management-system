

package com.example.service;

import com.example.dto.FilterDTO;
import com.example.repository.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FilterService {

    @Autowired
    private FilterRepository filterRepository;

    // Service to get all filters
    public FilterDTO getAllFilters() {
        return filterRepository.getFilters();
    }
}
