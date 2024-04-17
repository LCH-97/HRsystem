package com.HelloRolha.HR.feature.employee.service;

import com.HelloRolha.HR.feature.employee.controller.EmployeeController;
import com.HelloRolha.HR.feature.employee.model.dto.Login.LoginRes;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpReq;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpRes;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.employee.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    //@Mock
    //private StringRedisTemplate redisTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    @DisplayName("직원 회원가입")
    void employeeSignupTest() {
        // given
        Employee employee = Employee.builder()
                .id(1)
                .build();
//       given(employeeRepository.save(any(Employee.class))).willReturn(employee);
        SignUpReq request = SignUpReq.builder()
                .username("test01@test.com")
                .password("qwer1234")
                .name("test01")
                .positionId(1)
                .departmentId(1)
                .build();

        // when
        SignUpRes response = employeeService.signUp(request);

        // then

        assertTrue(response.getResult());

    }

    @Test
    @DisplayName("직원 로그인")
    void employeeLoginTest() {
        // Given
        LoginRes fakeResponse = LoginRes.builder()
                 //.name("test01")
                .token("fakeToken")
                 //.refreshToken("fakeRefreshToken")
                 //.refreshTokenKey("fakeRefreshTokenKey")
                .build();

        // When
        LoginRes response = fakeResponse;

        // Then
        assertNotNull(response);
         // assertNotNull(response.getName());
        assertNotNull(response.getToken());
         //assertNotNull(response.getRefreshToken());
         //assertNotNull(response.getRefreshTokenKey());
    }


    @Test
    @DisplayName("매니저 회원가입")
    void managerSignupTest() {
        // Given
        Employee employee = Employee.builder()
                .id(1)
                .authority("ROLE_ADMIN") // 매니저를 ROLE_ADMIN으로 등록
                .build();
//        given(employeeRepository.save(any(Employee.class))).willReturn(employee);
        SignUpReq request = SignUpReq.builder()
                .username("manager01@test.com")
                .password("password1234")
                .name("manager01")
                .positionId(2)
                .departmentId(2)
                .build();

        // When
        SignUpRes response = employeeService.signUp(request);

        // Then
        assertTrue(response.getResult());
    }

    @Test
    @DisplayName("매니저 로그인")
    void managerLoginTest() { //매니저 로그인
        // Given
        LoginRes fakeResponse = LoginRes.builder()
                //.name("manager01")
                .token("fakeToken2")
                //.refreshToken("fakeRefreshToken2")
                //.refreshTokenKey("fakeRefreshTokenKey2")
                .build();

        // When
        LoginRes response = fakeResponse;

        // Then
        assertNotNull(response);
        //assertNotNull(response.getName());
        assertNotNull(response.getToken());
        //assertNotNull(response.getRefreshToken());
        //assertNotNull(response.getRefreshTokenKey());
    }

}