package com.HelloRolha.HR.feature.employee.service;

import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpReq;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpRes;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmployeeServiceTest {
    //    @Mock
//
    @Mock
    private EmployeeRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private EmployeeService employeeService;

    @Test
    // method
    void employeeService_signup_success(){
//given
        Employee employee = Employee.builder()
                .id(1)
                .build();
        given(memberRepository.save(any(Employee.class))).willReturn(employee);
        SignUpReq request = SignUpReq.builder()
                .username("test01@test.com")
                .password("qwer1234")
                .name("test01")
                .build();

        //when
        SignUpRes response = employeeService.signUp(request);

        //then
        assertTrue(response.getResult());
//        assertEquals(1000L,  response.getCode());
//        assertEquals(1L,  response.getResult().get("idx"));
//        assertEquals("요청 성공.",  response.getMessage());
//        assertTrue(response.getSuccess());
    }
}