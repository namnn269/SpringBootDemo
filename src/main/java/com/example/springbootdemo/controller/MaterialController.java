package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.MaterialDto_2;
import com.example.springbootdemo.dto.MaterialSearchDto;
import com.example.springbootdemo.entity.MMaterial;
import com.example.springbootdemo.repository.MMaterialRepository;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PageableDefault;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/material")
public class MaterialController {
    @Autowired
    private MMaterialRepository materialRepo;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @GetMapping
    public Object get() {
        return materialRepo.findAll();
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

    @GetMapping(value = "/page-jpa")
    public Object getJpa(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer size,
                         @RequestParam(required = false) String sortCols,
                         @RequestParam(required = false) String sortTypes) {

        MaterialSearchDto search = new MaterialSearchDto();
//        search.setMaterialCode("aaa");

        Order materialName = Order.asc("materialName");
        Order materialCode = new Order(Sort.Direction.ASC, "materialCode");

        List<Order> orders = List.of(materialName, materialCode);
        Sort sort = Sort.by(orders);
        sort = sort.and(Sort.by(Sort.Direction.DESC, "vendorCode"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Map<String, Object> params = convertToPrams(search);
        Page<Map<String, Object>> pageResult = materialRepo.findDynamicMaterialJpaWithSort(params, pageable);
        List<MaterialDto_2> list = mapToDto(pageResult.toList(), MaterialDto_2.class);
        return List.of(pageResult.getContent(), list);
    }

    public static Map<String, Object> convertToPrams(Object obj) {
        PropertyUtilsBean utilsBean = new PropertyUtilsBean();
        try {
            return utilsBean.describe(obj);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> mapToDto(List<Map<String, Object>> origList, Class<T> clazz) {
        PropertyUtilsBean utilsBean = new PropertyUtilsBean();
        return origList.stream().map(el -> {
            try {
                T t = clazz.getConstructor().newInstance();
                utilsBean.copyProperties(t, el);
                return t;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @GetMapping(value = "/page-native")
    public Object getNative(@RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer size,
                            @RequestParam(required = false) String sortCols,
                            @RequestParam(required = false) String sortTypes,
                            @PageableDefault(page = 2, size = 9) Pageable pageableReq) {

        MaterialSearchDto search = new MaterialSearchDto();

        List<Order> orders = List.of(Order.asc("materialCodexx"), Order.desc("materialNamepp"));
        Sort sort = Sort.unsorted();
        sort = sort.and(Sort.by(new Order[]{}));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Map<String, Object>> pageResult = materialRepo.findDynamicMaterialNativeWithSort(search, pageable);
//        System.out.println(pageResult.getTotalElements());
        return Map.of("data", pageResult.getContent());
    }

    @GetMapping("/pair-search")
    public Object pairSearch(String warehouseGroups ,
                             String materialCodes,
                             String materialNames) {
        String[] warehouseGroupArr = warehouseGroups.split(",");
        String[] materialCodeArr = materialCodes.split(",");
        String[] materialNameArr = materialNames.split(",");
        List<Object[]> pairs = new ArrayList<>();
        for (int i = 0; i < materialCodeArr.length; i++) {
            Object[] pair = new Object[3];
            pair[0] = warehouseGroupArr[i];
            pair[1] = materialCodeArr[i];
            pair[2] = materialNameArr[i];
            pairs.add(pair);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("pairs", pairs);
        String sql = " SELECT m.record_id, m.warehouse_group_code, m.material_code, m.material_name " +
                " FROM m_material m " +
                " WHERE (m.warehouse_group_code, m.material_code, m.material_name) IN (:pairs) ";
        List<Map<String, Object>> list = namedParameterJdbcTemplate.query(sql, params, (rs, i) -> Map.of(
                "recordId", rs.getInt("record_id"),
                "warehouseGroupCode", rs.getInt("warehouse_group_code"),
                "materialCode", rs.getString("material_code"),
                "materialName", rs.getString("material_name")));
        return list;
    }
}
