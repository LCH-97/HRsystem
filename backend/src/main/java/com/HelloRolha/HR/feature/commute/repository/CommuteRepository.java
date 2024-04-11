package com.HelloRolha.HR.feature.commute.repository;


import com.HelloRolha.HR.feature.commute.model.Commute;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CommuteRepository extends JpaRepository<Commute, Integer> {
    List<Commute> findAllByEmployee(Employee employee);
    @Query("SELECT c FROM Commute c WHERE c.employee = :employee AND DATE(c.createAt) = CURRENT_DATE()")
    Optional<Commute> findByEmployee(@Param("employee") Employee employee);
}
