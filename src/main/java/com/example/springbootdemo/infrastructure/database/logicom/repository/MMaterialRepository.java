package com.example.springbootdemo.infrastructure.database.logicom.repository;

import com.example.springbootdemo.dto.MaterialDto_1;
import com.example.springbootdemo.dto.MaterialSearchDto;
import com.example.springbootdemo.infrastructure.database.logicom.entity.MMaterial;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface MMaterialRepository extends JpaRepository<MMaterial, Integer> {

    long countBySystemUserCodeAndMaterialCode(String systemUserCode, String materialCode);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0"))
    @Transactional
    List<MMaterial> findForShareByRecordIdIn(List<Integer> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0"))
    List<MMaterial> findForUpdateByRecordIdIn(List<Integer> ids);

    List<MMaterial> findByRecordIdNotIn(List<Integer> ids);

    @Query(value = " SELECT new com.example.springbootdemo.dto.MaterialDto_1 " +
            " (mm.materialCode, mmbv.materialVendorCode, mmbv.vendorMaterialCode) " +
            " FROM MMaterial mm " +
            " JOIN MMaterialByVendor mmbv ON mm.materialCode = mmbv.materialCode " +
            " WHERE TRUE ")
    List<MaterialDto_1> findFromTwoTables(PageRequest pageable);

    @Query(value = " SELECT mm.material_code, mm.material_name " +
            " FROM m_material mm " +
            " WHERE mm.record_id IN (:ids) FOR UPDATE NOWAIT ", nativeQuery = true)
    List<Object[]> selectMaterialNativeQuery(@Param("ids") List<Integer> ids);

    @Query(value = " SELECT m.materialCode AS materialCode, " +
            " m.materialName AS materialName, " +
            " m.supplierCode AS supplierCode, " +
            " mmbv.materialVendorCode AS vendorCode " +
            " FROM MMaterial m " +
            " JOIN MMaterialByVendor mmbv " +
            " ON mmbv.systemUserCode = m.systemUserCode " +
            " AND mmbv.warehouseGroupCode = mmbv.warehouseGroupCode " +
            " AND mmbv.materialCode = m.materialCode " +
            " WHERE (:#{#search.materialCode} IS NULL OR LOWER( m.materialCode) LIKE CONCAT('%', LOWER(:#{#search.materialCode}), '%')) " +
            " AND (:#{#search.materialName} IS NULL OR LOWER(m.materialName) LIKE %:#{#search.materialName != null ? #search.materialName.toLowerCase() : ''}% ) " +
            " AND (:#{#search.supplierCode} IS NULL OR LOWER(m.supplierCode) LIKE CONCAT('%', LOWER(:#{#search.supplierCode}), '%' )) " +
            " AND (:#{#search.vendorCode} IS NULL OR LOWER(mmbv.materialVendorCode) LIKE CONCAT('%', LOWER(:#{#search.vendorCode}), '%')) ")
    List<Map<Object, Object>> findDynamicMaterial(MaterialSearchDto search, Pageable pageable);

    String ORDER_BY_FOLLOWER = "--\n";

    @Query(value = " WITH temp AS (SELECT * " +
            " FROM (SELECT * FROM m_material p ORDER BY" + ORDER_BY_FOLLOWER + " p.record_id ASC) a " +
            " ORDER BY" + ORDER_BY_FOLLOWER + " a.record_id ASC LIMIT 100 OFFSET 0) " +
            " SELECT m.material_code AS materialCodexx, " +
            "   m.material_name AS materialNamepp, " +
            "   m.supplier_code AS supplierCode, " +
            "   mmbv.material_vendor_code AS vendorCodeyy " +
            " FROM " +
            "   (SELECT * FROM temp p ORDER BY" + ORDER_BY_FOLLOWER + " p.record_id) AS m " +
            " JOIN m_material_by_vendor mmbv " +
            " ON mmbv.system_user_code = m.system_user_code " +
            " AND mmbv.warehouse_group_code = mmbv.warehouse_group_code " +
            " AND mmbv.material_code = m.material_code " +
            " WHERE (:#{#search.materialCode} IS NULL OR LOWER(m.material_code) LIKE CONCAT('%', LOWER(:#{#search.materialCode}), '%')) ",
            nativeQuery = true)
    Page<Map<String, Object>> findDynamicMaterialNativeWithSort(MaterialSearchDto search, Pageable pageable);

    @Query(value = " SELECT  " +
            "   m.materialCode AS materialCode, " +
            "   m.materialName AS materialName, " +
            "   m.supplierCode AS supplierCode, " +
            "   mmbv.materialVendorCode AS vendorCode," +
            "   m.updateDateTime AS updateDateTime, " +
            "   (SELECT m2.recordId FROM MMaterial m2 WHERE m2.recordId = m.recordId) AS recordId2 \n" +
            " FROM MMaterial m " +
            " JOIN MMaterialByVendor mmbv " +
            " ON mmbv.systemUserCode = m.systemUserCode " +
            " AND mmbv.warehouseGroupCode = mmbv.warehouseGroupCode " +
            " AND mmbv.materialCode = m.materialCode " +
            " WHERE TRUE AND (:#{#search.get('materialCode')} IS NULL OR LOWER( m.materialCode) LIKE CONCAT('%', LOWER(:#{#search.get('materialCode')}), '%')) ",
            countQuery = " SELECT COUNT(*) " +
                    " FROM MMaterial m " +
                    " JOIN MMaterialByVendor mmbv " +
                    " ON mmbv.systemUserCode = m.systemUserCode " +
                    " AND mmbv.warehouseGroupCode = mmbv.warehouseGroupCode " +
                    " AND mmbv.materialCode = m.materialCode " +
                    " WHERE TRUE AND (:#{#search.get('materialCode')} IS NULL OR LOWER( m.materialCode) LIKE CONCAT('%', LOWER(:#{#search.get('materialCode')}), '%')) ")
    Page<Map<String, Object>> findDynamicMaterialJpaWithSort(Map<String, Object> search, Pageable pageable);

    @Query(value = " SELECT mm.material_code AS materialCodeNative, " +
            " mm.material_name AS materialNameNative " +
            " FROM m_material mm " +
            " WHERE (mm.material_name) LIKE :param ", nativeQuery = true)
    List<Map<String, Object>> nativeQuery(String param);

    @Query(value = " SELECT mm.materialCode AS materialCodeJpa, " +
            " mm.materialName AS materialNameJpa " +
            " FROM MMaterial mm " +
            " WHERE (mm.materialName) LIKE :param ")
    List<Map<String, Object>> jpaQuery(String param);
}