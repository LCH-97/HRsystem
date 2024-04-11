package com.example.demo.batchTest.Overtime.model;

import com.example.demo.batchTest.Employee.model.BaseEntity;
import com.example.demo.batchTest.Employee.model.Employee;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Overtime extends BaseEntity{

    private String date;
    private String shift;
    private String startTime;
    private String endTime;
    private Long sumTime;
    private String reason;
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
