package com.HelloRolha.HR.config.utils.filter;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.config.utils.JwtUtils;
import com.HelloRolha.HR.error.TokenNotFoundException;
import com.HelloRolha.HR.error.TokenRefreshException;
import com.HelloRolha.HR.error.UserAccountException;
import com.HelloRolha.HR.error.UserNotFoundException;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.refreshToken.model.RefreshToken;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenCreateReq;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenReq;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenRes;
import com.HelloRolha.HR.feature.refreshToken.repo.RefreshTokenRepository;
import com.HelloRolha.HR.feature.refreshToken.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private final String secretKey;

    @Value("${jwt.secret-key2}")
    private final String secretKey2;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    private final EmployeeRepository employeeRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = "";
        //토큰 가져오기
        if (header.startsWith("Bearer ")) {
            token = header.split(" ")[1];

        } else {
            //Bearer 토큰이 없으면
            throw UserAccountException.forInvalidToken("Not Bear Token");

        }

        Integer tokenId = JwtUtils.getId(token, secretKey);
        Integer refreshTokenId = null;
        String storedRefreshToken = null;


        // Redis에서 리프레시 토큰 검색
        try {
            storedRefreshToken = (String) redisTemplate.opsForValue().get("refreshToken: " + tokenId);
            if (storedRefreshToken != null) {
                refreshTokenId = JwtUtils.getId(storedRefreshToken, secretKey2);
            } else {
                throw new TokenNotFoundException("Redis token not found, looking up in DB.");
            }

            // Redis에서 못찾으면 DB로 검색
        } catch (Exception e) {
            log.info("Failed to get refresh token from Redis: {}, Trying from DB.", e.getMessage());
            Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
            if (refreshTokenOptional.isPresent()) {
                storedRefreshToken = refreshTokenOptional.get().getRefreshToken();
                refreshTokenId = JwtUtils.getId(storedRefreshToken, secretKey2);
            }
        }

        // token 기간다되면 재발급
        boolean isTokenExpired = JwtUtils.isTokenExpired(token, secretKey);
        if (isTokenExpired) {
            TokenReq tokenReq = new TokenReq(storedRefreshToken);
            TokenRes tokenRes = refreshTokenService.refreshAccessToken(tokenReq);
            TokenCreateReq tokenCreateReq = TokenCreateReq.builder()
                    .userId(refreshTokenId)
                    .refreshToken(tokenRes.getRefreshToken())
                    .token(tokenRes.getToken())
                    .build();
            refreshTokenService.create(tokenCreateReq);

            Optional<Employee> employeeOptional = employeeRepository.findById(refreshTokenId);
            if (employeeOptional.isEmpty()) throw UserNotFoundException.forIdx(refreshTokenId);
            Employee employee = employeeOptional.get();

            // 토큰이 조작되었는지 확인하는 코드
            // 인가하는 코드
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    employee,
                    null,
                    employee.getAuthorities()

            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {

            Integer accessTokenId = JwtUtils.getId(token, secretKey);
            Optional<Employee> employeeOptional = employeeRepository.findById(accessTokenId);
            if (employeeOptional.isEmpty()) throw UserNotFoundException.forIdx(accessTokenId);
            Employee employee = employeeOptional.get();

            // 토큰이 조작되었는지 확인하는 코드
            // 인가하는 코드
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    employee,
                    null,
                    employee.getAuthorities()

            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }
}