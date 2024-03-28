package com.HelloRolha.HR.feature.board.repository;

import com.HelloRolha.HR.feature.board.model.Board;

import org.h2.mvstore.Page;

import java.awt.print.Pageable;



import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    public List<Board> findList();

    Optional<Board> findByIdWithDetails(Integer id);


    org.springframework.data.domain.Page<Board> findList(org.springframework.data.domain.Pageable pageable);
}
