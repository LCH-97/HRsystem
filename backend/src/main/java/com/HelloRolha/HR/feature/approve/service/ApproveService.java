package com.HelloRolha.HR.feature.approve.service;

import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.approve.model.ApproveFile;
import com.HelloRolha.HR.feature.approve.model.dto.Approve.*;
import com.HelloRolha.HR.feature.approve.model.dto.ApproveLine.ApproveLineCreateReq;
import com.HelloRolha.HR.feature.approve.model.dto.ApproveLine.ApproveLineList;
import com.HelloRolha.HR.feature.approve.repo.ApproveFileRepository;
import com.HelloRolha.HR.feature.approve.repo.ApproveRepository;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ApproveService {
    private final ApproveRepository approveRepository;
    private final ApproveFileRepository approveFileRepository;
    private final EmployeeRepository employeeRepository;
    private final ApproveLineService approveLineService;
    private final AmazonS3 s3;

    @Value("gurigiri-s3")
    private String bucket;

    public ApproveCreateRes create(ApproveCreateReq approveCreateReq) {
        Employee employee = employeeRepository.findById(approveCreateReq.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("기안자의 ID가 존재하지 않습니다."));

        ApproveCreateRes approveCreateRes = ApproveCreateRes.builder().build();
        Approve approve = Approve.builder()
                .content(approveCreateReq.getContent())
                .title(approveCreateReq.getTitle())
                .employee(employee)
                .status(0)
                .build();

        approveCreateRes.setApproveId(approveRepository.save(approve).getId());

        ApproveLineCreateReq approveLine1 = ApproveLineCreateReq.builder()
                .confirmerId(approveCreateReq.getConfirmer1Id())
                .approveId(approve.getId())
                .build();
        approveCreateRes.setApproveLine1Id(approveLineService.create(approveLine1).getId());

        ApproveLineCreateReq approveLine2 = ApproveLineCreateReq.builder()
                .confirmerId(approveCreateReq.getConfirmer2Id())
                .approveId(approve.getId())
                .build();
        approveCreateRes.setApproveLine2Id(approveLineService.create(approveLine2).getId());
        return approveCreateRes;
    }

    @Transactional
    public List<ApproveList> list() {
        List<Approve> approves = approveRepository.findAll();
        List<ApproveList> approveLists = new ArrayList<>();

        for (Approve approve : approves) {
            Employee employee = approve.getEmployee();

            if (employee != null) {
                List<ApproveLineList> approveLineLists = approveLineService.read2(approve.getId());
                Employee confirmer1 = employeeRepository.findById(approveLineLists.get(0).getConfirmerId())
                        .orElseThrow(() -> new IllegalArgumentException("결재자1의 ID가 존재하지 않습니다."));
                Employee confirmer2 = employeeRepository.findById(approveLineLists.get(1).getConfirmerId())
                        .orElseThrow(() -> new IllegalArgumentException("결재자2의 ID가 존재하지 않습니다."));

                ApproveList approveList = ApproveList.builder()
                        .id(approve.getId())
                        .title(approve.getTitle())
                        .content(approve.getContent())
                        .employeeName(employee.getName())
                        .status(approve.getStatus())
                        .createAt(approve.getCreateAt())
                        .confirmer1Name(confirmer1.getName())
                        .confirmer2Name(confirmer2.getName())
                        .build();
                approveLists.add(approveList);

            }
        }
        return approveLists;
    }



    public ApproveRead read(Integer Id) {
        Optional<Approve> optionalApprove = approveRepository.findById(Id);

        return optionalApprove.map(approve -> {
            Employee employee = approve.getEmployee();
            if (employee == null) {
                throw new RuntimeException("기안자의 정보를 찾을 수 없습니다.");
            }
            return ApproveRead.builder()
                    .id(approve.getId())
                    .title(approve.getTitle())
                    .content(approve.getContent())
                    .status(approve.getStatus())
                    .employeeId(employee.getId())
                    .employeeName(employee.getName())
                    .createAt(approve.getCreateAt())
                    .build();
        }).orElse(null);
    }


    @Transactional
    public void update(ApproveUpdate approveUpdate) {
        Approve approve = approveRepository.findById(approveUpdate.getId())
                .orElseThrow(() -> new RuntimeException("결재 정보를 찾을 수 없습니다."));

        if (approve.getStatus() != 3) {
            throw new IllegalStateException("반려된 상태의 결재만 수정할 수 있습니다.");
        }

        approve.setContent(approveUpdate.getContent());
        approve.setUpdateAt(approve.getUpdateAt());
        approveRepository.save(approve);
    }

    @Transactional
    public void returnStatus(ApproveReturn approveReturn) {

        Approve approve = approveRepository.findById(approveReturn.getId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 결재 정보를 찾을 수 없습니다."));

        approve.setStatus(approveReturn.getStatus());
        approveRepository.save(approve);
    }

    @Transactional
    public void delete(Integer approveId) {
        Approve approve = approveRepository.findById(approveId)
                .orElseThrow(() -> new RuntimeException("결재 정보를 찾을 수 없습니다."));
        approveFileRepository.deleteByApprove(approve); // ApproveFile 레코드 먼저 삭제
        approveRepository.delete(approve); // 이후 Approve 레코드 삭제
    }



    public String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        return folderPath;
    }

    public String uploadFile(MultipartFile file, Integer approveId) {
        String originalName = file.getOriginalFilename();
        String folderPath = makeFolder();
        String uuid = UUID.randomUUID().toString();
        String saveFileName = folderPath + File.separator + uuid + "_" + originalName;
        InputStream input = null;
        try {
            input = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            s3.putObject(bucket, saveFileName, input, metadata);

            Approve approve = approveRepository.findById(approveId)
                    .orElseThrow(() -> new RuntimeException("해당 결재가 존재하지 않습니다."));

            ApproveFile approveFile = new ApproveFile();
            approveFile.setFilename(saveFileName);
            approveFile.setOriginalFilename(originalName);
            approveFile.setApprove(approve);
            approveFileRepository.save(approveFile);

        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 중 오류 발생", e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException("입력 스트림 닫기 실패", e);
            }
        }
        return s3.getUrl(bucket, saveFileName).toString();
    }

    public void saveFile(Integer id, String uploadPath) {
        approveFileRepository.save(ApproveFile.builder()
                .approve(Approve.builder().id(id).build())
                .filename(uploadPath)
                .build());
    }
}