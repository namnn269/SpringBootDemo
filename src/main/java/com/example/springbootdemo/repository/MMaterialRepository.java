package com.example.springbootdemo.repository;

import com.example.springbootdemo.dto.MaterialDto_1;
import com.example.springbootdemo.dto.MaterialDto_2;
import com.example.springbootdemo.dto.MaterialSearchDto;
import com.example.springbootdemo.entity.MMaterial;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
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
}















