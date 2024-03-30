package com.HelloRolha.HR.feature.board.repository;

import com.HelloRolha.HR.feature.board.model.Board;
import com.HelloRolha.HR.feature.board.model.QBoard;
import com.HelloRolha.HR.feature.board.model.QBoardFile;
import com.HelloRolha.HR.feature.employee.model.entity.QEmployee;
import com.HelloRolha.HR.feature.goout.model.QGooutFile;
import com.HelloRolha.HR.feature.goout.model.QGooutType;
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
    public Page<Board> findList(Pageable pageable) {
        // 조인이 필요한 각 클래스들에 대한 객체 생성
        QBoard board = new QBoard("board");
        QEmployee writer = new QEmployee("writer");

        // 총 개수를 위한 쿼리
        long total = queryFactory
                .selectFrom(board)
                .leftJoin(board.writer, writer)
                .fetchCount();

        // QueryDSL 을 사용하기 위한 from 메서드 작성
        List<Board> result = from(board)
                .leftJoin(board.writer, writer)
                // 중복제거를 위한 코드 추가
                .distinct()
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Optional<Board> findByIdWithDetails(Integer id) {
        QBoard board = QBoard.board;
        QBoardFile boardFile = QBoardFile.boardFile;
        QEmployee writer = new QEmployee("writer");

        Board result = queryFactory.selectFrom(board)
                .leftJoin(board.boardFiles, boardFile).fetchJoin()
                .leftJoin(board.writer, writer).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}