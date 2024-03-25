
package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class GooutRead {
    private Integer id;
    private Integer agentId;
    private String agentName;
    private Integer employeeId;
    private String employeeName;
    private Integer writerId;
    private String writerName;
    private Integer gooutTypeId;
    private String gooutTypeName;
    private Integer status;
    private LocalDate first;
    private LocalDate last;
}
