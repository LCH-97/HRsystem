package com.HelloRolha.HR.feature.overtime.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.overtime.model.Overtime;
import com.HelloRolha.HR.feature.overtime.model.dto.CreateOvertimeReq;
import com.HelloRolha.HR.feature.overtime.model.dto.OvertimeDto;
import com.HelloRolha.HR.feature.overtime.service.OvertimeNotFoundException;
import com.HelloRolha.HR.feature.overtime.service.OvertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/employee")
public class OvertimeController {

    private final OvertimeService overtimeService;

    @Autowired
    public OvertimeController(OvertimeService overtimeService) {
        this.overtimeService = overtimeService;
    }

    @PostMapping("/overtime/create") // 초과 근무 신청
    public ResponseEntity<BaseRes> submitOvertimeRequest( CreateOvertimeReq createOvertimeReq) {
        BaseRes res = BaseRes.builder()
                .code(1200)
                .isSuccess(true)
                .message("초과 생성 성공")
                .result(overtimeService.processOvertimeRequest(createOvertimeReq))
                .build();

        return ResponseEntity.ok(res);
    }

    @GetMapping("/overtime/list") // 초과 근무 목록 조회
    public ResponseEntity<BaseRes> list() {
        List<OvertimeDto> overtimeDtos = overtimeService.mylist();
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("초과 근무 확인 성공")
                .isSuccess(true)
                .result(overtimeDtos)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/overtime/read/{id}") // 초과 근무 상세 조회
    public ResponseEntity<OvertimeDto> read(@PathVariable Integer id) {
        OvertimeDto overtimeDto = overtimeService.read(id);
        return ResponseEntity.ok().body(overtimeDto);
    }

    @PatchMapping("/overtime/update/{id}") // 초과 근무 수정
    public ResponseEntity<String> update(@PathVariable Integer id, OvertimeDto overtimeDto) {
        try {
            overtimeService.update(id, overtimeDto);
            return ResponseEntity.ok().body("초과 근무가 성공적으로 수정되었습니다.");
        } catch (OvertimeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/overtime/approve")
    public ResponseEntity<String> approveOvertime(@RequestBody OvertimeDto overtimeDto) {
        try {
            overtimeService.approveOvertime(overtimeDto.getId());
            return ResponseEntity.ok().body("초과 근무가 성공적으로 승인되었습니다.");
        } catch (OvertimeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/overtime/list/real") // 초과 근무 목록 조회
    public ResponseEntity<BaseRes> list2() {
        List<OvertimeDto> overtimeDtos = overtimeService.list();
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("초과 근무 확인 성공2")
                .isSuccess(true)
                .result(overtimeDtos)
                .build();
        return ResponseEntity.ok().body(response);
    }
}