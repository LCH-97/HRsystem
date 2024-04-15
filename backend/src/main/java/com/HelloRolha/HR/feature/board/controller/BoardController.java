package com.HelloRolha.HR.feature.board.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.common.dto.PaginationReq;
import com.HelloRolha.HR.feature.board.model.Board;
import com.HelloRolha.HR.feature.board.model.dto.*;
import com.HelloRolha.HR.feature.board.service.BoardNotFoundException;
import com.HelloRolha.HR.feature.board.service.BoardService;

import com.HelloRolha.HR.feature.goout.model.dto.GooutFileDto;
import com.HelloRolha.HR.feature.goout.model.dto.GooutRead;
import com.HelloRolha.HR.feature.goout.model.dto.GooutUpdateReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/board")
@CrossOrigin("*")
public class BoardController {

    BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity create(@RequestPart BoardCreateReq boardCreateReq,
                                 @RequestPart(name = "uploadFiles", required = false) MultipartFile[] uploadFiles) {
        Board board = boardService.create(boardCreateReq);

        if (uploadFiles != null) {
            for (MultipartFile uploadFile : uploadFiles) {
                // 파일 업로드 메소드 호출 시 gooutId 전달
                String uploadPath = boardService.uploadFile(uploadFile, board.getId());
            }
        }

        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("공지사항 생성 성공")
                .isSuccess(true)
                .result(board.getId())
                .build();
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ResponseEntity<BaseRes> list(@RequestBody PaginationReq paginationReq) {
        BoardListRes boardListRes = boardService.list(paginationReq.getPage(), paginationReq.getSize());
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("공지사항 확인 성공")
                .isSuccess(true)
                .result(boardListRes)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/check/{id}")
    public ResponseEntity<BaseRes> read(@PathVariable Integer id) {
        BoardReadDto boardReadDto = boardService.read(id);
        if (boardReadDto == null) {
            return ResponseEntity.notFound().build(); // 적절한 예외 처리
        }
        BaseRes response = BaseRes.builder() // 응답 객체 생성
                .code(1200)
                .message("공지사항 상세 확인 성공")
                .isSuccess(true)
                .result(boardReadDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/update")
    public ResponseEntity<BaseRes> update(@RequestPart BoardUpdateReq boardUpdateReq,
                                          @RequestPart(name = "uploadFiles", required = false) MultipartFile[] uploadFiles) {
        boardService.update(boardUpdateReq);

        if (uploadFiles != null) {
            for (MultipartFile uploadFile : uploadFiles) {
                // 파일 업로드 메소드 호출 시 gooutId 전달
                String uploadPath = boardService.uploadFile(uploadFile, boardUpdateReq.getId());
            }
        }

        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("공지사항 수정 성공")
                .isSuccess(true)
                .result(boardUpdateReq)
                .build();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResponseEntity<BaseRes> delete(@PathVariable Integer id) {
        boardService.delete(id);
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("공지사항 삭제 성공")
                .isSuccess(true)
                .result("삭제한 id : " + id)
                .build();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/files/{boardId}")
    public ResponseEntity<List<BoardFileDto>> listFilesByBoardId(@PathVariable Integer boardId) {
        List<BoardFileDto> files = boardService.listFilesByBoardId(boardId);
        return ResponseEntity.ok().body(files);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/files/delete/{fileId}")
    public ResponseEntity<BaseRes> deleteFile(@PathVariable Integer fileId) {
        boardService.deleteFile(fileId);
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("파일 삭제 성공")
                .isSuccess(true)
                .result("삭제한 파일 id : " + fileId)
                .build();
        return ResponseEntity.ok(response);
    }
}