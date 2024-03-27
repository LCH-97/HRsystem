package com.HelloRolha.HR.feature.salary.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class getSalaryListRes {

    private Integer month;
    private List<SalaryDto> salaryDtoList;

}
