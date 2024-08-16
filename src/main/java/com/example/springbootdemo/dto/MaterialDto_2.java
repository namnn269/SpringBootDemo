package com.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDto_2 {
    private String materialCode;
    private String materialName;
    private String supplierCode;
    private String vendorCode;
    private Date updateDateTime;

    private Integer recordId2;

}
