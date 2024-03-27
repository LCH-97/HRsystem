package com.HelloRolha.HR.feature.goout.service;


import com.HelloRolha.HR.feature.employee.model.dto.EmployeeDto;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.goout.model.GooutFile;
import com.HelloRolha.HR.feature.goout.model.GooutLine;
import com.HelloRolha.HR.feature.goout.model.GooutType;
import com.HelloRolha.HR.feature.goout.model.dto.*;
import com.HelloRolha.HR.feature.goout.repo.*;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GooutService {
    private final GooutRepository gooutRepository;
    private final GooutFileRepository gooutFileRepository;
    private final GooutLineRepository gooutLineRepository;
    private final GooutTypeRepository gooutTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final AmazonS3 s3;
    private final GooutLineService gooutLineService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public GooutCreateRes create(GooutCreateReq gooutCreateReq) {
        if (gooutCreateReq.getAgentId().equals(gooutCreateReq.getEmployeeId())) {
            throw new IllegalArgumentException("대리자의 ID와 신청직원의 ID는 같을 수 없습니다.");
        }

        GooutCreateRes gooutCreateRes = GooutCreateRes.builder().build();

        Goout goout = Goout.builder()
                .agent(Employee.builder().id(gooutCreateReq.getAgentId()).build())
                .employee(Employee.builder().id(gooutCreateReq.getEmployeeId()).build())
                .writer(Employee.builder().id(gooutCreateReq.getWriterId()).build())
                .gooutType(GooutType.builder().id(gooutCreateReq.getGooutTypeId()).build())
                .first(gooutCreateReq.getFirst())
                .last(gooutCreateReq.getLast())
                .status(0)
                .build();

        gooutCreateRes.setGooutId(gooutRepository.save(goout).getId()) ;

        GooutLineCreateReq gooutLine1 = GooutLineCreateReq.builder()
                .confirmerId(gooutCreateReq.getConfirmer1Id())
                .gooutId(goout.getId())
                .build();
        gooutCreateRes.setGooutLine1Id(gooutLineService.create(gooutLine1).getId());


        GooutLineCreateReq gooutLine2 = GooutLineCreateReq.builder()
                .confirmerId(gooutCreateReq.getConfirmer2Id())
                .gooutId(goout.getId())
                .build();
        gooutCreateRes.setGooutLine2Id( gooutLineService.create(gooutLine2).getId());
        return gooutCreateRes;
    }

    @Transactional
    public List<GooutList> list(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Goout> gooutPage = gooutRepository.findList(pageable);

            List<GooutList> gooutLists = new ArrayList<>();

        for (Goout goout : gooutPage) {
            Employee employee = goout.getEmployee();
            Employee writer = goout.getWriter();
            GooutType gooutType = goout.getGooutType();
            if (employee != null) {
                GooutList gooutList = GooutList.builder()
                        .id(goout.getId())
                        .name(employee.getName())
                        .writerName(writer.getName())
                        .gooutTypeName(gooutType.getName())
                        .status(goout.getStatus())
                        .first(goout.getFirst())
                        .last(goout.getLast())
                        .build();
                gooutLists.add(gooutList);
            }
        }
        return gooutLists;
    }

    @Transactional
    public GooutRead read(Integer id) {
        Optional<Goout> optionalGoout = gooutRepository.findByIdWithDetails(id);

        return optionalGoout.map(goout -> {
            Employee employee = goout.getEmployee();
            if (employee == null) {
                throw new RuntimeException("휴가 신청 직원의 정보를 찾을 수 없습니다.");
            }

            Employee agent = goout.getAgent();
            if (agent == null) {
                throw new RuntimeException("대리인의 정보를 찾을 수 없습니다.");
            }

            Employee writer = goout.getWriter();
            if (writer == null) {
                throw new RuntimeException("글쓴이의 정보를 찾을 수 없습니다.");
            }

            GooutType gooutType = goout.getGooutType();
            if (gooutType == null) {
                throw new RuntimeException("휴가타입 정보를 찾을 수 없습니다.");
            }

            return GooutRead.builder()
                    .id(goout.getId())
                    .agentId(agent.getId())
                    .agentName(agent.getName())
                    .employeeId(employee.getId())
                    .employeeName(employee.getName())
                    .writerId(writer.getId())
                    .writerName(writer.getName())
                    .gooutTypeId(gooutType.getId())
                    .gooutTypeName(gooutType.getName())
                    .status(goout.getStatus())
                    .first(goout.getFirst())
                    .last(goout.getLast())
                    .build();
        }).orElse(null);
    }

    @Transactional
    public void returnStatus(GooutReturnReq gooutReturnReq) {

        Goout goout = gooutRepository.findById(gooutReturnReq.getId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 휴가/외출 정보를 찾을 수 없습니다."));

        goout.setStatus(gooutReturnReq.getStatus());
        gooutRepository.save(goout);
    }

    @Transactional
    public void update(GooutUpdateReq gooutUpdateReq) {
        Goout goout = gooutRepository.findById(gooutUpdateReq.getId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 휴가/외출 정보를 찾을 수 없습니다."));

        if (goout.getStatus() != 3) {
            throw new IllegalStateException("반려된 상태의 휴가/외출 정보만 수정할 수 있습니다.");
        }

        GooutType gooutType = gooutTypeRepository.findById(gooutUpdateReq.getGooutTypeId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 휴가타입이 존재하지 않습니다."));

        Employee employee = employeeRepository.findById(gooutUpdateReq.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 휴가가는 직원이 존재하지 않습니다."));

        Employee writer = employeeRepository.findById(gooutUpdateReq.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 글쓴이이 존재하지 않습니다."));

        Employee agent = employeeRepository.findById(gooutUpdateReq.getAgentId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 대리자가 존재하지 않습니다."));

        // 휴가/외출 정보 업데이트
        goout.setFirst(gooutUpdateReq.getFirst());
        goout.setLast(gooutUpdateReq.getLast());
        goout.setGooutType(gooutType);
        goout.setEmployee(employee);
        goout.setWriter(writer);
        goout.setAgent(agent);
        gooutRepository.save(goout);
    }

    @Transactional
    public void delete(Integer id) {
        Goout goout = gooutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 휴가/외출 정보를 찾을 수 없습니다."));

        gooutFileRepository.deleteAllByGoout(goout);

        gooutRepository.delete(goout);
    }

    @Transactional
    public void cancel(Integer id) {
        Goout goout = gooutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 휴가/외출 정보를 찾을 수 없습니다."));
        goout.setStatus(4);
        gooutRepository.save(goout);

        List<GooutLine> gooutLines = gooutLineRepository.findByGooutId(id);
        gooutLines.get(0).setStatus(4);
        gooutLines.get(1).setStatus(4);
        gooutLineRepository.save(gooutLines.get(0));
        gooutLineRepository.save(gooutLines.get(1));
    }

    @Transactional
    public String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        return folderPath;
    }


    @Transactional
    public String uploadFile(MultipartFile file, Integer gooutId) {
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

            Goout goout = gooutRepository.findById(gooutId)
                    .orElseThrow(() -> new RuntimeException("해당 휴가/외출 신청이 존재하지 않습니다."));

            GooutFile gooutFile = new GooutFile();
            gooutFile.setFilename(saveFileName);
            gooutFile.setOriginalFilename(originalName);
            gooutFile.setGoout(goout); // 찾아온 Goout 엔티티 사용
            gooutFileRepository.save(gooutFile);

        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 중 오류 발생", e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("입력 스트림 닫기 실패", e);
            }
        }

        return s3.getUrl(bucket, saveFileName).toString();
    }

    @Transactional
    public void saveFile(Integer id, String uploadPath) {
        gooutFileRepository.save(GooutFile.builder()
                .goout(Goout.builder().id(id).build())
                .filename(uploadPath)
                .build());
    }

    @Transactional
    public Integer getPaidVacationCount(LocalDate startDate, LocalDate endDate, EmployeeDto employee) {
        Integer counter = 0;
        //Todo 비효율적인 쿼리임. 바꿀 수 있으면 바꾸자.a
        //sql 문을 month 에 맞는 데이터만 가져오도록 만들 수 있다.
        List<Goout> gooutList = gooutRepository.findAllByEmployee(Employee.builder().id(employee.getId()).build());
        if(gooutList.isEmpty()){

            System.out.println("오류 예외 처리해라");
            return 0;
        }

        for (Goout goout:gooutList){
            //휴가 출발일부터 마지막 날까지 반복,  월급을 계산하는 날 사이에 있다면
            for(LocalDate date = goout.getFirst(); !date.isEqual(goout.getLast()) ;date=date.plusDays(1)){
                if( date.isAfter(startDate) && date.isBefore(endDate)){
                    counter++;
                }
            }
        }
        return counter;
    }

    @Transactional
    public String generatePresignedUrl(String fileKey, String fileName) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + 1000 * 60 * 10; // 10분 후 만료
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileKey)
                .withMethod(com.amazonaws.HttpMethod.GET)
                .withExpiration(expiration);

        // 파일 다운로드를 위한 Content-Disposition 설정 추가
        generatePresignedUrlRequest.addRequestParameter("response-content-disposition", "attachment; filename=\"" + fileName + "\"");

        URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Transactional
    public List<GooutFileDto> listFilesByGooutId(Integer gooutId) {
        // Goout 엔티티의 ID를 기반으로 파일 목록 조회
        List<GooutFile> gooutFiles = gooutFileRepository.findByGooutId(gooutId);

        // 조회된 파일 목록을 GooutFileDto 리스트로 변환
        List<GooutFileDto> fileDtos = gooutFiles.stream().map(file -> {
            // 각 파일에 대한 Presigned URL 생성
            String downloadUrl = generatePresignedUrl(file.getFilename(), file.getOriginalFilename());

            // 파일의 원본 이름과 다운로드 URL을 사용하여 GooutFileDto 객체 생성
            return new GooutFileDto(file.getOriginalFilename(), downloadUrl);
        }).collect(Collectors.toList());

        return fileDtos;
    }
}

