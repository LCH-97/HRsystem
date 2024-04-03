package com.HelloRolha.HR.feature.board.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BoardUpdateReq {
    private final Integer id;
    private final String title;
    private final String text;
}
