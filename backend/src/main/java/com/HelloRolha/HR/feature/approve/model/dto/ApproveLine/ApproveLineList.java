package com.HelloRolha.HR.feature.approve.model.dto.ApproveLine;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApproveLineList {
    private Integer id;
    private String confirmerName;
    private Integer confirmerId;
    private Integer approveId;
    private Integer status;
    private String comment;

}
