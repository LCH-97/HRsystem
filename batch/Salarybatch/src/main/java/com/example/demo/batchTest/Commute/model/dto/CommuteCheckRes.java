package com.example.demo.batchTest.Commute.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommuteCheckRes {
    private Integer id;
    private String startTime;
    private String endTime;
    private String sumTime;
    private Boolean isCommute;
    private Boolean isLeave;

}