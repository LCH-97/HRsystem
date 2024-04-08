package com.HelloRolha.HR.feature.salary.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.salary.model.dto.SalaryDto;
import com.HelloRolha.HR.feature.salary.service.SalaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/salary")
public class SalaryController {
    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping("/init") // 초과 근무 목록 조회
    public ResponseEntity<BaseRes> init() {
        List<SalaryDto> salaryDtoList = salaryService.init();
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("월급 초기화 성공")
                .isSuccess(true)
                .result(salaryDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
