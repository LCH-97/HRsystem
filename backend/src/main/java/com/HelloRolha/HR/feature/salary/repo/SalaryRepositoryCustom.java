package com.HelloRolha.HR.feature.salary.repo;

import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.salary.model.entity.Salary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryRepositoryCustom {
    public List<Salary> readSalaryListBetween(LocalDate startDate, LocalDate endDate);

    public List<Salary> readFirstDateOfSalary();
    public List<Salary> readLastDateOfSalary();

}
