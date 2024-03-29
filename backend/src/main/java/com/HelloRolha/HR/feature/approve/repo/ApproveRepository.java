package com.HelloRolha.HR.feature.approve.repo;

import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.goout.model.Goout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApproveRepository extends JpaRepository<Approve, Integer>, ApproveRepositoryCustom {
    List<Approve> findAllByEmployee(Employee employee);
    @Query("SELECT a FROM Approve a ORDER BY a.createAt DESC")
    Page<Approve> findAllByOrderByCreateAtDesc(Pageable pageable);

}