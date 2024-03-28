package com.HelloRolha.HR.feature.approve.model.dto.approveLine;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApproveLineConfirm {
    private Integer id;
    private Integer confirmerId;
    private Integer approveId;
    private String comment;

}