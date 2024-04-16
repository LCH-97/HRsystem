package com.HelloRolha.HR.feature.employee.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.employee.model.dto.EmployeeDto;
import com.HelloRolha.HR.feature.employee.model.dto.Login.LoginReq;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpReq;
import com.HelloRolha.HR.feature.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @RequestMapping(method = RequestMethod.POST, value = "/init") //초기 인사 관리자
    public ResponseEntity init(SignUpReq signUpReq) {

        BaseRes res = BaseRes.builder()
                .code(1200)
                .isSuccess(true)
                .message("첫 관리자 생성 성공")
                .result(employeeService.createAdmin(signUpReq))
                .build();


        return ResponseEntity.ok().body(res);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity signUp(SignUpReq signUpReq) {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("회원 가입 성공")
                .result(employeeService.signUp(signUpReq))
                .build();


        return ResponseEntity.ok().body(res);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<BaseRes> login(LoginReq loginReq) {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("로그인 성공")
                .result(employeeService.login(loginReq))
                .build();


        return ResponseEntity.ok().body(res);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity<List<EmployeeDto>> listEmployees() {
        List<EmployeeDto> employeeDtos = employeeService.listEmployee();
        return ResponseEntity.ok().body(employeeDtos); // HTTP 200 OK 응답과 함께 직원 목록 반환
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDto>> listEmployeeByDepartmentId(@PathVariable Integer departmentId) {
    List<EmployeeDto> employees = employeeService.listEmployeeByDepartmentId(departmentId);
    return ResponseEntity.ok().body(employees);
    }
}
