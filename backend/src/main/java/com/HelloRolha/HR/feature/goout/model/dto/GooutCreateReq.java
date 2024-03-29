package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GooutCreateReq {
    private Integer agentId;
    private Integer employeeId;
//    private Integer writerId;
    private Integer gooutTypeId;
    private LocalDate first;
    private LocalDate last;
    private Integer confirmer1Id;
    private Integer confirmer2Id;
}

