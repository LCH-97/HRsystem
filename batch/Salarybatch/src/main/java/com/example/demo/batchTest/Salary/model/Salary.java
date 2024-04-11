package com.example.demo.batchTest.Salary.model;

import com.example.demo.batchTest.Employee.model.BaseEntity;
import com.example.demo.batchTest.Employee.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert ////ToDo
public class Salary extends BaseEntity {

    private LocalDate salaryDate; // 월급날 배치처리 한 날
    private Long commuteCount; // 전 월 출근 시간
    private Long paidVacationCount; //전 월 휴가 일수
    private Long overTime; // 전 월 초과근무 시간
    private Long totalSalary;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public void calculateTotalSalary() {
        // 총 근무 일을 구하자
        this.totalSalary= 0L;
        Long salaryHourly = employee.getSalary()/209;

        //Duration duration = Duration.between(startDate,endDate);

        Long workHour = 200L; //Todo 주말 빼는 로직 추가해야함.
        // 주말 빼고 ...
        // 휴무일 빼고 ...
        // 주말 근무는 또 뭐하고...
        // 근무 시간이 충분하지 않다면?

        //근무 시간 + 유급 휴가날 * 8 이 일해야하는 시간보다 적다면?
        try{
            if((commuteCount/60) +paidVacationCount*8 <  workHour){
                // 월급 삭감.
                this.totalSalary += salaryHourly * ((commuteCount/60) +paidVacationCount*8);
            }
            //시급에 맞게 계산
            this.totalSalary += salaryHourly * workHour;
        }catch (NullPointerException e){
            if(commuteCount == null) this.commuteCount = 0L;
            if(paidVacationCount == null) this.paidVacationCount = 0L;
        }
//

        // 초과 근무 시간에 대해 1.5배의 월급을 준다.
        //employee.getSalary() 는 월급이기 때문에 시간 단위로 해야한다.
        // 하루 8시간 주 40 근로를 한다면, 월 209시간 근무를 한다고 가정한다.
        try{
            this.totalSalary += salaryHourly * 15 / 10 * this.overTime;
        }catch (NullPointerException e){
            this.overTime = 0L;
        }

    }
}