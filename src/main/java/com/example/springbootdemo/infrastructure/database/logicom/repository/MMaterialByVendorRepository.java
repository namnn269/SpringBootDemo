package com.example.springbootdemo.infrastructure.database.logicom.repository;

import com.example.springbootdemo.dto.MMaterialByVendorDto;
import com.example.springbootdemo.infrastructure.database.logicom.entity.MMaterialByVendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MMaterialByVendorRepository extends JpaRepository<MMaterialByVendor, Integer> {

    @Query(value = """
            SELECT new com.example.springbootdemo.dto.MMaterialByVendorDto(
              mbv.recordId AS recordId,
              mbv.materialCode AS materialCode,
              mm.materialName AS materialName,
              mm.warehouseGroupCode AS warehouseGroupCode,
              mbv.remarks AS remarksmbv,
              ( SELECT MAX(mm2.recordId)
               FROM MMaterial mm2
               WHERE mm2.materialCode = mm.materialCode
               AND mm2.warehouseGroupCode = mm.warehouseGroupCode
               AND mm2.systemUserCode = mm.systemUserCode ) AS countNumber) 
            FROM MMaterialByVendor mbv
            JOIN MMaterial mm
              ON mm.materialCode = mbv.materialCode
              AND mm.systemUserCode = mbv.systemUserCode
              AND mm.warehouseGroupCode = mbv.warehouseGroupCode
            WHERE (:#{#params.get('materialCode')} IS NULL OR mbv.materialCode = :#{#params.get('materialCode')})
              AND (:#{#params.get('warehouseGroupCode')} IS NULL OR mbv.materialCode = :#{#params.get('warehouseGroupCode')})
            """, //WARNING: hibernate 6. khong sort duoc subSelect field: countNumber voi Sort.by() va JpaSort.unsafe() trong JPQL
            countQuery = """
                    SELECT COUNT(*)
                    FROM MMaterialByVendor mbv
                    JOIN MMaterial mm
                      ON mm.materialCode = mbv.materialCode
                      AND mm.systemUserCode = mbv.systemUserCode
                      AND mm.warehouseGroupCode = mbv.warehouseGroupCode
                    WHERE (:#{#params.get('materialCode')} IS NULL OR mbv.materialCode = :#{#params.get('materialCode')})
                      AND (:#{#params.get('warehouseGroupCode')} IS NULL OR mbv.warehouseGroupCode = :#{#params.get('warehouseGroupCode')}) 
                    """
    )
    Page<MMaterialByVendorDto> sortWithSubSelectJpaData(Map<String, Object> params, Pageable pageable);


    @Query(value = """
            SELECT 
              mbv.record_id AS recordId,
              mbv.material_code AS materialCode,
              mm.material_name AS materialName,
              mm.warehouse_group_code AS warehouseGroupCode,
              mbv.remarks AS remarksmbv,
              ( SELECT MAX(mm2.record_id)
               FROM m_material mm2
               WHERE mm2.material_code = mm.material_code
               AND mm2.warehouse_group_code = mm.warehouse_group_code
               AND mm2.system_user_code = mm.system_user_code ) AS countNumber
            FROM m_material_by_vendor mbv
            JOIN m_material mm
              ON mm.material_code = mbv.material_code
              AND mm.system_user_code = mbv.system_user_code
              AND mm.warehouse_group_code = mbv.warehouse_group_code
            WHERE (:#{#params.get('materialCode')} IS NULL OR mbv.material_code = :#{#params.get('materialCode')})
              AND (:#{#params.get('warehouseGroupCode')} IS NULL OR mbv.warehouse_group_code = :#{#params.get('warehouseGroupCode')})
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM m_material_by_vendor mbv
                    JOIN m_material mm
                      ON mm.material_code = mbv.material_code
                      AND mm.system_user_code = mbv.system_user_code
                      AND mm.warehouse_group_code = mbv.warehouse_group_code
                    WHERE (:#{#params.get('materialCode')} IS NULL OR mbv.material_code = :#{#params.get('materialCode')})
                      AND (:#{#params.get('warehouseGroupCode')} IS NULL OR mbv.warehouse_group_code = :#{#params.get('warehouseGroupCode')} )
                    """
    , nativeQuery = true)
    Page<Map<String, Object>> sortWithSubSelectNative(Map<String, Object> params, Pageable pageable);
}
