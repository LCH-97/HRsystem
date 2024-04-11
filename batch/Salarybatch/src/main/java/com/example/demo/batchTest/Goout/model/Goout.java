package com.example.demo.batchTest.Goout.model;

import com.example.demo.batchTest.Employee.model.BaseEntity;
import com.example.demo.batchTest.Employee.model.Employee;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Goout  extends BaseEntity {
    private Integer status;
    private LocalDate first;
    private LocalDate last;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

}