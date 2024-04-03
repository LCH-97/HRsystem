package com.HelloRolha.HR.feature.board.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class BoardListReq {
    private Integer id;
    private String writerName;
    private String title;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}