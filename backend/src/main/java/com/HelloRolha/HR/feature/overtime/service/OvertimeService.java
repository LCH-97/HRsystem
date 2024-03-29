package com.HelloRolha.HR.feature.overtime.service;

import com.HelloRolha.HR.error.ErrorCode;
import com.HelloRolha.HR.error.exception.CoummuteSQLException;
import com.HelloRolha.HR.error.exception.OvertimeSQLException;
import com.HelloRolha.HR.error.exception.getEmployeeInSecurityContextHolderError;
import com.HelloRolha.HR.feature.employee.model.dto.EmployeeDto;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.overtime.model.Overtime;
import com.HelloRolha.HR.feature.overtime.model.dto.CreateOvertimeReq;
import com.HelloRolha.HR.feature.overtime.model.dto.CreateOvertimeRes;
import com.HelloRolha.HR.feature.overtime.model.dto.OvertimeDto;
import com.HelloRolha.HR.feature.overtime.repository.OvertimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OvertimeService {

    private final OvertimeRepository overtimeRepository;

    @Autowired
    public OvertimeService(OvertimeRepository overtimeRepository) {
        this.overtimeRepository = overtimeRepository;
    }

    public CreateOvertimeRes processOvertimeRequest(CreateOvertimeReq createOvertimeReq) {
        try{
            Employee employee = ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }catch (Exception e){
            throw new getEmployeeInSecurityContextHolderError(ErrorCode.EMPLOYEE_GET_FAIL_IN_SECURITYCONTEXTHOLDER,e.getMessage());
        }

        try{

            Overtime overtime = overtimeRepository.save(Overtime.builder()
                    .date(createOvertimeReq.getDate())
                    .shift(createOvertimeReq.getShift())
                    .startTime(createOvertimeReq.getStartTime())
                    .endTime(createOvertimeReq.getEndTime())
                    .reason(createOvertimeReq.getReason())
                    .status("대기 중")
                    .employee(employee)
                    .build());
            return CreateOvertimeRes.builder().id(overtime.getId()).build();
        }catch (Exception e){
            throw new OvertimeSQLException(ErrorCode.DB_ERROR_SQL,e.getMessage());
        }



    }

    public List<OvertimeDto> list() {
        List<Overtime> overtimes = overtimeRepository.findList();
        List<OvertimeDto> overtimeDtos = new ArrayList<>();

        for (Overtime overtime : overtimes) {
            if (overtime != null) {
                OvertimeDto overtimeDto = OvertimeDto.builder()
                        .id(overtime.getId())
                        .shift(overtime.getShift())
                        .startTime(overtime.getStartTime())
                        .endTime(overtime.getEndTime())
                        .date(overtime.getDate())
                        .reason(overtime.getReason())
                        .status(overtime.getStatus())
                        .build();
                overtimeDtos.add(overtimeDto);
            }
        }
        return overtimeDtos;
    }


    public OvertimeDto read(Integer id) {
        Overtime overtime = overtimeRepository.findById(id).orElseThrow(()->new OvertimeNotFoundException(""));

        return OvertimeDto.builder()
                .id(overtime.getId())
                .shift(overtime.getShift())
                .startTime(overtime.getStartTime())
                .endTime(overtime.getEndTime())
                .date(overtime.getDate())
                .reason(overtime.getReason())
                .status(overtime.getStatus())
                .build();
    }


    public void update(Integer id, OvertimeDto overtimeDto) {
        Optional<Overtime> result = overtimeRepository.findById(id);
        if(result.isPresent()) {
            Overtime overtime = result.get();
            overtime.setShift(overtimeDto.getShift());
            overtime.setDate(overtimeDto.getDate());
            overtime.setStartTime(overtimeDto.getStartTime());
            overtime.setEndTime(overtimeDto.getEndTime());
            overtime.setReason(overtimeDto.getReason());
            overtime.setStatus("대기 중");
            overtimeRepository.save(overtime);
        }
    }

    public void approveOvertime(Integer id) {
        Overtime overtime = overtimeRepository.findById(id)
                .orElseThrow(() -> new OvertimeNotFoundException("초과 근무를 찾을 수 없습니다."));

        // 초과 근무 승인 시 상태를 "승인됨"으로 변경합니다.
        overtime.setStatus("승인됨");
        overtimeRepository.save(overtime);
    }


    public Long getOverTimeOf(Employee employee, LocalDate startDate, LocalDate endDate ) {


        //Todo 예외 처리 : 결과가 비어있다면
        Long totalMinutes = 0L;
        for(Overtime overtime:employee.getOvertimes()){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate overtimeDate = LocalDate.parse(overtime.getDate(), formatter);

            if(overtimeDate.isAfter(startDate) && overtimeDate.isBefore(endDate)){

                LocalTime startTime = LocalTime.parse(overtime.getStartTime());
                LocalTime endTime = LocalTime.parse(overtime.getEndTime());
                // 시작 시간과 종료 시간의 Duration 계산
                Duration duration = Duration.between(startTime, endTime);

                // 총 업무 시간 계산
                totalMinutes += duration.toMinutes();

            }
        }
//        long hours = totalMinutes / 60; // 시간 계산
//        long minutes = totalMinutes % 60; // 분 계산

        // 시간과 분을 문자열로 결합하여 sumTime에 저장
//        String sumTime = String.format("%d h %d m", hours, minutes);
        return totalMinutes / 60;
    }

}