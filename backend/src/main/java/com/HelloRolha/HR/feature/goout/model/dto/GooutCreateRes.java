package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GooutCreateRes {
    private Integer gooutId;
    private Integer gooutLine1Id;
    private Integer gooutLine2Id;
}
