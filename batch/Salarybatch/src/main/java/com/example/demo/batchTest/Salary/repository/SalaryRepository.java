package com.example.demo.batchTest.Salary.repository;


import com.example.demo.batchTest.Salary.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    Optional<Salary> findByEmployeeId(Long i);
}
