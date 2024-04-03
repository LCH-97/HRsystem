package com.HelloRolha.HR.feature.goout.repo;

import com.HelloRolha.HR.feature.goout.model.GooutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GooutTypeRepository extends JpaRepository<GooutType, Integer> {
    @Query("SELECT g.maxHoliday FROM GooutType g WHERE g.id = :gooutTypeId")
    int findMaxHolidayByGooutTypeId(@Param("gooutTypeId") Integer gooutTypeId);
}
