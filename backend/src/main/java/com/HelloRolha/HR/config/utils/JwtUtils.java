package com.HelloRolha.HR.config.utils;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.error.UserAccountException;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
@RequiredArgsConstructor
public class JwtUtils {

    // 일반 로그인 사용자 토큰 생성
    public static String generateAccessToken(Employee employee, String secretKey, Long expiredTimeMs) {

        Claims claims = Jwts.claims();
        claims.put("ID", employee.getId());


        byte[] secretBytes = secretKey.getBytes();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(Keys.hmacShaKeyFor(secretBytes), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static String generateRefreshToken(Employee employee, String secretKey) {
        byte[] secretBytes = secretKey.getBytes();
        long refreshExpiredTimeMs = 3600000L; // 1시간 = 360만
        Claims claims = Jwts.claims();
        claims.put("ID", employee.getId());

        String refreshToken = Jwts.builder()
                .setClaims(claims) // 주로 사용자의 고유 식별자(username 등)을 사용합니다.
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiredTimeMs)) // 만료 시간 설정
                .signWith(Keys.hmacShaKeyFor(secretBytes), SignatureAlgorithm.HS256)
                .compact();
        return refreshToken;
    }

    // 키 변환 메서드
    public static Key getSignKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 사용자 이름 가져오는 메서드

    public static String getName(String token, String key) {
        return extractAllClaims(token, key).get("NAME", String.class);
    }
    public static String getAuthority(String token, String key) {
        return extractAllClaims(token, key).get("ROLE", String.class);
    }
    public static Integer getId(String token, String key) {
        return extractAllClaims(token, key).get("ID", Integer.class);
    }

    // 토근에서 정보를 가져오는 코드가 계속 중복되어 사용되기 때문에 별도의 메서드로 만들어서 사용하기 위한 것
    public static Claims extractAllClaims(String token, String key) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey(key))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // access 키가 기간이 지났으면 여기로 온다.
           return null;

        } catch (SignatureException e) {
            // 리프레쉬 키를 secret키로 까면 여기로 온다.
//            @ExceptionHandler(ExpiredJwtException.class)
//            public ResponseEntity<BaseRes> handleExpiredJwtException(ExpiredJwtException e, HttpServletRequest request) {
//                String refreshToken = request.getHeader("refreshToken");
//        TokenRes tokenRes = refreshTokenService.refreshAccessToken(new TokenReq(refreshToken));
//                BaseRes response = BaseRes.builder()
//                        .code(1200)
//                        .isSuccess(true)
//                        .message("새 token, refreshToken이 발급되었습니다.")
//                        .result(null)
//                        .build();
//                return ResponseEntity.ok().body(response);
//
//            }
            return null;
        }
    }

    // 토큰의 만료 여부를 확인하는 메소드
    public static boolean isTokenExpired(String token, String secretKey) {
        try {
            // 토큰에서 Claims 추출 시도
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return false; // 여기까지 정상적으로 수행되면 토큰은 만료되지 않은 것입니다.
        } catch (ExpiredJwtException e) {
            // 만약 ExpiredJwtException이 발생했다면, 토큰이 만료된 것입니다.
            return true;
        } catch (Exception e) {
            // 기타 다른 예외 상황 (예: 서명 검증 실패 등)
            // 이 경우는 토큰 만료와는 다른 문제이므로, 적절히 처리할 수 있도록 합니다.
            // 예를 들어, 토큰의 서명이 유효하지 않은 경우에는 다른 예외를 던질 수 있습니다.
            throw UserAccountException.forInvalidToken(token);
        }
    }
}
