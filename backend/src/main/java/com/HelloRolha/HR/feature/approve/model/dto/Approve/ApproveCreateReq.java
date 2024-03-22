package com.HelloRolha.HR.feature.approve.model.dto.Approve;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Data
public class ApproveCreateReq {
    private String title;
    private String content;
    private Integer employeeId;

}