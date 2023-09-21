package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
