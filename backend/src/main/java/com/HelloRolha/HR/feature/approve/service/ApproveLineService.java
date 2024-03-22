package com.HelloRolha.HR.feature.approve.service;

import com.HelloRolha.HR.error.ApproveNotFoundException;
import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.approve.model.ApproveLine;
import com.HelloRolha.HR.feature.approve.model.dto.ApproveLine.*;
import com.HelloRolha.HR.feature.approve.repo.ApproveLineRepository;
import com.HelloRolha.HR.feature.approve.repo.ApproveRepository;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.goout.model.GooutLine;
import com.HelloRolha.HR.feature.goout.model.dto.GooutLineList;
import com.HelloRolha.HR.feature.goout.model.dto.GooutLineUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApproveLineService {
    private final ApproveLineRepository approveLineRepository;
    private final EmployeeRepository employeeRepository;
    private final ApproveRepository approveRepository;

    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    LocalDateTime localDateTimeInKorea = nowInKorea.toLocalDateTime();

    @Transactional
    public ApproveLine create(ApproveLineCreateReq approveLineCreateReq) {
        if (approveLineCreateReq.getConfirmer1Id().equals(approveLineCreateReq.getConfirmer2Id())) {
            throw new IllegalArgumentException("결재자1의 ID와 결재자2의 ID는 같을 수 없습니다.");
        }

        Employee confirmer1 = employeeRepository.findById(approveLineCreateReq.getConfirmer1Id())
                .orElseThrow(() -> new IllegalArgumentException("결재자1의 ID가 존재하지 않습니다."));
        Employee confirmer2 = employeeRepository.findById(approveLineCreateReq.getConfirmer2Id())
                .orElseThrow(() -> new IllegalArgumentException("결재자2의 ID가 존재하지 않습니다."));
        Approve approve = approveRepository.findById(approveLineCreateReq.getApproveId())
                .orElseThrow(() -> new IllegalArgumentException("결재의 ID가 존재하지 않습니다."));
        Employee employee = employeeRepository.findById(approveLineCreateReq.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("기안자의 ID가 존재하지 않습니다."));


        ApproveLine approveLine =ApproveLine.builder()
                        .approve(approve)
                        .confirmer1(confirmer1)
                        .confirmer2(confirmer2)
                        .confirmer(employee)
                        .approveTime(localDateTimeInKorea)
                        .status(0)
                        .build();

        return approveLineRepository.save(approveLine);
    }



//    public Object patch(ApproveLinePatchReq approveLinePatchReq) {
//        return null;
//    }

    @Transactional
    public List<ApproveLineList> list() {
        List<ApproveLine> approveLines = approveLineRepository.findAll(); // 모든 결재라인 조회
        List<ApproveLineList> approveLineLists = new ArrayList<>();

        for (ApproveLine line : approveLines) {
            Employee confirmer1 = line.getConfirmer1();
            if (confirmer1 == null) {
                throw new RuntimeException("confirmer1의 정보를 찾을 수 없습니다.");
            }

            Employee confirmer2 = line.getConfirmer2();
            if (confirmer2 == null) {
                throw new RuntimeException("confirmer2의 정보를 찾을 수 없습니다.");
            }

            ApproveLineList approveLineList = ApproveLineList.builder()
                    .id(line.getId())
                    .confirmer1Name(confirmer1.getName())
                    .confirmer2Name(confirmer2.getName())
                    .build();
            approveLineLists.add(approveLineList);
        }
        return approveLineLists;
    }

    @Transactional
    public ApproveLineRead read(Integer id) {
        ApproveLine approveLine = approveLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        return ApproveLineRead.builder()
                .confirmer1Id(approveLine.getConfirmer1().getId())
                .confirmer2Id(approveLine.getConfirmer2().getId())
                .approveId(approveLine.getApprove().getId())
                .comment(approveLine.getComment())
                .approveTime(approveLine.getApproveTime())
                .applyTime(approveLine.getApplyTime())
                .status(approveLine.getStatus())
                .build();
    }

    @Transactional
    public ApproveLineRead read2(Integer id) {
        ApproveLine approveLine = approveLineRepository.findByApproveId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));
        return ApproveLineRead.builder()
                .confirmer1Id(approveLine.getConfirmer1().getId())
                .confirmer1Name(approveLine.getConfirmer1().getName())
                .confirmer2Id(approveLine.getConfirmer2().getId())
                .confirmer2Name(approveLine.getConfirmer2().getName())
                .employeeId(approveLine.getConfirmer().getId())
                .approveId(approveLine.getApprove().getId())
                .comment(approveLine.getComment())
                .approveTime(approveLine.getApproveTime())
                .applyTime(approveLine.getApplyTime())
                .status(approveLine.getStatus())
                .build();
    }

    @Transactional
    public ApproveConfirmRes confirm1(ApproveLineConfirm approveLineConfirm) {
        ApproveLine approveLine = approveLineRepository.findById(approveLineConfirm.getApproveId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!approveLine.getConfirmer1().getId().equals(approveLineConfirm.getConfirmer1Id())){
            throw new IllegalArgumentException("결재자1이 올바르지 않습니다.");
        }

        if (approveLine.getStatus() != 0) {
            throw new IllegalArgumentException("결재자1이 결재를 할 수 있는 상태가 아닙니다.");
        }

        approveLine.setStatus(1);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
        return ApproveConfirmRes.builder().status(approveLine.getStatus()).build();
    }

    @Transactional
    public ApproveConfirmRes confirm2(ApproveLineConfirm approveLineConfirm) {
        ApproveLine approveLine = approveLineRepository.findById(approveLineConfirm.getApproveId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!approveLine.getConfirmer2().getId().equals(approveLineConfirm.getConfirmer2Id())){
            throw new IllegalArgumentException("결재자2가 올바르지 않습니다.");
        }

        if (approveLine.getStatus() != 1) {
            throw new IllegalArgumentException("결재자2가 결재를 할 수 있는 상태가 아닙니다.");
        }

        approveLine.setStatus(2);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
        return ApproveConfirmRes.builder().status(approveLine.getStatus()).build();
    }

    @Transactional
    public ApproveConfirmRes reject1(ApproveLineConfirm approveLineConfirm) {
        ApproveLine approveLine = approveLineRepository.findById(approveLineConfirm.getApproveId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!approveLine.getConfirmer1().getId().equals(approveLineConfirm.getConfirmer1Id())){
            throw new IllegalArgumentException("결재자1이 올바르지 않습니다.");
        }

        if (approveLine.getStatus() != 0) {
            throw new IllegalArgumentException("결재자1이 결재를 할 수 있는 상태가 아닙니다.");
        }

        approveLine.setStatus(3);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
        return ApproveConfirmRes.builder().status(approveLine.getStatus()).build();
    }

    @Transactional
    public ApproveConfirmRes reject2(ApproveLineConfirm approveLineConfirm) {
        ApproveLine approveLine = approveLineRepository.findById(approveLineConfirm.getApproveId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!approveLine.getConfirmer2().getId().equals(approveLineConfirm.getConfirmer2Id())){
            throw new IllegalArgumentException("결재자2가 올바르지 않습니다.");
        }

        if (approveLine.getStatus() != 1) {
            throw new IllegalArgumentException("결재자2가 결재를 할 수 있는 상태가 아닙니다.");
        }

        approveLine.setStatus(3);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
        return ApproveConfirmRes.builder().status(approveLine.getStatus()).build();
    }

    @Transactional
    public void update(ApproveLineUpdateReq approveLineUpdateReq) {
        ApproveLine approveLine = approveLineRepository.findByApproveId(approveLineUpdateReq.getApproveId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 결재 정보를 찾을 수 없습니다."));

        Employee confirmer1 = employeeRepository.findById(approveLineUpdateReq.getConfirmer1Id())
                .orElseThrow(() -> new RuntimeException("해당 ID의 결재자1 정보를 찾을 수 없습니다."));
        Employee confirmer2 = employeeRepository.findById(approveLineUpdateReq.getConfirmer2Id())
                .orElseThrow(() -> new RuntimeException("해당 ID의 결재자2 정보를 찾을 수 없습니다."));

        // 결재라인 정보 업데이트
        approveLine.setConfirmer1(confirmer1);
        approveLine.setConfirmer2(confirmer2);
        approveLineRepository.save(approveLine);
    }

    @Transactional
    public void delete(Integer id) {
        ApproveLine approveLine = approveLineRepository.findByApproveId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 결재라인이 존재하지 않습니다."));

        approveLineRepository.delete(approveLine);
    }

}

