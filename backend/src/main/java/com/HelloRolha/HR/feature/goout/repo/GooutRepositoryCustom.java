package com.HelloRolha.HR.feature.goout.repo;

import com.HelloRolha.HR.feature.goout.model.Goout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GooutRepositoryCustom {
    public Page<Goout> findList(Pageable pageable);
    Optional<Goout> findByIdWithDetails(Integer id);
}
