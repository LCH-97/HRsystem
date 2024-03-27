package com.HelloRolha.HR.feature.goout.repo;

import com.HelloRolha.HR.feature.goout.model.Goout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface GooutRepositoryCustum {
    public Page<Goout> findList(Pageable pageable);
    Optional<Goout> findByIdWithDetails(Integer id);
}
