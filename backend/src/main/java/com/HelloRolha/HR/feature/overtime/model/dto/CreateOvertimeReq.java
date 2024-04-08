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
    private String reason;

    public Long calculateOvertime(){
        String[] start = this.startTime.split(":");
        String[] end = this.endTime.split(":");
        Long hour = Long.parseLong(end[0]) - Long.parseLong(start[0]);
        Long min = Long.parseLong(end[1]) - Long.parseLong(start[1]);
        return hour*60+min;
    }
}
