package com.HelloRolha.HR.feature.approve.model.dto.approveLine;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ApproveLineRead {
    private Integer confirmerId;
    private String confirmerName;
    private Integer approveId;
    private String comment;
    private LocalDateTime approveTime;
    private LocalDateTime applyTime;
    private Integer status;

}
