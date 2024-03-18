package com.HelloRolha.HR.feature.approve.model.dto.ApproveLine;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class ApproveLineUpdateReq {
    private Integer confirmer1Id;
    private Integer confirmer2Id;
    private Integer approveId;
}
