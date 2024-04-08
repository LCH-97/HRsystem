package com.HelloRolha.HR.config.utils.filter;

import com.HelloRolha.HR.config.utils.JwtUtils;
import com.HelloRolha.HR.error.TokenNotFoundException;
import com.HelloRolha.HR.error.UserAccountException;
import com.HelloRolha.HR.error.UserNotFoundException;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenReq;
import com.HelloRolha.HR.feature.refreshToken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
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
    private final EmployeeRepository employeeRepository;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = "";
        String refreshToken = "";

        //토큰 가져오기
        if (header.startsWith("Bearer ")) {
            token = header.split(" ")[1];
            refreshToken = header.split(" ")[1];
        } else {
            //Bearer 토큰이 없으면
            throw UserAccountException.forInvalidToken("Not Bear Token");

        }
        // 리프레시토큰인지 점검
        Integer refreshTokenId  = JwtUtils.getId(refreshToken, secretKey);

        // Redis에서 리프레시 토큰 검색
        String storedRefreshTokenKey = "refreshToken:" + refreshTokenId;
        String storedRefreshToken = redisTemplate.opsForValue().get(storedRefreshTokenKey);
        TokenReq tokenReq = TokenReq.builder()
                .refreshToken(storedRefreshToken)
                .build();

        // token 기간다되면 재발급
        boolean isTokenExpired = JwtUtils.isTokenExpired(token, secretKey);
        if (isTokenExpired) {
            refreshTokenService.refreshAccessToken(tokenReq);
        }

        // 토큰에 있는 id 정보를 가져온다. 정보를 가져오면서 유효한 토큰 인지 확인 할 수 있다.
        Integer accessTokenId = JwtUtils.getId(token, secretKey);

        //검증
        if(!accessTokenId.equals(refreshTokenId)){
            throw new TokenNotFoundException("refreshToken이 올바르지 않습니다.");
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(refreshTokenId);
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