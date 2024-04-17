package com.HelloRolha.HR.feature.board.service;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.board.controller.BoardController;
import com.HelloRolha.HR.feature.board.model.dto.BoardCreateReq;
import com.HelloRolha.HR.feature.board.repository.BoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class BoardServiceTest {
    @InjectMocks
    private BoardService boardService;
    @Mock
    private BoardRepository boardRepository;

    BoardServiceTest() {
    }

    @BeforeEach
    void setUp() {
    }

//    @Test
//    @DisplayName("공지사항 생성")
//    void testCreateBoardSuccess() {
//        BoardController boardController = new BoardController(this.boardService);
//        BoardCreateReq boardCreateReq = BoardCreateReq.builder().title("board").text("boardText").build();
//        ResponseEntity<BaseRes> responseEntity = boardController.create(boardCreateReq, (MultipartFile[])null);
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    }
}
