package com.HelloRolha.HR.feature.approve.model.dto.ApproveLine;

import lombok.Builder;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

@Data
@Builder
public class ApproveConfirmRes {
    private Integer status;
}
