package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class GooutLineConfirm {
    private Integer id;
    private Integer confirmerId;
    private Integer gooutId;
    private String comment;
}