package com.HelloRolha.HR.feature.salary.repo;

import com.HelloRolha.HR.error.BusinessException;
import com.HelloRolha.HR.error.ErrorCode;
import com.HelloRolha.HR.feature.department.model.entity.QDepartment;
import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
import com.HelloRolha.HR.feature.position.model.entity.QPosition;
import com.HelloRolha.HR.feature.salary.model.entity.QSalary;
import com.HelloRolha.HR.feature.salary.model.entity.Salary;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

public class SalaryRepositoryCustomImpl extends QuerydslRepositorySupport implements SalaryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public SalaryRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Salary.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Salary> readSalaryListBetween(LocalDate startDate,LocalDate endDate) {

        QSalary salary = new QSalary("salary");
        QEmployee employee = new QEmployee("employee");
        JPAQuery<Salary> query = queryFactory
                .select(salary)
                .from(salary)
                .innerJoin(salary.employee, employee).fetchJoin()
                .where(salary.salaryDate.between(startDate, endDate));


        // 쿼리 실행
        List<Salary> result = query.fetch();
        return result;
    }

    @Override
    public LocalDate readFirstDateOfSalary() {
        QSalary salary = new QSalary("salary");
        JPAQuery<Salary> query = queryFactory
                .select(salary)
                .from(salary)
                .orderBy(salary.salaryDate.asc())
                .limit(1);

        // 쿼리 실행
        List<Salary> orders = query.fetch();
        try{
            return orders.get(0).getSalaryDate();
        }
        catch (IndexOutOfBoundsException e){
            throw new BusinessException(ErrorCode.DB_NULL,"salary table is empty");
        }

    }

    @Override
    public LocalDate readLastDateOfSalary() {
        QSalary salary = new QSalary("salary");
        JPAQuery<Salary> query = queryFactory
                .select(salary)
                .from(salary)
                .orderBy(salary.salaryDate.desc())
                .limit(1);

        // 쿼리 실행
        List<Salary> orders = query.fetch();
        return orders.get(0).getSalaryDate();
    }

//    @Override
//    public List<Salary> lastDateOfSalaryList() {
//        QSalary salary = new QSalary("salary");
//        return queryFactory.selectFrom(salary)
//                .where(salary.createAt.month().eq(
//                        queryFactory.select(salary.createAt.month())
//                                .from(salary)
//                                .orderBy(salary.createAt.desc())
//                                .limit(1)
//                ))
//                .fetch();
//
//    }

    public List<Salary> getAllSalaryList(){
        QSalary salary = new QSalary("salary");
        QEmployee employee = new QEmployee("employee");
        QDepartment department = new QDepartment("department");
        QPosition position = new QPosition("position");
        JPAQuery<Salary> query = queryFactory
                .select(salary)
                .from(salary)
                .innerJoin(salary.employee, employee).fetchJoin()
                .innerJoin(employee.department, department).fetchJoin()
                .innerJoin(employee.position, position).fetchJoin();


        // 쿼리 실행
        return query.fetch();
    }
}
