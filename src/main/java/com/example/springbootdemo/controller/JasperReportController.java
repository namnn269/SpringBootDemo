package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.Customer;
import com.example.springbootdemo.dto.Phone;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/report")
public class JasperReportController {
    @GetMapping
    public Object report() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1, "customer 1", 45));
        customers.add(new Customer(2, "customer 2", 46));
        customers.add(new Customer(3, "customer 3", 47));
        customers.add(new Customer(4, "customer 4", 48));
        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone(11, "Apple", new Date("02/02/2000")));
        phones.add(new Phone(12, "Samsung", new Date()));
        phones.add(new Phone(13, "Oppo", new Date("02/07/2020")));

        try {
            JasperReport jasperReport = JasperCompileManager
                    .compileReport("C:\\Users\\Nam\\JaspersoftWorkspace\\MyReports\\temp_1.jrxml");
//                    .compileReport(new ClassPathResource("C:\\Users\\Nam\\JaspersoftWorkspace\\MyReports\\temp_1.jrxml").getInputStream());

            Map<String, Object> testMap = new HashMap<>();
            testMap.put("mapParam1", "map param 1");
            testMap.put("mapParam2", "map param 2");
            Map<String, Object> testMap2 = new HashMap<>();
            testMap2.put("mapParam1", "map param 1-2");
            testMap2.put("mapParam2", "map param 2-2");
            testMap2.put("mapParam3", "map param 3-2");
            testMap2.put("mapParam4", "map param 4-2");

            Map<String, Object> params = new HashMap<>();
            params.put("materialCode", "material 10");
            params.put("nextApplicationDate", new Date());
            params.put("dataset_phones", new JRBeanCollectionDataSource(phones));
            params.put("dataset_customers", new JRBeanCollectionDataSource(customers));
            params.put("dataset_customers_map", new JRMapCollectionDataSource(List.of(testMap, testMap2)));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\Nam\\Desktop\\report.pdf");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>("OK OK", HttpStatus.OK);
    }

    public static void main(String[] args) {
        System.out.println(new Date("02/02/2000"));
    }
}
