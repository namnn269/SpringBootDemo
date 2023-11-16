package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.MMaterial;
import com.example.springbootdemo.repository.MMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;

@Service
public class DuplicateKeyService {
    @Autowired
    MMaterialRepository materialRepo;

    @Transactional
    public Object updateDuplicateKey(Integer id, String materialCode) {
        Optional<MMaterial> byId = materialRepo.findById(id);
        if (byId.isEmpty()) return null;
        MMaterial material = byId.get();
        long l = materialRepo.countBySystemUserCodeAndMaterialCode("01", materialCode);
        material.setMaterialCode(materialCode);
        materialRepo.save(material);
        l = materialRepo.countBySystemUserCodeAndMaterialCode("01", materialCode); // duplicate key => can NOT count
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return material;
    }
}
