package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity.MMaterialByVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MMaterialByVendorRepository extends JpaRepository<MMaterialByVendor, Integer> {
}
