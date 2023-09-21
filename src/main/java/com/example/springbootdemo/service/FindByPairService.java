package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.MMaterialDto;
import com.example.springbootdemo.entity.MMaterial;
import com.example.springbootdemo.utils.MyPair;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.AvailableHints;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FindByPairService {

    final EntityManager entityManager;
    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final JdbcTemplate jdbcTemplate;

    public Object getMaterialJpa(List<MyPair<String, String>> pairs) {
        String sql = " SELECT new com.example.springbootdemo.service.MaterialDto" +
                " ( mm.materialCode, mm.materialName, mm.supplierCode) FROM MMaterial mm " +
                " WHERE (mm.materialCode, mm.supplierCode) IN (('M0001','S222'),('M0002','S0001')) ";

        TypedQuery<MMaterialDto> query = entityManager.createQuery(sql, MMaterialDto.class);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        query.setHint(AvailableHints.HINT_SPEC_LOCK_TIMEOUT, 0);
        return query.getResultList();
    }

    public Object getMaterialJdbc(List<MyPair<String, String>> pairs) {

        String sql = " SELECT mm.material_code as materialCode, " +
                " mm.material_name as materialName, " +
                " mm.supplier_code as supplierCode," +
                " FROM m_material mm " +
                " WHERE (mm.material_code, mm.supplier_code) IN (('M0001','S222'),('M0002','S0001')) ";

        Query query = entityManager.createNativeQuery(sql, MMaterialDto.class);
        List list = query.getResultList();

//        List<MaterialDto> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(MaterialDto.class));
//        List<MaterialDto> list = namedParameterJdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(MaterialDto.class));

        return list;
    }

    public Object getRowNum() {

        String sql = "SELECT *," +
                " ROW_NUMBER() OVER(PARTITION BY mm.supplier_code ORDER BY mm.material_code ) AS rn " +
                " FROM m_material mm ";

        Query query = entityManager.createNativeQuery(sql);
        List list = query.getResultList();

//        List<MaterialDto> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(MaterialDto.class));
//        List<MaterialDto> list = namedParameterJdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(MaterialDto.class));

        return list;
    }

    public Object getRowNumJpa() {
        String sql = " SELECT new com.example.springbootdemo.service.MaterialDtoRn" +
                " ( mm.materialCode, mm.materialName, mm.supplierCode, " +
                " ROW_NUMBER() OVER(PARTITION BY mm.supplierCode ORDER BY mm.materialCode ) )  " +
                " FROM MMaterial mm ";

        TypedQuery<MMaterialDto> query = entityManager.createQuery(sql, MMaterialDto.class);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        query.setHint(AvailableHints.HINT_SPEC_LOCK_TIMEOUT, 0);
        return query.getResultList();
    }

    public Object getSubQueryJpa() {
        String sql = " SELECT new com.example.springbootdemo.service.MaterialDtoVendor" +
                " ( mmbv.materialCode, mm.materialName, COALESCE(mm.supplierCode,  mm.materialName, 'papapapaa'), " +
                " ROW_NUMBER() OVER(PARTITION BY mm.supplierCode ORDER BY mm.materialCode )," +
                " mmbv.materialVendorCode )  " +
                " FROM " +
                "   (SELECT m.materialCode AS materialCode, m5.materialName AS materialName, m.supplierCode AS supplierCode " +
                "   FROM MMaterial m " +
                "   JOIN MMaterial m5 ON m5.materialCode = m.materialCode) mm " +
                " JOIN MMaterialByVendor mmbv ON mmbv.materialCode = mm.materialCode";

        TypedQuery<MaterialDtoVendor> query = entityManager.createQuery(sql, MaterialDtoVendor.class);
        query.setMaxResults(50);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        query.setHint(AvailableHints.HINT_SPEC_LOCK_TIMEOUT, 0);

        return query.getResultList();
    }

}

@Data
@NoArgsConstructor
class MaterialDtoVendor {
    private String materialCode;
    private String materialName;
    private String supplierCode;
    private Object rn;
    private String vendor;

    public MaterialDtoVendor(String materialCode, String materialName, String supplierCode, Object rn, String vendor) {
        this.materialCode = materialCode;
        this.materialName = materialName;
        this.supplierCode = supplierCode;
        this.rn = rn;
        this.vendor = vendor;
    }
}

@Data
@NoArgsConstructor
class MaterialDtoRn {
    private String materialCode;
    private String materialName;
    private String supplierCode;
    private Long rn;

    public MaterialDtoRn(String materialCode, String materialName, String supplierCode, Object rn) {
        this.materialCode = materialCode;
        this.materialName = materialName;
        this.supplierCode = supplierCode;
        this.rn = (Long) rn;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class MaterialDto {
    private String materialCode;
    private String materialName;
    private String supplierCode;
}
