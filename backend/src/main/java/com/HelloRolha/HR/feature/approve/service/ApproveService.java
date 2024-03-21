package com.HelloRolha.HR.feature.approve.service;

import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.approve.model.ApproveFile;
import com.HelloRolha.HR.feature.approve.model.ApproveLine;
import com.HelloRolha.HR.feature.approve.model.dto.Approve.*;
import com.HelloRolha.HR.feature.approve.model.dto.ApproveLine.ReturnStatusRes;
import com.HelloRolha.HR.feature.approve.repo.ApproveFileRepository;
import com.HelloRolha.HR.feature.approve.repo.ApproveLineRepository;
import com.HelloRolha.HR.feature.approve.repo.ApproveRepository;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.goout.model.dto.GooutReturnReq;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ApproveService {
    private final ApproveRepository approveRepository;
    private final ApproveLineRepository approveLineRepository;
    private final ApproveFileRepository approveFileRepository;
    private final EmployeeRepository employeeRepository;
    private final AmazonS3 s3;

    @Value("gurigiri-s3")
    private String bucket;

    public Approve create(ApproveCreateReq approveCreateReq) {
        Employee employee = employeeRepository.findById(approveCreateReq.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("기안자의 ID가 존재하지 않습니다."));

        Approve approve = Approve.builder()
                .content(approveCreateReq.getContent())
                .title(approveCreateReq.getTitle())
                .employee(employee)
                .status(0)
                .build();

        return approveRepository.save(approve);

    }

    @Transactional
    public List<ApproveList> list() {
        List<Approve> approves = approveRepository.findAll();
        List<ApproveList> approveLists = new ArrayList<>();

        for (Approve approve : approves) {
            Employee employee = approve.getEmployee();
            String employeeName = employee != null ? employee.getName() : "Unknown"; // Employee가 null이면 "Unknown"

            String confirmer1Name = "Unknown";
            String confirmer2Name = "Unknown";

            if (approve != null && !approve.getApproveLines().isEmpty()) {
                ApproveLine approveLine = approve.getApproveLines().get(0); // 첫 번째 ApproveLine을 가져옴

                Employee confirmer1 = approveLine.getConfirmer1();
                Employee confirmer2 = approveLine.getConfirmer2();

                confirmer1Name = confirmer1 != null ? confirmer1.getName() : "Unknown"; // confirmer1의 이름을 가져옴
                confirmer2Name = confirmer2 != null ? confirmer2.getName() : "Unknown"; // confirmer2의 이름을 가져옴
            }

//            String confirmer1Name = approve.getApproveLines().get(0).getConfirmer1().getName();
//            String confirmer2Name = approve.getApproveLines().get(1).getConfirmer1().getName();

//            String confirmer1Name = "Unknown"; // 초기값 설정
//            String confirmer2Name = "Unknown"; // 초기값 설정

            if (employee != null) {
                ApproveList approveList = ApproveList.builder()
                    .id(approve.getId())
                    .name(employeeName)
                    .title(approve.getTitle())
                    .content(approve.getContent())
                    .createAt(approve.getCreateAt())
                    .updateAt(approve.getUpdateAt())
                    .confirmer1(confirmer1Name) // confirmer1의 이름 설정
                    .confirmer2(confirmer2Name) // confirmer2의 이름 설정
                    .status(approve.getApproveLines().get(0).getStatus())
                    .build();

            approveLists.add(approveList);
        }
        }
        return approveLists;
    }



    public ApproveRead read(Integer approveId) {
        Approve approve = approveRepository.findById(approveId)
                .orElseThrow(() -> new RuntimeException("결재 정보를 찾을 수 없습니다."));
        String confirmer1Name = "Unknown"; // 초기값 설정
        Integer confirmer1Id = 0;
        String confirmer2Name = "Unknown"; // 초기값 설정
        Integer confirmer2Id = 0;
        //ApproveLine firstApproveLine = approve.getApproveLines().get(0); // 첫 번째 ApproveLine 가져오기
        if (!approve.getApproveLines().isEmpty()) {
            ApproveLine firstApproveLine = approve.getApproveLines().get(0); // 첫 번째 ApproveLine 가져오기
            confirmer1Name = firstApproveLine.getConfirmer1() != null ? firstApproveLine.getConfirmer1().getName() : "N/A";
            confirmer2Name = firstApproveLine.getConfirmer2() != null ? firstApproveLine.getConfirmer2().getName() : "N/A";
            confirmer1Id = firstApproveLine.getConfirmer1() != null ? firstApproveLine.getConfirmer1().getId() : 0;
            confirmer2Id = firstApproveLine.getConfirmer2() != null ? firstApproveLine.getConfirmer2().getId() : 0;


//            confirmer1Name = firstApproveLine.getConfirmer1().getName();
//            confirmer2Name = firstApproveLine.getConfirmer2().getName();
        }
        Employee employee = approve.getEmployee();
        String employeeName = employee != null ? employee.getName() : "Unknown"; // Employee가 null이면 "Unknown"
        return ApproveRead.builder()
                .id(approve.getId())
                .title(approve.getTitle())
                .content(approve.getContent())
                .status(approve.getApproveLines().get(0).getStatus())
                .confirmer1(confirmer1Name) // confirmer1의 이름 설정
                .confirmer2(confirmer2Name) // confirmer2의 이름 설정
                .confirmer1Id(confirmer1Id)
                .confirmer2Id(confirmer2Id)
                .createAt(approve.getCreateAt())
                .updateAt(approve.getUpdateAt())
                .name(employeeName)
                .build();
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
//        return ApproveUpdate.builder()
//                .content(approveUpdate.getContent())
//                .build();
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

    public String uploadFile(MultipartFile file) {
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

            s3.putObject(bucket, saveFileName.replace(File.separator, "/"), input, metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        return s3.getUrl(bucket, saveFileName.replace(File.separator, "/")).toString();
    }

    public void saveFile(Integer id, String uploadPath) {
        approveFileRepository.save(ApproveFile.builder()
                .approve(Approve.builder().id(id).build())
                .filename(uploadPath)
                .build());
    }
}
