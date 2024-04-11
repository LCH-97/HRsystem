package com.example.demo.batchTest.Overtime.config;

import com.example.demo.batchTest.Commute.model.dto.CommuteDto;
import com.example.demo.batchTest.Overtime.model.Overtime;
import com.example.demo.batchTest.Overtime.model.OvertimeDto;
import com.example.demo.batchTest.Overtime.repository.OvertimeRepository;
import com.example.demo.batchTest.Salary.model.Salary;
import com.example.demo.batchTest.Salary.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaOvertimeItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final OvertimeRepository overtimeRepository;

    private final SalaryRepository salaryRepository;
    private int chunkSize = 10;

//    @Bean
//    public Job jpaPagingItemReaderJobOvertime(Step jpaOvertimeItemReaderStep) {
//        return jobBuilderFactory.get("jpaPagingItemReaderJobOvertime").incrementer(new RunIdIncrementer())
//                .start(jpaOvertimeItemReaderStep).on("*").end().end()
//                .build();
//    }

    @Bean
    public Step jpaOvertimeItemReaderStep(HashMap<Integer, Salary> salaryMap) {
        return stepBuilderFactory.get("jpaPagingItemReaderStepOvertime").allowStartIfComplete(true) // 빈 이름 변경
                .<Overtime, Object[]>chunk(chunkSize)
                .reader(jpaPagingItemReaderOvertime()) // 메서드 이름 변경
//                .processor(jpaPagingItemProcessorOvertime()) // 메서드 이름 변경
                .writer(jpaPagingItemWriterOvertime(salaryMap)) // 메서드 이름 변경 및 파라미터 추가
                .build();
    }

    @Bean
    public JpaPagingItemReader<Overtime> jpaPagingItemReaderOvertime() {
        String queryString = "SELECT c.employee.id, SUM(c.sumTime) " +
                "FROM Overtime c " +
                "WHERE MONTH(c.date) = MONTH( '"+
                LocalDate.now(ZoneId.of("Asia/Seoul"))+
                "' )" +
                "GROUP BY c.employee";

        return new JpaPagingItemReaderBuilder<Overtime>()
                .name("jpaPagingItemReaderOvertime")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(queryString)
                .build();
    }

    @Bean
    public ItemProcessor<Overtime, Overtime> jpaPagingItemProcessorOvertime() {
        return new ItemProcessor<Overtime, Overtime>() {
            @Override
            public Overtime process(Overtime overtime) throws Exception {

                Long sumTime = calculateSumTime(overtime.getStartTime(), overtime.getEndTime());
                overtime.setSumTime(sumTime); // 계산된 총 업무 시간을 설정

                log.info("Total Sum Time = {}", sumTime);
                // startTime과 endTime을 기반으로 총 업무 시간 계산

                overtimeRepository.save(overtime);
                return overtime;
            }

            // 업무 시간을 계산하는 메서드
            private Long calculateSumTime(String startTime, String endTime) {


                // 분으로 반환
                return 60*2L;
            }
        };
    }

    @Bean
    public ItemWriter<Object[]> jpaPagingItemWriterOvertime(HashMap<Integer,Salary> salaryMap) {
        return list ->  {
            for (Object[] result: list) {
                OvertimeDto overtime =OvertimeDto.builder()
                        .employeeId((Integer) result[0])
                        .totalSumTime((Long)result[1])
                        .build();
                log.info("Current overtime = {}", overtime.getTotalSumTime());

                salaryMap.get(overtime.getEmployeeId()).setOverTime(overtime.getTotalSumTime());

//                commutes.add(commute);
            }
//            private Long totalMinutes = 0L;
//
//            @Override
//            public void write(List<? extends Overtime> items) throws Exception {
//                 각 Overtime 항목에 대한 로그 출력 및 총 업무 시간 계산
//                for (Overtime overtime : items) {
//                    log.info("Current Overtime = {}, Sum Time = {}", overtime.toString(), overtime.getSumTime());
//                    totalMinutes += overtime.getSumTime();
//
//                    // 각 Overtime 객체의 sumTime을 DB에 저장
////                    saveOvertimeToDatabase(overtime);
//                }
//
//                // 총 업무 시간을 포맷하여 출력
//                String totalTimeFormatted = formatTotalTime(totalMinutes);
//                log.info("Total Sum Time = {}", totalTimeFormatted);
//
//                // Salary 엔티티를 조회하여 총 초과 근무 시간을 업데이트
//                updateTotalOvertimeInSalary(totalMinutes);
//            }
//
//            private void saveOvertimeToDatabase(Overtime overtime) {
//                // Overtime 객체를 DB에 저장
//                // 여기에서는 JPA를 사용하여 저장
//                overtimeRepository.save(overtime);
//            }
//
//            private void updateTotalOvertimeInSalary(long totalMinutes) {
//                Optional<Salary> optionalSalary = salaryRepository.findByEmployeeId(1L);
//                if (optionalSalary.isPresent()) {
//                    Salary salary = optionalSalary.get();
//                    salary.setOverTime(totalMinutes);
//                    salaryRepository.save(salary);
//                } else {
//                    throw new RuntimeException("Salary entity with employee_id = 1 not found");
//                }
//            }
//
//            private long convertToMinutes(String sumTime) {
//                String[] parts = sumTime.split(":");
//                long hours = Long.parseLong(parts[0]);
//                long minutes = Long.parseLong(parts[1]);
//                return hours * 60 + minutes;
//            }
//
//            private String formatTotalTime(long totalMinutes) {
//                long hours = totalMinutes / 60;
//                long minutes = totalMinutes % 60;
//                return String.format("%02d:%02d", hours, minutes);
//            }
        };
    }

}

