package com.HelloRolha.HR.feature.approve.repo;

import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.approve.model.QApprove;
import com.HelloRolha.HR.feature.approve.model.QApproveFile;
import com.HelloRolha.HR.feature.approve.model.QApproveLine;
import com.HelloRolha.HR.feature.board.model.Board;
import com.HelloRolha.HR.feature.board.model.QBoard;
import com.HelloRolha.HR.feature.board.model.QBoardFile;
import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApproveRepositoryCustomImpl extends QuerydslRepositorySupport implements ApproveRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ApproveRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Approve.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Approve> findList() {

        // 조인이 필요한 각 클래스들에 대한 객체 생성
        QApprove approve = new QApprove("approve");
        QApproveFile approveFile = new QApproveFile("approveFile");
        QApproveLine approveLine = new QApproveLine("approveLine");
        QEmployee employee = new QEmployee("employee");

        // QueryDSL 을 사용하기 위한 from 메서드 작성
        List<Approve> result = from(approve)
                .leftJoin(approve.approveFiles, approveFile).fetchJoin()
                .leftJoin(approve.approveLines, approveLine).fetchJoin()
                .leftJoin(approve.employee, employee).fetchJoin()
                // 중복제거를 위한 코드 추가
                .fetch().stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public Optional<Approve> findByIdWithDetails(Integer id) {
        QApprove approve = QApprove.approve;
        QEmployee employee = QEmployee.employee;

        Approve result = queryFactory.selectFrom(approve)
                .leftJoin(approve.approveFiles).fetchJoin()
                .leftJoin(approve.approveLines).fetchJoin()
                .leftJoin(approve.employee, employee).fetchJoin()
                .where(approve.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }




    @Override
    public Page<Approve> findList(Pageable pageable) {
        QApprove approve = new QApprove("approve");
        //QProductImage productImage = new QProductImage("productImage");
        QEmployee employee = new QEmployee("employee");

        List<Approve> result = from(approve)
//                .leftJoin(board.productImageList, productImage).fetchJoin()
                .leftJoin(approve.employee, employee).fetchJoin()
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }
}