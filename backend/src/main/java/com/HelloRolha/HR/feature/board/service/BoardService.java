package com.HelloRolha.HR.feature.board.service;



import com.HelloRolha.HR.error.BusinessException;
import com.HelloRolha.HR.error.ErrorCode;
import com.HelloRolha.HR.feature.approve.model.dto.approve.ApproveRead;
import com.HelloRolha.HR.feature.board.model.Board;
import com.HelloRolha.HR.feature.board.model.BoardFile;
import com.HelloRolha.HR.feature.board.model.dto.*;
import com.HelloRolha.HR.feature.board.repository.BoardFileRepository;
import com.HelloRolha.HR.feature.board.repository.BoardRepository;

import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.goout.model.GooutFile;
import com.HelloRolha.HR.feature.goout.model.dto.GooutCreateReq;
import com.HelloRolha.HR.feature.goout.model.dto.GooutFileDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final EmployeeRepository employeeRepository;
    private final AmazonS3 s3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private void validateBoardCreateReq(BoardCreateReq boardCreateReq) {
        if (boardCreateReq == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "공지사항 정보는 null일 수 없습니다.");
        }
        if (boardCreateReq.getTitle() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "제목은 null일 수 없습니다.");
        }
        if (boardCreateReq.getText() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "글 내용은 null일 수 없습니다.");
        }
    }

    @Transactional
    public Board create(BoardCreateReq boardCreateReq) {

        validateBoardCreateReq(boardCreateReq);

        Employee writer = ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        Board board = Board.builder()
                .writer(Employee.builder().id(writer.getId()).build()) // writer ID를 여기에 사용
                .title(boardCreateReq.getTitle())
                .text(boardCreateReq.getText())
                .build();
        return boardRepository.save(board);
    }

    @Transactional
    public BoardListRes list(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Board> boardPage = boardRepository.findList(pageable);

        List<BoardListReq> boardListReqs = new ArrayList<>();

        for (Board board : boardPage.getContent()) {
            Employee writer = board.getWriter();
            if (board != null) {
                BoardListReq boardListReq = BoardListReq.builder()
                        .id(board.getId())
                        .writerName(writer.getName())
                        .title(board.getTitle())
                        .createAt(board.getCreateAt())
                        .updateAt(board.getUpdateAt())
                        .build();
                boardListReqs.add(boardListReq);
            }
        }
        return BoardListRes.builder()
                .boards(boardListReqs)
                .totalElements(boardPage.getTotalElements()) // 전체 요소의 수
                .build();
    }

    @Transactional
    public BoardReadDto read(Integer id) {
        Optional<Board> optionalBoard = boardRepository.findByIdWithDetails(id);

        return optionalBoard.map(board -> {
            Employee writer = board.getWriter();
            if (writer == null) {
                throw new RuntimeException("작성자의 정보를 찾을 수 없습니다.");
            }
            return BoardReadDto.builder()
                .id(board.getId())
                .writerName(writer.getName())
                .writerId(writer.getId())
                .title(board.getTitle())
                .text(board.getText())
                .createAt(board.getCreateAt())
                .updateAt(board.getUpdateAt())
                .build();
        }).orElse(null);
    }

    @Transactional
    public void update(BoardUpdateReq boardUpdateReq) {
        Board board = boardRepository.findById(boardUpdateReq.getId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 공지사항을 찾을 수 없습니다."));
        board.setTitle(boardUpdateReq.getTitle());
        board.setText(boardUpdateReq.getText());
        boardRepository.save(board);
    }

    @Transactional
    public void delete(Integer id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 공지사항을 찾을 수 없습니다."));

        boardFileRepository.deleteAllByBoard(board);
        boardRepository.delete(board);
    }

    @Transactional
    public String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        return folderPath;
    }


    @Transactional
    public String uploadFile(MultipartFile file, Integer boardId) {
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

            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("해당 공지사항이 존재하지 않습니다."));

            BoardFile boardFile = new BoardFile();
            boardFile.setFilename(saveFileName);
            boardFile.setOriginalFilename(originalName);
            boardFile.setBoard(board); // 찾아온 Goout 엔티티 사용
            boardFileRepository.save(boardFile);

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

    public void saveFile(Integer id, String uploadPath) {
        boardFileRepository.save(BoardFile.builder()
                .board(Board.builder().id(id).build())
                .filename(uploadPath)
                .build());
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
    public List<BoardFileDto> listFilesByBoardId(Integer boardId) {
        // Board 엔티티의 ID를 기반으로 파일 목록 조회
        List<BoardFile> boardFiles = boardFileRepository.findByBoardId(boardId);

        // 조회된 파일 목록을 BoardFileDto 리스트로 변환
        List<BoardFileDto> fileDtos = boardFiles.stream().map(file -> {
            // 각 파일에 대한 Presigned URL 생성
            String downloadUrl = generatePresignedUrl(file.getFilename(), file.getOriginalFilename());

            // 파일의 원본 이름과 다운로드 URL을 사용하여 GooutFileDto 객체 생성
            return new BoardFileDto(file.getId(), file.getOriginalFilename(), downloadUrl);
        }).collect(Collectors.toList());

        return fileDtos;
    }

    @Transactional
    public void deleteFile(Integer fileId) {
        boardFileRepository.findById(fileId).ifPresent(file -> {
            // S3에서 파일 삭제
            s3.deleteObject(bucket, file.getFilename());
            // 데이터베이스에서 파일 정보 삭제
            boardFileRepository.delete(file);
        });
    }
}