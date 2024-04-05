package com.HelloRolha.HR.feature.overtime.model;

import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Overtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String date;
    private String shift;
    private String startTime;
    private String endTime;
    private String sumTime;
    private String reason;
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;


}
