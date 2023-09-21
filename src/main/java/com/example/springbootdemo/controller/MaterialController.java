package com.example.springbootdemo.controller;

import com.example.springbootdemo.entity.MMaterial;
import com.example.springbootdemo.repository.MMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/material")
public class MaterialController {
    @Autowired
    private MMaterialRepository materialRepo;

    @GetMapping
    public Object get() {
        List<MMaterial> in = materialRepo.findForShareByRecordIdIn(List.of());
        List<MMaterial> notIn = materialRepo.findByRecordIdNotIn(List.of());
        Map<String, List<MMaterial>> res = new HashMap<>();
        res.put("in", in);
        res.put("notIn", notIn);
        return res;
    }

    @GetMapping(value = "/page")
    public Object get2(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String sortCols,
                       @RequestParam(required = false) String sortTypes) {

        List<Sort.Order> orders = new LinkedList<>();
        if (sortCols != null && sortTypes != null) {
            String[] sortColArr = sortCols.split(",");
            String[] sortTypeArr = sortTypes.split(",");
            for (int i = 0; i < sortTypeArr.length; i++) {
                String fieldSort;
                switch (sortColArr[i]) {
                    case "materialCode" -> fieldSort = "materialCode";
                    case "materialVendorCode" -> fieldSort = "mmbv.materialVendorCode";
                    case "vendorMaterialCode" -> fieldSort = "mmbv.vendorMaterialCode";
                    default -> fieldSort = "";
                }
                Sort.Order order = sortTypeArr[i].equalsIgnoreCase("ASC")
                        ? Sort.Order.asc(fieldSort)
                        : Sort.Order.desc(fieldSort);
                orders.add(order);
            }
        } else {
            Sort.Order order = Sort.Order.desc("mmbv.recordId");
            orders.add(order);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(orders));

        return materialRepo.findFromTwoTables(pageRequest);
    }

}
