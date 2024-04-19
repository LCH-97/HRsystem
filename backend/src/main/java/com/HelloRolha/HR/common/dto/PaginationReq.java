package com.HelloRolha.HR.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationReq {
    private Integer page;
    private Integer size;
}
