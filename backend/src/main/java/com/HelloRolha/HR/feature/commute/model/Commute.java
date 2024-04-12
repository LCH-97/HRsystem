package com.HelloRolha.HR.feature.commute.model;




import com.HelloRolha.HR.common.entity.BaseEntity;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Commute extends BaseEntity {
    private Long sumTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Boolean isLeave(){
        return this.sumTime != null;
    }
    public String getEndTime(){
        if(isLeave())
            return super.getUpdateAt().toString();
        else return "아직 근무 중입니다.";
    }

    public String getSumTimeByHours() {
        Duration duration = Duration.between(super.getUpdateAt(), LocalDateTime.now());
        long totalMinutes = duration.toMinutes();
        if(isLeave()){
            totalMinutes = sumTime;
        }
        long hours = totalMinutes / 60; // 시간 계산
        long minutes = totalMinutes % 60; // 분 계산
        return  String.format("%d:%d", hours, minutes);
    }
}

