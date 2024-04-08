package com.HelloRolha.HR.feature.goout.repo;

import com.HelloRolha.HR.feature.goout.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
    boolean existsByDate(LocalDate date);
}