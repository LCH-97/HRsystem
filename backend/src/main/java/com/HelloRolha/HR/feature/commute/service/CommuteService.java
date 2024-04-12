package com.HelloRolha.HR.feature.commute.service;


import com.HelloRolha.HR.error.ErrorCode;
import com.HelloRolha.HR.error.UserNotFoundException;
import com.HelloRolha.HR.error.exception.AlreadyCommuteException;
import com.HelloRolha.HR.error.exception.CommuteNotFoundException;
import com.HelloRolha.HR.error.exception.CoummuteSQLException;
import com.HelloRolha.HR.feature.commute.model.Commute;
import com.HelloRolha.HR.feature.commute.model.dto.CommuteCheckRes;
import com.HelloRolha.HR.feature.commute.model.dto.CommuteDto;
import com.HelloRolha.HR.feature.commute.repository.CommuteRepository;
import com.HelloRolha.HR.feature.employee.model.dto.EmployeeDto;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CommuteService {
    private final CommuteRepository commuteRepository;

    //redis 도입으로 추가된 부분
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(CommuteService.class);
    //redis 도입으로 추가된 부분

    @Transactional
    public CommuteDto commute() {
        //자신의 id를 가져오는 법
        Employee employee = ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println("\nEmployee id :" + employee.getId() + "\n");
        //오늘 출근을 한 상태라면 못해야한다.
        if (todayCheck(employee)) {
            throw AlreadyCommuteException.alreadyCommuteException();
        }

        Commute commute = Commute.builder()
                .employee(employee)
                .build();

        try {
            commute = commuteRepository.save(commute);

            String commuteKey = "commute:" + employee.getId();
            String commuteIdKey = "commuteId:" + employee.getId();
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            try {
                // Redis에 출근 시간 저장 시도
                redisTemplate.opsForValue().set(commuteKey, currentTime, 16, TimeUnit.HOURS);
                redisTemplate.opsForValue().set(commuteIdKey, commute.getId().toString(), 16, TimeUnit.HOURS);
            } catch (Exception e) {
                // Redis 서버 접근 실패 시, 로그 남기기
                log.error("Redis 서버 접근 실패: ", e);
            }
        } catch (Exception e) {
            throw new CoummuteSQLException(ErrorCode.DB_ERROR_SQL, "Commute Create Fail");
        }

        return CommuteDto.builder()
                .id(commute.getId())
                .startTime(commute.getCreateAt())
                .build();
    }

    @Transactional
    public CommuteDto leave(Integer id) {
        Employee employee = ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Commute commute = commuteRepository.findById(id)
                .orElseThrow(() -> new CommuteNotFoundException("Fail to leave : NotFoundException"));

//        commute.setEndTime(LocalDateTime.now()); // 현재 시간으로 설정

        // 출근 시간과 퇴근 시간의 Duration 계산
        Duration duration = Duration.between(commute.getCreateAt(), LocalDateTime.now());

        // 총 업무 시간 계산
        long totalMinutes = duration.toMinutes();
        long hours = totalMinutes / 60; // 시간 계산
        long minutes = totalMinutes % 60; // 분 계산

        // 시간과 분을 문자열로 결합하여 sumTime에 저장

        String sumTime = String.format("%d:%d", hours, minutes);

        commute.setSumTime(totalMinutes);

        Commute updatedCommute = commuteRepository.save(commute);

        //redis 도입으로 추가된 부분
        String employeeId = String.valueOf(updatedCommute.getEmployee().getId());
        String leaveKey = "leave:" + employeeId;
        String leaveTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String sumTimeKey = "sumTime:" + employeeId;
        String commuteIdKey = "commuteId:" + employee.getId();
        try {
            // Redis에 퇴근 시간, 근무합계 저장 시도
            redisTemplate.opsForValue().set(commuteIdKey, commute.getId().toString(), 16, TimeUnit.HOURS); // 16시간 만료 시간 설정
            redisTemplate.opsForValue().set(leaveKey, leaveTime, 16, TimeUnit.HOURS); // 16시간 만료 시간 설정
            redisTemplate.opsForValue().set(sumTimeKey, sumTime, 16, TimeUnit.HOURS); // 16시간 만료 시간 설정
        } catch (Exception e) {
            // Redis 서버 접근 실패 시, 로그 남기기
            log.error("Redis 서버 접근 실패: ", e);
        }
        //redis 도입으로 추가된 부분

        return CommuteDto.builder()
                .id(updatedCommute.getId())
                .startTime(updatedCommute.getCreateAt())
                .endTime(updatedCommute.getUpdateAt())

                .sumTime(sumTime)
                .employeeName(employee.getName())

                .build();
    }

    public Boolean todayCheck(Employee employee) {

        Optional<Commute> Commutes = commuteRepository.findByEmployee(employee);
// 오늘 출근한 기록이 있음. 단 퇴근까지 판단하지 않음.
        return Commutes.isPresent();


    }

    public CommuteCheckRes check() {
        Employee employee = ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        try {
            String commuteKey = "commute:" + employee.getId();
            String leaveKey = "leave:" + employee.getId();
            String sumTimeKey = "sumTime:" + employee.getId();
            String commuteIdKey = "commuteId:" + employee.getId();


            // Redis에서 출근 정보 조회
            String startTimeRedis = redisTemplate.opsForValue().get(commuteKey);
            String endTimeRedis = redisTemplate.opsForValue().get(leaveKey);
            String sumTimeRedis = redisTemplate.opsForValue().get(sumTimeKey);
            String commuteIdRedis = redisTemplate.opsForValue().get(commuteIdKey);
            Boolean isCommuteRedis = startTimeRedis != null;
            Boolean isLeaveRedis = endTimeRedis != null;

            // 최종 값 초기화
            String finalStartTime = startTimeRedis;
            String finalEndTime = endTimeRedis;
            String finalSumTime = sumTimeRedis;
            Boolean finalIsCommute = isCommuteRedis;
            Boolean finalIsLeave = isLeaveRedis;

            return CommuteCheckRes.builder()
                    .id(Integer.parseInt(commuteIdRedis))
                    .isCommute(finalIsCommute)
                    .isLeave(finalIsLeave)
                    .startTime(finalStartTime)
                    .endTime(finalEndTime)
                    .sumTime(finalSumTime)

                    .build();
        } catch (Exception e) {
            // Redis에서 정보를 찾을 수 없는 경우 DB에서 조회
            log.error("Redis not working:",e);
            // DB에 접근해서 해결
            return checkInDb(employee);
        }


//
//
//                // Redis에 정보 저장
//                redisTemplate.opsForValue().set(commuteKey, finalStartTime, 16, TimeUnit.HOURS); // 16시간 만료 시간 설정
//                if (finalEndTime != null) {
//                    redisTemplate.opsForValue().set(leaveKey, finalEndTime, 16, TimeUnit.HOURS); // 16시간 만료 시간 설정
//                }
//                if (finalSumTime != null) {
//                    redisTemplate.opsForValue().set(sumTimeKey, finalSumTime, 16, TimeUnit.HOURS); // 16시간 만료 시간 설정
//                }
//

    }

    public CommuteCheckRes checkInDb(Employee employee){
        LocalDate today = LocalDate.now();

        Optional<Commute> optionalCommuteToday = commuteRepository.findByEmployee(employee);

        if (optionalCommuteToday.isPresent()) {
            Commute commuteToday = optionalCommuteToday.get();
            if(commuteToday.isLeave())
                return CommuteCheckRes.builder()
                    .id(commuteToday.getId())
                    .isCommute(true)
                    .isLeave(commuteToday.isLeave())
                    .startTime(commuteToday.getCreateAt().toString())
                    .endTime(commuteToday.getEndTime())
                    .sumTime(commuteToday.getSumTimeByHours())
                    .build();
            else{
                return CommuteCheckRes.builder()
                        .id(commuteToday.getId())
                        .isCommute(true)
                        .isLeave(commuteToday.isLeave())
                        .startTime(commuteToday.getCreateAt().toString())
                        .endTime(commuteToday.getEndTime())
                        .sumTime("0:00")
                        .build();
            }
        }else {
            return CommuteCheckRes.builder()
                    .isCommute(false)
                    .isLeave(false)
                    .startTime("출근 버튼을 눌러주세요.")

                    .build();
        }
    }


    // 결과 반환


    public List<CommuteDto> mainlist() {
        List<Commute> commutes = commuteRepository.findAll();
        List<CommuteDto> commuteDtos = new ArrayList<>();

        for (Commute commute : commutes) {
            if (commute != null && commute.getEmployee() != null) { // commute, employee, 출근시간이 null이 아닌 경우
                CommuteDto commuteDto = CommuteDto.builder()
                        .id(commute.getEmployee().getId())
                        .employeeName(commute.getEmployee().getName())
                        .build();
                commuteDtos.add(commuteDto);
            }
        }
        return commuteDtos;
    }


    public Long getWorkTimeByMinutes(LocalDate startDate, LocalDate endDate, EmployeeDto employee) {
        Long counter = 0L;
        //Todo 비효율적인 쿼리임. 바꿀 수 있으면 바꾸자.
        //sql 문을 month 에 맞는 데이터만 가져오도록 만들 수 있다.
        List<Commute> Commutes = commuteRepository.findAllByEmployee(Employee.builder().id(employee.getId()).build());
        if (Commutes.isEmpty()) {

            System.out.println("오류 예외 처리해라");
            return counter;
        }

        for (Commute commute : Commutes) {
            if (startDate.isBefore(commute.getCreateAt().toLocalDate()) && endDate.isAfter(commute.getCreateAt().toLocalDate())) {


                // 하루 일한 총 시간 - 휴식 시간해야됨
//                long totalMinutes = duration.toMinutes();
                // 만약 8시간이 넘어간다면?

                counter += commute.getSumTime();
            }

        }
        return counter;
    }

    @Transactional
    public void test() {
        Employee employee = ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
