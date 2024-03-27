package com.HelloRolha.HR.feature.approve.model.dto.Approve;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Data
@Getter
@Setter
public class ApproveRead {
    private Integer id;
    private String title;
    private String content;
    private Integer status;
    private String employeeName;
    private Integer employeeId;
    private LocalDateTime createAt;
}