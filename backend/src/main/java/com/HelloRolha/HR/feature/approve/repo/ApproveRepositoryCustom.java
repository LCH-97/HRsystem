package com.HelloRolha.HR.feature.approve.repo;

import com.HelloRolha.HR.feature.approve.model.Approve;


import java.util.List;
import java.util.Optional;

public interface ApproveRepositoryCustom {

    public List<Approve> findList();

    Optional<Approve> findByIdWithDetails(Integer id);


    org.springframework.data.domain.Page<Approve> findList(org.springframework.data.domain.Pageable pageable);
}
