package com.HelloRolha.HR.feature.board.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardListRes {
    private List<BoardListReq> boards;
    private long totalElements;
}
