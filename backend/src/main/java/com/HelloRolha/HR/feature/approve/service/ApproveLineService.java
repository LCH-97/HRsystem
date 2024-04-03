package com.HelloRolha.HR.feature.approve.service;

import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.approve.model.ApproveLine;
import com.HelloRolha.HR.feature.approve.model.dto.approveLine.*;
import com.HelloRolha.HR.feature.approve.repo.ApproveLineRepository;
import com.HelloRolha.HR.feature.approve.repo.ApproveRepository;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
        Employee confirmer = employeeRepository.findById(approveLineCreateReq.getConfirmerId())
                .orElseThrow(() -> new IllegalArgumentException("결재자의 ID가 존재하지 않습니다."));
        Approve approve = approveRepository.findById(approveLineCreateReq.getApproveId())
                .orElseThrow(() -> new IllegalArgumentException("결재의 ID가 존재하지 않습니다."));

        if (approveLineCreateReq.getConfirmerId() == null) {
            throw new IllegalArgumentException("결재자를 선택해주세요.");
        }

//        Objects.requireNonNull(approveLineCreateReq.getConfirmerId(), "결재자는 null일 수 없습니다.");

        ApproveLine approveLine =ApproveLine.builder()
                .approve(Approve.builder().id(approveLineCreateReq.getApproveId()).build())
                .confirmer(Employee.builder().id(approveLineCreateReq.getConfirmerId()).build())
                .approveTime(localDateTimeInKorea)
                .status(0)
                .build();
        return approveLineRepository.save(approveLine);
    }

    @Transactional
    public List<ApproveLineList> list() {
        List<ApproveLine> approveLines = approveLineRepository.findAll();
        List<ApproveLineList> approveLineLists = new ArrayList<>();

        for (ApproveLine approveLine : approveLines) {
            Employee confirmer = approveLine.getConfirmer();
            if (confirmer == null) {
                throw new RuntimeException("confirmer의 정보를 찾을 수 없습니다.");
            }

            ApproveLineList approveLineList = ApproveLineList.builder()
                    .id(approveLine.getId())
                    .confirmerName(confirmer.getName())
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
                .confirmerId(approveLine.getConfirmer().getId())
                .confirmerName(approveLine.getConfirmer().getName())
                .approveId(approveLine.getApprove().getId())
                .comment(approveLine.getComment())
                .approveTime(approveLine.getApproveTime())
                .applyTime(approveLine.getApplyTime())
                .status(approveLine.getStatus())
                .build();
    }

    @Transactional
    public List<ApproveLineList> read2(Integer approveId) {
        List<ApproveLine> approveLines = approveLineRepository.findByApproveId(approveId);
        List<ApproveLineList> approveLineLists = new ArrayList<>();
        for (ApproveLine approveLine : approveLines) {
            ApproveLineList approveLineList = ApproveLineList.builder()
                    .id(approveLine.getId())
                    .confirmerId(approveLine.getConfirmer().getId())
                    .confirmerName(approveLine.getConfirmer().getName())
                    .approveId(approveLine.getApprove().getId())
                    .comment(approveLine.getComment())
                    .status(approveLine.getStatus())
                    .build();
            approveLineLists.add(approveLineList);
        }
        return approveLineLists;
    }

    @Transactional
    public void confirm1(ApproveLineConfirm approveLineConfirm) {
        List<ApproveLine> approveLines = approveLineRepository.findByApproveIdAndConfirmerId(
                approveLineConfirm.getApproveId(), approveLineConfirm.getConfirmerId());;
        if (approveLines.isEmpty()) {
            throw new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다.");
        }
        ApproveLine approveLine = approveLines.get(0);
        approveLine.setStatus(1);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
    }

    @Transactional
    public void confirm2(ApproveLineConfirm approveLineConfirm) {
        List<ApproveLine> approveLines = approveLineRepository.findByApproveIdAndConfirmerId(
                approveLineConfirm.getApproveId(), approveLineConfirm.getConfirmerId());;
        if (approveLines.isEmpty()) {
            throw new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다.");
        }
        ApproveLine approveLine = approveLines.get(0);
        approveLine.setStatus(2);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
    }

    @Transactional
    public void reject1(ApproveLineConfirm approveLineConfirm) {
        List<ApproveLine> approveLines = approveLineRepository.findByApproveIdAndConfirmerId(
                approveLineConfirm.getApproveId(), approveLineConfirm.getConfirmerId());;
        if (approveLines.isEmpty()) {
            throw new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다.");
        }
        ApproveLine approveLine = approveLines.get(0);
        approveLine.setStatus(3);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
    }

    @Transactional
    public void reject2(ApproveLineConfirm approveLineConfirm) {
        List<ApproveLine> approveLines = approveLineRepository.findByApproveIdAndConfirmerId(
                approveLineConfirm.getApproveId(), approveLineConfirm.getConfirmerId());;
        if (approveLines.isEmpty()) {
            throw new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다.");
        }
        ApproveLine approveLine = approveLines.get(0);
        approveLine.setStatus(3);
        approveLine.setComment(approveLineConfirm.getComment());
        approveLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        approveLineRepository.save(approveLine); // 변경 사항 저장
    }

    @Transactional
    public void update(ApproveLineUpdateReq approveLineUpdateReq) {
        ApproveLine approveLine = approveLineRepository.findById(approveLineUpdateReq.getId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 결재 정보를 찾을 수 없습니다."));

        Employee confirmer = employeeRepository.findById(approveLineUpdateReq.getConfirmerId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 결재자 정보를 찾을 수 없습니다."));

        // 결재라인 정보 업데이트
        approveLine.setConfirmer(confirmer);
        approveLineRepository.save(approveLine);
    }

    @Transactional
    public void delete(Integer id) {
        ApproveLine approveLine = approveLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 결재라인이 존재하지 않습니다."));

        approveLineRepository.delete(approveLine);
    }

}