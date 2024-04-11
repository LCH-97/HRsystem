package com.HelloRolha.HR.feature.refreshToken.model.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenCreateReq {
    private Integer userId;
    private String refreshToken;
    private String token;
}
