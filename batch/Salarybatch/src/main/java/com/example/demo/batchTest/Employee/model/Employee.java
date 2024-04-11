package com.example.demo.batchTest.Employee.model;

import com.example.demo.batchTest.Commute.model.Commute;
import com.example.demo.batchTest.Goout.model.Goout;
import com.example.demo.batchTest.Overtime.model.Overtime;
import com.example.demo.batchTest.Salary.model.Salary;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert ////ToDo
public class Employee extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    //    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ROLE_NEW'")
    @ColumnDefault("'ROLE_NEW'")
    private String authority;
    private LocalDate employmentDate; // 입사일
    private LocalDate quitDate; // 퇴사일
    @ColumnDefault("false")
    private Boolean status;

    //details
    private String name;
    private String phoneNum;
    private String birth;
    private String address;
    private Integer age;
    @ColumnDefault("5000000")
    private Long salary;


    @OneToMany(mappedBy = "employee")
    private List<Salary> salaryList = new ArrayList<>();

    // 사용 기능들
    @OneToMany(mappedBy = "employee")
    private List<Goout> goouts = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<Commute> commutes = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<Overtime> overtimes = new ArrayList<>();



}