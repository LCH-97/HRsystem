package com.HelloRolha.HR.feature.approve.repo;

import com.HelloRolha.HR.feature.approve.model.ApproveLine;
import com.HelloRolha.HR.feature.goout.model.GooutLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApproveLineRepository extends JpaRepository<ApproveLine, Integer> {
    Optional<ApproveLine> findByApproveId(Integer id);
}
