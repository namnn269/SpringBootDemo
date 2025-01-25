package com.example.springbootdemo.infrastructure.database.logicom.repository;

import com.example.springbootdemo.infrastructure.database.logicom.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
