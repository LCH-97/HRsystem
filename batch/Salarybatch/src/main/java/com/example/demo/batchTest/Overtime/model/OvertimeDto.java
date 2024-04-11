package com.example.demo.batchTest.Overtime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OvertimeDto {
    private Integer employeeId;
    private Long totalSumTime;
}
