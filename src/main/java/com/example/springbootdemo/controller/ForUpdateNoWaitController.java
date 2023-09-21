package com.example.springbootdemo.controller;

import com.example.springbootdemo.entity.MMaterial;
import com.example.springbootdemo.repository.MMaterialRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/for-update-nowait")
@Transactional
public class ForUpdateNoWaitController {

    @Autowired
    private MMaterialRepository materialRepo;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PatchMapping(value = "/for-share")
    public Object update(@RequestBody List<Integer> ids) {
        List<MMaterial> materials = null;
        try {
            materials = materialRepo.findForShareByRecordIdIn(ids);
            Thread.sleep(3000);
            materials.get(0).setMaterialName("material update for share 1");
            materialRepo.saveAll(materials);
        } catch (PessimisticLockingFailureException ex) {
            System.out.println("============ no wait");
            throw ex;
        } catch (NullPointerException ex) {
            System.out.println("=========== null pointer");
        } catch (JpaSystemException ex) {
            System.out.println("=============" + ex.getClass());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println(" ============= " + ex.getClass());
            ex.printStackTrace();
        }
        return materials;
    }

    @PatchMapping(value = "/for-share-2")
    public Object update3(@RequestBody List<Integer> ids) {
        List<MMaterial> materials = null;
        try {
            materials = materialRepo.findForShareByRecordIdIn(ids);
            Thread.sleep(3000);
            materials.get(0).setMaterialName("material update for share 2");
            materialRepo.saveAll(materials);
        } catch (PessimisticLockingFailureException ex) {
            System.out.println("============ no wait");
            throw ex;
        } catch (NullPointerException ex) {
            System.out.println("=========== null pointer");
        } catch (JpaSystemException ex) {
            System.out.println("=============" + ex.getClass());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println(" ============= " + ex.getClass());
            ex.printStackTrace();
        }
        return materials;
    }

    @PatchMapping(value = "/for-update")
    public Object update2(@RequestBody List<Integer> ids) {
        List<MMaterial> materials = null;
        try {
            materials = materialRepo.findForUpdateByRecordIdIn(ids);
            Thread.sleep(3000);
            materials.get(0).setMaterialName("material update for update");
            materialRepo.saveAll(materials);
        } catch (PessimisticLockingFailureException ex) {
            System.out.println("========== no wait");
            throw ex;
        } catch (NullPointerException ex) {
            System.out.println("=========== null pointer");
        } catch (JpaSystemException ex) {
            System.out.println("=============" + ex.getClass());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println(" ============= " + ex.getClass());
            ex.printStackTrace();
        }
        return materials;
    }

    @PatchMapping(value = "/native-query")
    @Transactional
    public Object update4(@RequestBody List<Integer> ids) throws InterruptedException {
        EntityManager entityManagerForUpdate = entityManager;
        try {

            String sql = " SELECT * " +
                    " FROM m_material mm " +
                    " WHERE mm.record_id IN (:ids) FOR UPDATE NOWAIT ";
            Query query = entityManagerForUpdate.createNativeQuery(sql, MMaterial.class);
            query.setParameter("ids", ids);
            List<MMaterial> materials = query.getResultList();
//            List<MMaterial> materials = materialRepo.selectMaterialForUpdate(ids);
            Thread.sleep(5000);
            materials.forEach(material -> material.setMaterialShort("native query " + new Random().nextInt()));
//            for (MMaterial material : materials) {
//                entityManagerForUpdate.persist(material);
//            }
            materialRepo.saveAll(materials);
            return materials;
        } catch (Exception ex) {
            System.out.println("transaction will be rollback: " + ex.getClass());
            throw new UnexpectedRollbackException("throw from native query controller, LOL");
        }
    }

    @PatchMapping(value = "/native-query-dto")
    @Transactional
    public Object update5(@RequestBody List<Integer> ids) throws InterruptedException {
        EntityManager entityManagerForUpdate = entityManager;
        try {

            String sql = " SELECT * " +
                    " FROM m_material mm " +
                    " WHERE mm.record_id IN (:ids) FOR UPDATE NOWAIT ";
            Map<String, Object> params = new HashMap<>();
            params.put("ids", ids);
            List<MMaterial> materials = namedParameterJdbcTemplate.query
                    (sql, params, BeanPropertyRowMapper.newInstance(MMaterial.class));
//            List<Map<String, Object>> materials = namedParameterJdbcTemplate.queryForList(sql, params);
            materials.forEach(material -> material.setMaterialShort("native dto " + new Random().nextInt()));
            materialRepo.saveAll(materials);
            Thread.sleep(5000);
            return materials;
        } catch (Exception ex) {
            System.out.println("transaction will be rollback: " + ex.getClass());
            throw new UnexpectedRollbackException("throw from native query controller, LOL");
        }
    }

}
