package com.HelloRolha.HR.feature.overtime.repository;

import com.HelloRolha.HR.feature.overtime.model.Overtime;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OvertimeRepositoryCustom {
  public List<Overtime> findList();


  Optional<Overtime> findByIdWithDetails(Integer id);
}