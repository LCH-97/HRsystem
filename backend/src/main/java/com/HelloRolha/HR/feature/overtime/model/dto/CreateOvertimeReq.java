package com.HelloRolha.HR.feature.overtime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOvertimeReq {
    private String date;
    private String shift;
    private String startTime;
    private String endTime;
    private String sumTime;
    private String reason;
}
