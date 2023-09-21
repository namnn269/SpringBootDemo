package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.Employee;
import com.example.springbootdemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Transactional(value = "demoTransaction",
            rollbackFor = {RuntimeException.class, HttpClientErrorException.NotAcceptable.class},
            noRollbackFor = {Exception.class},
            isolation = Isolation.READ_COMMITTED)
    Optional<Employee> save(Employee employee) {

        return Optional.of(employeeRepo.save(employee));
    }

    @Scheduled(cron = "0 47 8 * * *", zone = "GMT+7")
    public void update() {
        System.out.println("============ 0000 =============");
//        Employee employee = employeeRepo.findById(1).get();
//        employee.setName("employee 1 -fix 1");
//        employeeRepo.save(employee);
    }

}
