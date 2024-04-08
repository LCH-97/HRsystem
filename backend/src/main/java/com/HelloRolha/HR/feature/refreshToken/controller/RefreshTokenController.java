package com.HelloRolha.HR.feature.refreshToken.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenReq;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenRes;
import com.HelloRolha.HR.feature.refreshToken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/refresh")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    // 리프레시 토큰을 사용하여 새 액세스 토큰 발급 요청 처리
    @PostMapping("/accessToken")
    public ResponseEntity refreshAccessToken(@RequestBody TokenReq tokenReq) {
        TokenRes tokenRes = refreshTokenService.refreshAccessToken(tokenReq);

        BaseRes response = BaseRes.builder()
                .code(1200)
                .isSuccess(true)
                .message("새 access token과 refresh token이 생성되었습니다.")
                .result(tokenRes)
                .build();
        return ResponseEntity.ok().body(response);
    }
}