package com.HelloRolha.HR.feature.goout.service;

import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.goout.model.GooutLine;
import com.HelloRolha.HR.feature.goout.model.GooutType;
import com.HelloRolha.HR.feature.goout.model.dto.*;
import com.HelloRolha.HR.feature.goout.repo.GooutLineRepository;
import com.HelloRolha.HR.feature.goout.repo.GooutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GooutLineService {
    private final GooutLineRepository gooutLineRepository;
    private final EmployeeRepository employeeRepository;
    private final GooutRepository gooutRepository;
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    LocalDateTime localDateTimeInKorea = nowInKorea.toLocalDateTime();

    @Transactional
    public GooutLine create(GooutLineCreateReq gooutLineCreateReq) {

        Employee confirmer = employeeRepository.findById(gooutLineCreateReq.getConfirmerId())
                .orElseThrow(() -> new IllegalArgumentException("결재자의 ID가 존재하지 않습니다."));
        Goout goout = gooutRepository.findById(gooutLineCreateReq.getGooutId())
                .orElseThrow(() -> new IllegalArgumentException("휴가의 ID가 존재하지 않습니다."));

        GooutLine gooutLine = GooutLine.builder()
                .confirmer(Employee.builder().id(gooutLineCreateReq.getConfirmerId()).build())
                .goout(Goout.builder().id(gooutLineCreateReq.getGooutId()).build())
                .approveTime(localDateTimeInKorea)
                .status(0)
                .build();
        return gooutLineRepository.save(gooutLine);
    }

    @Transactional
    public List<GooutLineList> list() {
        List<GooutLine> gooutLines = gooutLineRepository.findAll(); // 모든 결재라인 조회
        List<GooutLineList> gooutLineLists = new ArrayList<>();

        for (GooutLine line : gooutLines) {
            Employee confirmer = line.getConfirmer();
            if (confirmer == null) {
                throw new RuntimeException("결재자의 정보를 찾을 수 없습니다.");
            }

            GooutLineList gooutLineList = GooutLineList.builder()
                    .id(line.getId())
                    .confirmerName(confirmer.getName())
                    .build();
            gooutLineLists.add(gooutLineList);
        }
        return gooutLineLists;
    }

    @Transactional
    public GooutLineRead read(Integer id) {
        GooutLine gooutLine = gooutLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        return GooutLineRead.builder()
                .confirmerId(gooutLine.getConfirmer().getId())
                .confirmerName(gooutLine.getConfirmer().getName())
                .gooutId(gooutLine.getGoout().getId())
                .comment(gooutLine.getComment())
                .approveTime(gooutLine.getApproveTime())
                .applyTime(gooutLine.getApplyTime())
                .status(gooutLine.getStatus())
                .build();
    }

    @Transactional
    //결재id로 결재라인 조회하기
    public List<GooutLineList> read2(Integer gooutId) {
        List<GooutLine> gooutLines = gooutLineRepository.findByGooutId(gooutId);

        List<GooutLineList> gooutLineLists = new ArrayList<>();
        for (GooutLine gooutLine : gooutLines) {
            GooutLineList gooutLineList = GooutLineList.builder()
                    .id(gooutLine.getId())
                    .confirmerId(gooutLine.getConfirmer().getId())
                    .confirmerName(gooutLine.getConfirmer().getName())
                    .gooutId(gooutLine.getGoout().getId())
                    .comment(gooutLine.getComment())
                    .status(gooutLine.getStatus())
                    .build();
            gooutLineLists.add(gooutLineList);
        }
        return gooutLineLists;
    }


    @Transactional
    public void confirm1(GooutLineConfirm gooutLineConfirm) {
        GooutLine gooutLine = gooutLineRepository.findById(gooutLineConfirm.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!gooutLine.getConfirmer().getId().equals(gooutLineConfirm.getConfirmerId())){
            throw new IllegalArgumentException("결재자이 올바르지 않습니다.");
        }

        if (gooutLine.getStatus() != 0) {
            throw new IllegalArgumentException("결재자1이 결재를 할 수 있는 상태가 아닙니다.");
        }

        gooutLine.setStatus(1);
        gooutLine.setComment(gooutLineConfirm.getComment());
        gooutLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        gooutLineRepository.save(gooutLine); // 변경 사항 저장

    }

    @Transactional
    public void confirm2(GooutLineConfirm gooutLineConfirm) {
        GooutLine gooutLine = gooutLineRepository.findById(gooutLineConfirm.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!gooutLine.getConfirmer().getId().equals(gooutLineConfirm.getConfirmerId())){
            throw new IllegalArgumentException("결재자2가 올바르지 않습니다.");
        }

        gooutLine.setStatus(2);
        gooutLine.setComment(gooutLineConfirm.getComment());
        gooutLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        gooutLineRepository.save(gooutLine); // 변경 사항 저장
    }

    @Transactional
    public void reject1 (GooutLineConfirm gooutLineConfirm) {
        GooutLine gooutLine = gooutLineRepository.findById(gooutLineConfirm.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!gooutLine.getConfirmer().getId().equals(gooutLineConfirm.getConfirmerId())){
            throw new IllegalArgumentException("결재자1이 올바르지 않습니다.");
        }

        if (gooutLine.getStatus() != 0) {
            throw new IllegalArgumentException("결재자1이 결재를 할 수 있는 상태가 아닙니다.");
        }

        gooutLine.setStatus(3);

        gooutLine.setComment(gooutLineConfirm.getComment());

        gooutLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        gooutLineRepository.save(gooutLine); // 변경 사항 저장
    }

    @Transactional
    public void reject2 (GooutLineConfirm gooutLineConfirm) {
        GooutLine gooutLine = gooutLineRepository.findById(gooutLineConfirm.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결재라인이 존재하지 않습니다."));

        if (!gooutLine.getConfirmer().getId().equals(gooutLineConfirm.getConfirmerId())){
            throw new IllegalArgumentException("결재자2가 올바르지 않습니다.");
        }


        gooutLine.setStatus(3);

        gooutLine.setComment(gooutLineConfirm.getComment());

        gooutLine.setApplyTime(localDateTimeInKorea); // applyTime 설정
        gooutLineRepository.save(gooutLine); // 변경 사항 저장
    }


    @Transactional
    public void update(GooutLineUpdateReq gooutLineUpdateReq) {
        GooutLine gooutLine = gooutLineRepository.findById(gooutLineUpdateReq.getId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 휴가/외출 정보를 찾을 수 없습니다."));

        Employee confirmer = employeeRepository.findById(gooutLineUpdateReq.getConfirmerId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 결재자 정보를 찾을 수 없습니다."));

        // 결재라인 정보 업데이트
        gooutLine.setConfirmer(confirmer);
        gooutLineRepository.save(gooutLine);
    }



    @Transactional
    public void delete(Integer id) {
        GooutLine gooutLine = gooutLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 결재라인이 존재하지 않습니다."));

        gooutLineRepository.delete(gooutLine);
    }

}
