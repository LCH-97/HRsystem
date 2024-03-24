package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Builder
@Data
public class GooutLineUpdateReq {
    private Integer confirmerId;
    private Integer id;
}
