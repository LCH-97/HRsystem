package com.HelloRolha.HR.feature.refreshToken.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenRes {
    private String token;
    private String refreshToken;
    private String refreshTokenKey;
}
