package com.example.demo.batchTest.Overtime.repository;

import com.example.demo.batchTest.Employee.model.Employee;
import com.example.demo.batchTest.Overtime.model.Overtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OvertimeRepository extends JpaRepository<Overtime, Integer> {
    List<Overtime> findAllByEmployee(Employee employee);
}