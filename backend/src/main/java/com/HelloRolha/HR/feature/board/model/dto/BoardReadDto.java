package com.HelloRolha.HR.feature.board.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class BoardReadDto {
    private final Integer id;
    private final String writerName;
    private final String title;
    private final String text;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;
}
