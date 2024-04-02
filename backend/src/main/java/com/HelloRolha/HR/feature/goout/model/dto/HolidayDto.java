package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class HolidayDto {
    private LocalDate date;
    private String description;
}
