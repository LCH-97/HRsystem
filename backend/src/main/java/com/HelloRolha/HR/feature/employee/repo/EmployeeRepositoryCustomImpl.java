package com.HelloRolha.HR.feature.employee.repo;

import com.HelloRolha.HR.feature.commute.model.QCommute;
import com.HelloRolha.HR.feature.department.model.entity.QDepartment;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.goout.model.QGoout;
import com.HelloRolha.HR.feature.goout.model.QGooutFile;
import com.HelloRolha.HR.feature.goout.model.QGooutType;
import com.HelloRolha.HR.feature.overtime.model.QOvertime;
import com.HelloRolha.HR.feature.position.model.entity.QPosition;
import com.HelloRolha.HR.feature.salary.model.entity.Salary;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeRepositoryCustomImpl extends QuerydslRepositorySupport implements EmployeeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public EmployeeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Employee.class);
        this.queryFactory = queryFactory;
    }

    //모든 직원의 리스트를 가져온다.
    @Override
    public List<Employee> getAllEmployList() { //퇴직한 사람도 나옴.

        // 조인이 필요한 각 클래스들에 대한 객체 생성
        QDepartment department = new QDepartment("department");
        QPosition position = new QPosition("position");
        QEmployee employee = new QEmployee("employee");

        // QueryDSL 을 사용하기 위한 from 메서드 작성
        JPAQuery<Employee> query = queryFactory.select(employee).from(employee)
                .innerJoin(employee.department, department).fetchJoin()
                .innerJoin(employee.position, position).fetchJoin()
                .distinct();// 중복제거를 위한 코드 추가
                //.where(employee.status); // true면 일하고 있는 직원

        return query.fetch();
    }
    //
//    @Override
//    public List<Employee> getWorkingEmployListForCalculateSalary() { //현재 일하고 있는 사람들만
//
//        // 조인이 필요한 각 클래스들에 대한 객체 생성
//        QGoout goout = new QGoout("goout");
//        QOvertime overtime = new QOvertime("overtime");
//        QEmployee employee = new QEmployee("employee");
//        QCommute commute = new QCommute("commute");
//        // QueryDSL 을 사용하기 위한 from 메서드 작성
//        JPAQuery<Employee> query = queryFactory.select(employee).from(employee)
//                .innerJoin(employee.commutes, commute).fetchJoin()
//                .innerJoin(employee.overtimes, overtime).fetchJoin()
//                .innerJoin(employee.goouts, goout).fetchJoin();
//
//  // onetoMany 관계 여러개를 join 할 수 없음. 못 쓰는 쿼리문
//
//        return query.fetch();
//    }
}
