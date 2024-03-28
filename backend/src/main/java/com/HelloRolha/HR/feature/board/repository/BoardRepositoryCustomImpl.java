package com.HelloRolha.HR.feature.board.repository;

import com.HelloRolha.HR.feature.board.model.Board;
import com.HelloRolha.HR.feature.board.model.QBoard;
import com.HelloRolha.HR.feature.board.model.QBoardFile;
import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BoardRepositoryCustomImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Board.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Board> findList() {

        // 조인이 필요한 각 클래스들에 대한 객체 생성
        QBoard board = new QBoard("board");
        QBoardFile boardFile = new QBoardFile("boardFile");
        QEmployee employee = new QEmployee("employee");

        // QueryDSL 을 사용하기 위한 from 메서드 작성
        List<Board> result = from(board)
                .leftJoin(board.boardFiles, boardFile).fetchJoin()
//                .leftJoin(board.employee, employee).fetchJoin()
                // 중복제거를 위한 코드 추가
                .fetch().stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public Optional<Board> findByIdWithDetails(Integer id) {
        QBoard board = QBoard.board;
        QEmployee employee = QEmployee.employee;

        Board result = queryFactory.selectFrom(board)
                .leftJoin(board.boardFiles).fetchJoin()
//                .leftJoin(board.employee, employee).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }




    @Override
    public Page<Board> findList(Pageable pageable) {
        QBoard board = new QBoard("board");
        //QProductImage productImage = new QProductImage("productImage");
        QEmployee employee = new QEmployee("employee");

        List<Board> result = from(board)
                //.leftJoin(board.productImageList, productImage).fetchJoin()
                //.leftJoin(board.employee, employee).fetchJoin()
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }
}