package com.HelloRolha.HR.feature.approve.model.dto.Approve;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApproveCreateRes {
    private Integer approveId;
    private Integer approveLine1Id;
    private Integer approveLine2Id;

}
