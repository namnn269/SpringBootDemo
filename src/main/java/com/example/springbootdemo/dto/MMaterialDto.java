package com.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MMaterialDto {
    private Integer recordId;

    private Integer version;

    private String systemUserCode;

    private String warehouseGroupCode;

    private String supplierCode;

    private String materialCode;

    private String materialName;

    private String materialShort;

    private String materialCategory;

    private String materialProcurementType;

    private Integer materialSizeD;

    private Integer materialSizeW;

    private Integer materialSizeH;

    private String remarks;

    private Integer createBy = 0;

    private Date createDateTime;

    private Integer updateBy = 0;

    private Date updateDateTime = new Date();

    private Integer fDelete = 0;

}
