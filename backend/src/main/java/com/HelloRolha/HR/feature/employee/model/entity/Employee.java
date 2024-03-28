package com.HelloRolha.HR.feature.employee.model.entity;
import com.HelloRolha.HR.common.entity.BaseEntity;
import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.commute.model.Commute;
import com.HelloRolha.HR.feature.department.model.entity.Department;
import com.HelloRolha.HR.feature.employee.model.dto.EmployeeDto;
import com.HelloRolha.HR.feature.goout.model.Goout;

import com.HelloRolha.HR.feature.overtime.model.Overtime;
import com.HelloRolha.HR.feature.position.model.entity.Position;


import com.HelloRolha.HR.feature.salary.model.entity.Salary;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
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
public class Employee extends BaseEntity implements UserDetails {
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

    //외래키
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Department_id")
    private Department department;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Position_id")
    private Position position;


    @OneToMany(mappedBy = "employee")
    private List<Salary> salaryList = new ArrayList<>();

    // 사용 기능들
    @OneToMany(mappedBy = "employee")
    private List<Goout> goouts = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<Approve> approves = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<Commute> commutes = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<Overtime> overtimes = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> this.authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
    //

    public Long getWorkTimeByMinutes(LocalDate startDate,LocalDate endDate) {
        Long counter = 0L;
        //Todo 비효율적인 쿼리임. 바꿀 수 있으면 바꾸자.
        //sql 문을 month 에 맞는 데이터만 가져오도록 만들 수 있다.

        if(this.commutes.isEmpty()){

            System.out.println("오류 예외 처리해라");
            return counter;
        }

        for (Commute commute:this.commutes){
            if(startDate.isBefore(commute.getCreateAt().toLocalDate())  && endDate.isAfter(commute.getCreateAt().toLocalDate())){
                String hour = commute.getSumTime().split(":")[0];
                String min = commute.getSumTime().split(":")[1];

                // 하루 일한 총 시간 - 휴식 시간해야됨
//                long totalMinutes = duration.toMinutes();
                // 만약 8시간이 넘어간다면?

                counter += Integer.parseInt(hour)*60 + Integer.parseInt(min);
            }

        }
        return counter;
    }

    @Transactional
    public Integer getPaidVacationCount(LocalDate startDate, LocalDate endDate) {
        Integer counter = 0;
        //Todo 비효율적인 쿼리임. 바꿀 수 있으면 바꾸자.
        //sql 문을 month 에 맞는 데이터만 가져오도록 만들 수 있다.

        if(this.goouts.isEmpty()){

            System.out.println("오류 예외 처리해라");
            return 0;
        }

        for (Goout goout:this.goouts){
            //휴가 출발일부터 마지막 날까지 반복,  월급을 계산하는 날 사이에 있다면
            for(LocalDate date = goout.getFirst(); !date.isEqual(goout.getLast()) ;date=date.plusDays(1)){
                if( date.isAfter(startDate) && date.isBefore(endDate)){
                    counter++;
                }
            }
        }
        return counter;
    }


}
