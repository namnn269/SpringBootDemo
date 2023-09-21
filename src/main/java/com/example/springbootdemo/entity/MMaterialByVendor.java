package com.example.springbootdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "m_material_by_vendor")
public class MMaterialByVendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "system_user_code", nullable = false)
    private String systemUserCode;

    @Column(name = "warehouse_group_code", nullable = false)
    private String warehouseGroupCode;

    @Column(name = "material_code")
    private String materialCode;

    @Column(name = "material_vendor_code")
    private String materialVendorCode;

    @Column(name = "vendor_material_code")
    private String vendorMaterialCode;

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
