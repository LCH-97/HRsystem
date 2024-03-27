package com.HelloRolha.HR.feature.approve.model.dto.Approve;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApproveCreateReq {
    private String title;
    private String content;
    private Integer employeeId;
    private Integer confirmer1Id;
    private Integer confirmer2Id;

}