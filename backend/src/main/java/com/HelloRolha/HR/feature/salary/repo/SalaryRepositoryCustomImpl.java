package com.HelloRolha.HR.feature.salary.repo;

import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
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
    public List<Salary> readFirstDateOfSalary() {
        QSalary salary = new QSalary("salary");
        JPAQuery<Salary> query = queryFactory
                .select(salary)
                .from(salary)
                .orderBy(salary.salaryDate.asc())
                .limit(1);

        // 쿼리 실행
        List<Salary> orders = query.fetch();
        return orders;
    }

    @Override
    public List<Salary> readLastDateOfSalary() {
        QSalary salary = new QSalary("salary");
        JPAQuery<Salary> query = queryFactory
                .select(salary)
                .from(salary)
                .orderBy(salary.salaryDate.desc())
                .limit(1);

        // 쿼리 실행
        List<Salary> orders = query.fetch();
        return orders;
    }
}
