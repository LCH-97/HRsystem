package com.HelloRolha.HR.feature.approve.repo;

import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApproveRepository extends JpaRepository<Approve, Integer> {
    List<Approve> findAllByEmployee(Employee employee);
}
