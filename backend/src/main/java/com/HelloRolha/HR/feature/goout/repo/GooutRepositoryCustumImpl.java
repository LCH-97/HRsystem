package com.HelloRolha.HR.feature.goout.repo;

import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.goout.model.QGoout;
import com.HelloRolha.HR.feature.goout.model.QGooutFile;
import com.HelloRolha.HR.feature.goout.model.QGooutType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GooutRepositoryCustumImpl extends QuerydslRepositorySupport implements GooutRepositoryCustum{
    private final JPAQueryFactory queryFactory;

    public GooutRepositoryCustumImpl(JPAQueryFactory queryFactory) {
        super(Goout.class);
        this.queryFactory = queryFactory;
    }
    @Override
    public Page<Goout> findList(Pageable pageable) {

        // 조인이 필요한 각 클래스들에 대한 객체 생성
        QGoout goout = new QGoout("goout");
        QGooutFile gooutFile= new QGooutFile("gooutFile");
        QGooutType gooutType = new QGooutType("gooutType");
        QEmployee employee = new QEmployee("employee");

        // QueryDSL 을 사용하기 위한 from 메서드 작성
        List<Goout> result = from(goout)
                .leftJoin(goout.gooutFiles, gooutFile).fetchJoin()
                .leftJoin(goout.gooutType, gooutType).fetchJoin()
                .leftJoin(goout.employee, employee).fetchJoin()
                .leftJoin(goout.agent, employee).fetchJoin()
                .leftJoin(goout.writer, employee).fetchJoin()
                // 중복제거를 위한 코드 추가
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().stream().distinct().collect(Collectors.toList());

        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public Optional<Goout> findByIdWithDetails(Integer id) {
        QGoout goout = QGoout.goout;
        QGooutFile gooutFile = QGooutFile.gooutFile;
        QGooutType gooutType = QGooutType.gooutType;
        QEmployee employee = QEmployee.employee;

        Goout result = queryFactory.selectFrom(goout)
                .leftJoin(goout.gooutFiles, gooutFile).fetchJoin()
                .leftJoin(goout.gooutType, gooutType).fetchJoin()
                .leftJoin(goout.employee, employee).fetchJoin()
                .leftJoin(goout.agent, employee).fetchJoin()
                .leftJoin(goout.writer, employee).fetchJoin()
                .where(goout.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
