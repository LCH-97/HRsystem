package com.HelloRolha.HR.feature.approve.model.dto.approveLine;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class ApproveLineUpdateReq {
    private Integer confirmerId;
    private Integer Id;

}