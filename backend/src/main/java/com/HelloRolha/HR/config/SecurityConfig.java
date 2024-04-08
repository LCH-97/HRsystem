package com.HelloRolha.HR.config;


import com.HelloRolha.HR.config.utils.filter.JwtFilter;
import com.HelloRolha.HR.error.security.CustomAccessDeniedHandler;
import com.HelloRolha.HR.error.security.CustomAuthenticationEntryPoint;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.refreshToken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final EmployeeRepository employeeRepository;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate redisTemplate;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf().disable()
                    .authorizeHttpRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // CORS 해결하기 위한 OPTION 메서드 허용
                    .antMatchers("/manager/**").hasAnyRole( "ADMIN")
                    .antMatchers("/employee/login").permitAll()
                    .antMatchers("/employee/signup").permitAll()
                    .antMatchers("/employee/init").permitAll()
                    .antMatchers("/refresh/accessToken").permitAll() // 리프레시토큰 재발급은 인증없앰
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .accessDeniedHandler(customAccessDeniedHandler) // 인가에 대한 예외 처리
                    .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증에 대한 예외 처리
                    .and()
                    .formLogin().disable()
                    .addFilterBefore(new JwtFilter(secretKey,employeeRepository, refreshTokenService, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            return  http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
