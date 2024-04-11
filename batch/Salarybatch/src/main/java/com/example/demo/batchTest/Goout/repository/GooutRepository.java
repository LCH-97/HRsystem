package com.example.demo.batchTest.Goout.repository;

import com.example.demo.batchTest.Employee.model.Employee;
import com.example.demo.batchTest.Goout.model.Goout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GooutRepository extends JpaRepository<Goout, Integer> {
    List<Goout> findAllByEmployee(Employee employee);
//    List<Goout> findByEmployeeIdAndGooutTypeIdAndStatusAndFirstBetween(Integer employeeId, Integer gooutTypeId, Integer status, LocalDate startOfYear, LocalDate endOfYear);

}
