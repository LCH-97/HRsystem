package com.HelloRolha.HR.feature.goout.repo;

import com.HelloRolha.HR.feature.goout.model.GooutLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GooutLineRepository extends JpaRepository<GooutLine, Integer> {
    List<GooutLine> findByGooutId(Integer gooutId);
}