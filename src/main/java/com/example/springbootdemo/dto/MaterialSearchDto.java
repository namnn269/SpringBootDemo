package com.example.springbootdemo.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialSearchDto {
    private String materialName;
    private String materialCode;
    private String supplierCode;
    private String vendorCode;
    private String sortTypes;
    private String sortCols;
    @Min(1)
    private Integer pageNo = 0;
    @Min(1)
    private Integer pageSize = 5;

}
