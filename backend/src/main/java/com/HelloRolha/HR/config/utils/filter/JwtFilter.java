package com.HelloRolha.HR.config.utils.filter;

import com.HelloRolha.HR.config.utils.JwtUtils;
import com.HelloRolha.HR.error.TokenNotFoundException;
import com.HelloRolha.HR.error.TokenRefreshException;
import com.HelloRolha.HR.error.UserAccountException;
import com.HelloRolha.HR.error.UserNotFoundException;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.refreshToken.model.dto.TokenReq;
import com.HelloRolha.HR.feature.refreshToken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    private final EmployeeRepository employeeRepository;
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

        Integer accessTokenId = JwtUtils.getId(token, secretKey);

        Optional<Employee> employeeOptional = employeeRepository.findById(accessTokenId);
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