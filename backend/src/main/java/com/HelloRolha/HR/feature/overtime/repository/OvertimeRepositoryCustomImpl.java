package com.HelloRolha.HR.feature.overtime.repository;

import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
import com.HelloRolha.HR.feature.overtime.model.Overtime;
import com.HelloRolha.HR.feature.overtime.model.QOvertime;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.h2.mvstore.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OvertimeRepositoryCustomImpl extends QuerydslRepositorySupport implements OvertimeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public OvertimeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Overtime.class);
        this.queryFactory = queryFactory;
    }
    @Override
    public List<Overtime> findList() {

        // 조인이 필요한 각 클래스들에 대한 객체 생성
        QOvertime overtime = new QOvertime("overtime");
//        QGooutFile gooutFile= new QGooutFile("gooutFile");
//        QGooutType gooutType = new QGooutType("gooutType");
        QEmployee employee = new QEmployee("employee");

        // QueryDSL 을 사용하기 위한 from 메서드 작성
        List<Overtime> result = from(overtime)
//                .leftJoin(overtime.gooutFiles, gooutFile).fetchJoin()
//                .leftJoin(overtime.gooutType, gooutType).fetchJoin()
                .leftJoin(overtime.employee, employee).fetchJoin()
//                .leftJoin(overtime.agent, employee).fetchJoin()
//                .leftJoin(overtime.writer, employee).fetchJoin()
                // 중복제거를 위한 코드 추가
                .fetch().stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public Optional<Overtime> findByIdWithDetails(Integer id) {
        QOvertime overtime = QOvertime.overtime;
        QEmployee employee = QEmployee.employee;

        Overtime result = queryFactory.selectFrom(overtime)
//                .leftJoin(goout.gooutFiles, gooutFile).fetchJoin()
//                .leftJoin(goout.gooutType, gooutType).fetchJoin()
                .leftJoin(overtime.employee, employee).fetchJoin()
//                .leftJoin(goout.agent, employee).fetchJoin()
//                .leftJoin(goout.writer, employee).fetchJoin()
                .where(overtime.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

//    @Override
//    public Page<Overtime> findList(Pageable pageable) {
//        QOvertime overtime = new QOvertime("overtime");
////      QProductImage productImage = new QProductImage("productImage");
//        QEmployee employee = new QEmployee("employee");
//
//        List<Overtime> result = from(Overtime)
////              .leftJoin(Overtime.productImageList, productImage).fetchJoin()
//                .leftJoin(Overtime.employee, employee).fetchJoin()
//                .distinct()
////                .offset(pageable.getOffset())
////                .limit(pageable.getPageSize())
//                .fetch().stream().distinct().collect(Collectors.toList());
//
//        return new PageImpl<>(result, pageable, result.size());
//    }
//

}