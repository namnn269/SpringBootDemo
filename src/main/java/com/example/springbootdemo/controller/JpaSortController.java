package com.example.springbootdemo.controller;

import com.example.springbootdemo.infrastructure.database.logicom.repository.MMaterialByVendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/jpa-sort")
public class JpaSortController {

    private final MMaterialByVendorRepository materialByVendorRepo;

    public JpaSortController(MMaterialByVendorRepository materialByVendorRepo) {
        this.materialByVendorRepo = materialByVendorRepo;
    }

    @GetMapping("/normal-sort")
    public Object normalSort(@RequestParam Map<String, Object> params) {
        JpaSort sort = JpaSort
                .unsafe(JpaSort.Direction.ASC, "(countNumber)")
                .andUnsafe(Sort.Direction.ASC, "(materialCode)");
        Pageable pageable = PageRequest.of(2,4, sort);
        Page<Map<String ,Object>> page = materialByVendorRepo.sortWithSubSelectNative(params, pageable);
        return page.getContent();
    }
}
