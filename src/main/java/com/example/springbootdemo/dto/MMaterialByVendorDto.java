package com.example.springbootdemo.dto;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MMaterialByVendorDto {
    private Integer recordId;
    private String materialCode;
    private String materialName;
    private String warehouseGroupCode;
    private String remarks;
    private Integer countNumber;
}
