package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.MaterialSearchDto;
import com.example.springbootdemo.dto.ReqParam;
import com.example.springbootdemo.entity.User;
import com.example.springbootdemo.repository.MMaterialRepository;
import com.example.springbootdemo.repository.TimeZoneRepository;
import com.example.springbootdemo.repository.UserRepository;
import com.example.springbootdemo.validator_service.ValidatorExtendsValidatorBeanSpring;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/spel")
@RequiredArgsConstructor
public class SpELController {
    final UserRepository userRepo;
    final TimeZoneRepository timeZoneRepo;
    final EntityManager entityManager;
    final MMaterialRepository materialRepo;
    final ValidatorExtendsValidatorBeanSpring validatorBeanSpring;

    @GetMapping("/without-param-name")
    public Object get1(@RequestParam String address,
                       @RequestParam String add) {
        return userRepo.findByAddressWithoutParamName(address, add);
    }

    @GetMapping("/with-param-name")
    public Object get2(@RequestParam String address,
                       @RequestParam String add) {
        return userRepo.findByAddressWithParamName(address, add);
    }

    @GetMapping("/with-body-param")
    public Object get3(@RequestBody ReqParam req) {
        return userRepo.findByAddressWithReqBody(req);
    }

    @GetMapping("/with-body-param-native")
    public Object get4(@RequestBody ReqParam req) {
        return userRepo.findByAddressWithReqBodyNative(req);
    }

    @GetMapping("/dynamic-table-user")
    public Object get5() {
        return userRepo.findByDynamicEntity();
    }

    @GetMapping("/dynamic-table-timezone")
    public Object get6() {
        return timeZoneRepo.findByDynamicEntity();
    }

    @GetMapping("/dynamic-sort-search-material")
    public Object material(MaterialSearchDto search) {

        if (validatorBeanSpring.hasError(search)) {
            return validatorBeanSpring.validateAndGetMessages(search);
        }

        Map<String, String> reqParamAndEntityFieldMap = new HashMap<>();
        reqParamAndEntityFieldMap.put("materialCode", "materialCode");
        reqParamAndEntityFieldMap.put("materialName", "materialName");
        reqParamAndEntityFieldMap.put("supplierCode", "supplierCode");
        reqParamAndEntityFieldMap.put("vendorCode", "mmbv.materialVendorCode");

//        List<Sort.Order> orderList = new ArrayList<>();
//        if (search.getSortCols() != null && search.getSortTypes() != null) {
//            String[] sortTypes = search.getSortTypes().split(",");
//            String[] sortCols = search.getSortCols().split(",");
//            for (int i = 0; i < sortTypes.length; i++) {
//                Sort.Order order = null;
//                if (sortCols[i].equalsIgnoreCase("materialCode")) {
//                    order = getOrder(sortTypes[i], "materialCode");
//                } else if (sortCols[i].equalsIgnoreCase("materialName")) {
//                    order = getOrder(sortTypes[i], "materialName");
//                } else if (sortCols[i].equalsIgnoreCase("supplierCode")) {
//                    order = getOrder(sortTypes[i], "supplierCode");
//                } else if (sortCols[i].equalsIgnoreCase("vendorCode")) {
//                    order = getOrder(sortTypes[i], "mmbv.materialVendorCode");
//                }
//                if (order != null) orderList.add(order);
//            }
//        }
//        orderList.add(getOrder("asc", "recordId"));
        Sort sort = Sort.by(getOrderList(search.getSortCols(), search.getSortTypes(), reqParamAndEntityFieldMap));

        PageRequest pageRequest = PageRequest.of(search.getPageNo() - 1, search.getPageSize(), sort);

        return materialRepo.findDynamicMaterial(search, pageRequest);
    }

    private Sort.Order getOrder(String sortCol, String sortType) {
        Sort.Direction direction = sortType.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new Sort.Order(direction, sortCol);
    }

    private List<Sort.Order> getOrderList(String sortColsReq, String sortTypesReq, Map<String, String> reqParamAndEntityFieldMap) {
        List<Sort.Order> orderList = new ArrayList<>();
        if (sortColsReq != null && sortTypesReq != null) {
            String[] sortTypeArr = sortTypesReq.split(",");
            String[] sortColArr = sortColsReq.split(",");
            for (int i = 0; i < sortTypeArr.length; i++) {
                Sort.Order order;
                if (reqParamAndEntityFieldMap.containsKey(sortColArr[i])) {
                    order = getOrder(reqParamAndEntityFieldMap.get(sortColArr[i]), sortTypeArr[i]);
                } else continue;
                orderList.add(order);
            }
        }
        orderList.add(getOrder("recordId", "asc"));
        return orderList;
    }

}
