package com.example.springbootdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * information about MMaterial.
 *
 * @author cuongtd
 * @version 1.0 04/05/2023 create
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "m_material")
public class MMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "system_user_code", nullable = false)
    private String systemUserCode;

    @Column(name = "warehouse_group_code", nullable = false)
    private String warehouseGroupCode;

    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @Column(name = "material_code", nullable = false)
    private String materialCode;

    @Column(name = "material_name")
    private String materialName;

    @Column(name = "material_short")
    private String materialShort;

    @Column(name = "material_category", nullable = false)
    private String materialCategory;

    @Column(name = "material_procurement_type", nullable = false)
    private String materialProcurementType;

    @Column(name = "material_size_d", nullable = false)
    private Integer materialSizeD;

    @Column(name = "material_size_w", nullable = false)
    private Integer materialSizeW;

    @Column(name = "material_size_h", nullable = false)
    private Integer materialSizeH;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "create_user_record_id")
    private Integer createBy = 0;

    @CreationTimestamp
    @Column(name = "create_date_time")
    private Date createDateTime;

    @Column(name = "update_user_record_id")
    private Integer updateBy = 0;

    @UpdateTimestamp
    @Column(name = "update_date_time")
    private Date updateDateTime = new Date();

    @Column(name = "f_delete")
    private Integer fDelete = 0;

}
