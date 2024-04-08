package com.HelloRolha.HR.feature.refreshToken.service;

import com.HelloRolha.HR.config.utils.JwtUtils;
import com.HelloRolha.HR.error.ErrorCode;
import com.HelloRolha.HR.error.TokenNotFoundException;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenReq;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {
    // 필요한 의존성 주입
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.refresh-token.expired-time-ms}")
    private Long refreshTokenExpiredTimeMs;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    // 리프레시 토큰으로 새 액세스 토큰 발급 (Redis 조회 로직 포함)
    public TokenRes refreshAccessToken(TokenReq tokenReq) throws TokenNotFoundException {

        // refreshToken을 디코드하여 Claims 객체로 변환
        Integer userId = JwtUtils.getId(tokenReq.getRefreshToken(), secretKey);

        // 사용자 정보 조회
        Employee employee = ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        //검증
        if(!employee.getId().equals(userId)) {
            throw new TokenNotFoundException(ErrorCode.EXPIRED_VERIFICATION_TOKEN.getDescription());
        }

        String newAccessToken = JwtUtils.generateAccessToken(employee, secretKey, expiredTimeMs);
        String newRefreshToken = JwtUtils.generateRefreshToken(employee, secretKey);
        redisTemplate.opsForValue().set("refreshToken:" + employee.getId(), newRefreshToken, refreshTokenExpiredTimeMs, TimeUnit.MILLISECONDS);

        return TokenRes.builder()
                .token(newAccessToken)
                .refreshTokenKey("refreshToken:" + employee.getId())
                .refreshToken(newRefreshToken)
                .build();
    }
}
