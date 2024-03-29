package com.HelloRolha.HR.config.utils.filter;

import com.HelloRolha.HR.config.utils.JwtUtils;
import com.HelloRolha.HR.error.UserNotFoundException;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token;
        //토큰 가져오기
        if (header != null && header.startsWith("Bearer ")) {
            token = header.split(" ")[1];
        } else {
            //토큰이 없으면
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰이 있다면 유효한 토큰인지 확인한다.

        String authority = JwtUtils.getAuthority(token, secretKey);

        Integer id = JwtUtils.getId(token, secretKey);
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if(employeeOptional.isEmpty()) throw UserNotFoundException.forIdx(id);
        Employee employee = employeeOptional.get();
        if (employee.getAuthority().equals("ROLE_USER") || employee.getAuthority().equals("ROLE_ADMIN") || employee.getAuthority().equals("ROLE_NEW")) {
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