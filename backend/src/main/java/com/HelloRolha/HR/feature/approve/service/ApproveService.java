package com.HelloRolha.HR.feature.approve.service;

import com.HelloRolha.HR.error.BusinessException;
import com.HelloRolha.HR.error.ErrorCode;
import com.HelloRolha.HR.feature.approve.model.Approve;
import com.HelloRolha.HR.feature.approve.model.ApproveFile;
import com.HelloRolha.HR.feature.approve.model.ApproveLine;
import com.HelloRolha.HR.feature.approve.model.dto.approve.*;
import com.HelloRolha.HR.feature.approve.model.dto.approveFile.ApproveFileDto;
import com.HelloRolha.HR.feature.approve.model.dto.approveLine.ApproveLineCreateReq;
import com.HelloRolha.HR.feature.approve.model.dto.approveLine.ApproveLineList;
import com.HelloRolha.HR.feature.approve.repo.ApproveFileRepository;
import com.HelloRolha.HR.feature.approve.repo.ApproveLineRepository;
import com.HelloRolha.HR.feature.approve.repo.ApproveRepository;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ApproveService {
    private final ApproveRepository approveRepository;
    private final ApproveLineRepository approveLineRepository;
    private final ApproveFileRepository approveFileRepository;
    private final EmployeeRepository employeeRepository;
    private final ApproveLineService approveLineService;
    private final AmazonS3 s3;

    @Value("${AWS_S3_BUCKET}")
    private String bucket;

    public ApproveCreateRes create(ApproveCreateReq approveCreateReq) {

        if (approveCreateReq == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "결재 정보는 Null일 수 없습니다.");
        }
        if (approveCreateReq.getTitle() == null || approveCreateReq.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "Title은 Null일 수 없습니다.");
        }
        if (approveCreateReq.getContent() == null || approveCreateReq.getContent().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "Content는 Null일 수 없습니다.");
        }
        if (approveCreateReq.getEmployeeId() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "기안자는 Null일 수 없습니다.");
        }
        if (approveCreateReq.getConfirmer1Id() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "결재자1은 Null일 수 없습니다.");
        }
        if (approveCreateReq.getConfirmer2Id() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "결재자2는 Null일 수 없습니다.");
        }
        Employee employee = employeeRepository.getOne(approveCreateReq.getEmployeeId());

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
public Page<ApproveList> list(Pageable pageable) {
    Page<Approve> approvePage = approveRepository.findAllByOrderByCreateAtDesc(pageable);

        Page<ApproveList> approveListPage = approvePage.map(approve -> {
            Employee employee = approve.getEmployee();
            List<ApproveLineList> approveLineLists = approveLineService.read2(approve.getId());
            Employee confirmer1 = employeeRepository.findById(approveLineLists.get(0).getConfirmerId())
                    .orElseThrow(() -> new IllegalArgumentException("결재자1의 ID가 존재하지 않습니다."));
            Employee confirmer2 = employeeRepository.findById(approveLineLists.get(1).getConfirmerId())
                    .orElseThrow(() -> new IllegalArgumentException("결재자2의 ID가 존재하지 않습니다."));

            return ApproveList.builder()
                    .id(approve.getId())
                    .title(approve.getTitle())
                    .content(approve.getContent())
                    .employeeName(employee.getName())
                    .status(approve.getStatus())
                    .createAt(approve.getCreateAt())
                    .confirmer1Name(confirmer1.getName())
                    .confirmer2Name(confirmer2.getName())
                    .build();

        });
        return approveListPage;
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

    @Transactional
    public void cancel(Integer id) {
        Approve approve = approveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 휴가/외출 정보를 찾을 수 없습니다."));
        approve.setStatus(4);
        approveRepository.save(approve);

        List<ApproveLine> approveLines = approveLineRepository.findByApproveId(id);
        approveLines.get(0).setStatus(4);
        approveLines.get(1).setStatus(4);
        approveLineRepository.save(approveLines.get(0));
        approveLineRepository.save(approveLines.get(1));
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
        String saveFileName = folderPath + "/" + uuid + "_" + originalName;
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
            approveFile.setFileName(saveFileName);
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
    @Transactional
    public String generatePresignedUrl(String fileKey, String fileName) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + 1000 * 60 * 10; // 10분 후 만료
        expiration.setTime(expTimeMillis);

        try {
            // 파일 이름을 URL 인코딩합니다.
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());

            // 파일 다운로드를 위한 Content-Disposition 설정
            String contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFileName);

            // 응답 헤더를 설정합니다.
            ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                    .withContentDisposition(contentDisposition);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileKey)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration)
                    .withResponseHeaders(headerOverrides); // 응답 헤더를 포함시킵니다.

            URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (Exception e) {
            // 예외 처리
            throw new RuntimeException("URL 생성 중 오류 발생", e);
        }
    }

    @Transactional
    public List<ApproveFileDto> listFilesByApproveId(Integer approveId) {
        List<ApproveFile> approveFiles = approveFileRepository.findByApproveId(approveId);

        List<ApproveFileDto> fileDtos = approveFiles.stream().map(file -> {
            String downloadUrl = generatePresignedUrl(file.getFilename(), file.getOriginalFilename());

            return new ApproveFileDto(file.getOriginalFilename(), downloadUrl);
        }).collect(Collectors.toList());

        return fileDtos;
    }
}