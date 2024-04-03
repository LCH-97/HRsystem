package com.HelloRolha.HR.feature.board.repository;

import com.HelloRolha.HR.feature.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<Board> findByIdWithDetails(Integer id);

    Page<Board> findList(Pageable pageable);
}
