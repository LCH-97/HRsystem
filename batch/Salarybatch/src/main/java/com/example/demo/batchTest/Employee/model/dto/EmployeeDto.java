package com.example.demo.batchTest.Employee.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {
    private Long id;
    private String name;
    private LocalDate employmentDate;
    private String department;
    private String position;
    private Long salary;
}



