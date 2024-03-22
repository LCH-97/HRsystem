package com.HelloRolha.HR.feature.approve.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.approve.model.ApproveLine;
import com.HelloRolha.HR.feature.approve.model.dto.ApproveLine.*;
import com.HelloRolha.HR.feature.approve.service.ApproveLineService;
import com.HelloRolha.HR.feature.goout.model.GooutLine;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/approve/line")
@RequiredArgsConstructor
public class ApproveLineController {
    private final ApproveLineService approveLineService;

    @PostMapping("/create")
    public ResponseEntity<BaseRes> create(@RequestBody ApproveLineCreateReq approveLineCreateReq) {
        ApproveLine approveLine = approveLineService.create(approveLineCreateReq);

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("결재 라인 생성 성공")
                .result(approveLine)
                .build();

        return ResponseEntity.ok().body(res);
    }

//    @PatchMapping("/patch")
//    public ResponseEntity<BaseRes> create(ApproveLinePatchReq approveLinePatchReq) {
//        BaseRes res = BaseRes.builder()
//                .code(200)
//                .isSuccess(true)
//                .message("결재 라인 변경 성공")
//                .result(approveLineService.patch(approveLinePatchReq))
//                .build();
//
//        return ResponseEntity.ok().body(res);
//    }

    @GetMapping("/list")
    public ResponseEntity<BaseRes> list() {
        try {
            List<ApproveLineList> approveLineLists = approveLineService.list();
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재 라인 목록 조회 성공")
                    .isSuccess(true)
                    .result(approveLineLists)
                    .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재 라인 목록 조회 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseRes> read(@PathVariable Integer id) {
        try {
            ApproveLineRead approveLineRead = approveLineService.read(id);
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재 라인 상세 조회 성공")
                    .isSuccess(true)
                    .result(approveLineRead)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재 라인 상세 조회 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/2/{id}")
    public ResponseEntity<BaseRes> read2(@PathVariable Integer id) {
        try {
            ApproveLineRead approveLineRead = approveLineService.read2(id);
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재 라인 상세 조회 성공")
                    .isSuccess(true)
                    .result(approveLineRead)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재 라인 상세 조회 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PatchMapping("/confirm1")
    public ResponseEntity<BaseRes> confirm1(@RequestBody ApproveLineConfirm approveLineConfirm) {
        try {
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재자1의 결재 처리 성공")
                    .isSuccess(true)
                    .result(approveLineService.confirm1(approveLineConfirm))
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재자1의 결재 처리 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            BaseRes response = BaseRes.builder()
                    .code(1500)
                    .message("내부 서버 오류: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/confirm2")
    public ResponseEntity<BaseRes> confirm2(@RequestBody ApproveLineConfirm approveLineConfirm) {
        try {
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재자2의 결재 처리 성공")
                    .isSuccess(true)
                    .result(approveLineService.confirm2(approveLineConfirm))
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재자2의 결재 처리 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            BaseRes response = BaseRes.builder()
                    .code(1500)
                    .message("내부 서버 오류: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PatchMapping("/reject1")
    public ResponseEntity<BaseRes> reject1(@RequestBody ApproveLineConfirm approveLineConfirm) {
        try {
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재자1의 거절 처리 성공")
                    .isSuccess(true)
                    .result(approveLineService.reject1(approveLineConfirm))
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재자1의 거절 처리 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            BaseRes response = BaseRes.builder()
                    .code(1500)
                    .message("내부 서버 오류: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PatchMapping("/reject2")
    public ResponseEntity<BaseRes> reject2(@RequestBody ApproveLineConfirm approveLineConfirm) {
        try {
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재자2의 거절 처리 성공")
                    .isSuccess(true)
                    .result(approveLineService.reject2(approveLineConfirm))
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재자2의 거절 처리 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            BaseRes response = BaseRes.builder()
                    .code(1500)
                    .message("내부 서버 오류: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseRes> delete(@PathVariable Integer id) {
        try {
            approveLineService.delete(id);
            BaseRes response = BaseRes.builder()
                    .code(1200)
                    .message("결재 라인 삭제 성공")
                    .isSuccess(true)
                    .result("삭제한 id : " + id)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            BaseRes response = BaseRes.builder()
                    .code(1400)
                    .message("결재 라인 삭제 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            BaseRes response = BaseRes.builder()
                    .code(1500)
                    .message("내부 서버 오류: " + e.getMessage())
                    .isSuccess(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<BaseRes> update(@RequestBody ApproveLineUpdateReq approveLineUpdateReq) {
        approveLineService.update(approveLineUpdateReq);
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("결재 정보 수정 성공")
                .isSuccess(true)
                .result(approveLineUpdateReq)
                .build();
        return ResponseEntity.ok(response);
    }
}