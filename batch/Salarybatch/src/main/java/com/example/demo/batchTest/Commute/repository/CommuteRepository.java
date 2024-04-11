package com.example.demo.batchTest.Commute.repository;

import com.example.demo.batchTest.Commute.model.Commute;
import com.example.demo.batchTest.Commute.model.dto.CommuteDto;
import com.example.demo.batchTest.Employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommuteRepository extends JpaRepository<Commute, Integer> {
    //    List<Commute> findAllByEmployee(Employee employee);
    @Query(
            "SELECT c.employee.id, SUM(c.sumTime) " +
                    "FROM Commute c " +
                    "GROUP BY c.employee"
    )
    List<CommuteDto> sumTimeCommuteDtoJPQL();
}
