package com.example.demo.batchTest.Employee.repository;



import com.example.demo.batchTest.Employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByName(String name);

//    List<Employee> findAllByStatus(boolean b);

//    List<Employee> findALLByAuthority(String userAdmin);
}
