package com.HelloRolha.HR.feature.approve.model.dto.approve;

import lombok.*;

@Builder
@Data
public class ApproveUpdate {
    private Integer id;
    private String content;
    private Integer employeeId;
}