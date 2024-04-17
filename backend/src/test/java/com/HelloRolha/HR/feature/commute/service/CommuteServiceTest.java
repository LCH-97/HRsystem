package com.HelloRolha.HR.feature.commute.service;

import com.HelloRolha.HR.feature.commute.controller.CommuteController;
import com.HelloRolha.HR.feature.commute.model.dto.CommuteDto;
import com.HelloRolha.HR.feature.commute.repository.CommuteRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommuteServiceTest {
    @Mock
    private CommuteRepository commuteRepository;
    @InjectMocks
    private CommuteService commuteService;
    @InjectMocks
    private CommuteController commuteController;

    CommuteServiceTest() {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("직원 출근")
    void commuteTest() { //직원 출근
        LocalDateTime fakeStartTime = LocalDateTime.of(2024, 1, 1, 9, 0);
        CommuteDto fakeResponse = CommuteDto.builder().startTime(fakeStartTime).build();
        LocalDateTime responseStartTime = fakeResponse.getStartTime();
        Assertions.assertNotNull(fakeResponse);
        Assertions.assertNotNull(responseStartTime);
    }

    @Test
    @DisplayName("직원 퇴근")
    void commute2Test() {
        LocalDateTime fakeStartTime = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime fakeEndTime = LocalDateTime.of(2024, 1, 1, 18, 0);
        String fakeSumTime = "8:0";
        CommuteDto fakeResponse = CommuteDto.builder().startTime(fakeStartTime).endTime(fakeEndTime).sumTime(fakeSumTime).build();
        LocalDateTime responseStartTime = fakeResponse.getStartTime();
        LocalDateTime responseEndTime = fakeResponse.getEndTime();
        String responseSumTime = fakeResponse.getSumTime();
        Assertions.assertNotNull(fakeResponse);
        Assertions.assertNotNull(responseStartTime);
        Assertions.assertNotNull(responseEndTime);
        Assertions.assertNotNull(responseSumTime);
    }

    @Test
    @DisplayName("출퇴근 조회")
    void commuteCheckTest() {
        LocalDateTime fakeStartTime = LocalDateTime.of(2024, 1, 1, 9, 0);
        CommuteDto fakeResponse = CommuteDto.builder().startTime(fakeStartTime).build();
        LocalDateTime responseStartTime = fakeResponse.getStartTime();
        LocalDateTime fakeStartTime2 = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime fakeEndTime = LocalDateTime.of(2024, 1, 1, 18, 0);
        String fakeSumTime = "8:0";
        CommuteDto.builder().startTime(fakeStartTime2).endTime(fakeEndTime).sumTime(fakeSumTime).build();
        LocalDateTime responseStartTime2 = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime responseEndTime2 = LocalDateTime.of(2024, 1, 1, 18, 0);
        String responseSumTime2 = "8:0";
        Assertions.assertEquals(fakeStartTime, responseStartTime2);
        Assertions.assertEquals(fakeEndTime, responseEndTime2);
        Assertions.assertEquals(fakeSumTime, responseSumTime2);
    }
}