package com.example.demo.batchTest.Commute.model.dto;

import com.example.demo.batchTest.Employee.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommuteDto {
    private Integer employeeId;
    private Long totalSumTime;
}

