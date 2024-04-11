package com.example.demo.batchTest.config;

import com.example.demo.batchTest.Commute.config.CommuteStepConfiguration;
import com.example.demo.batchTest.Commute.repository.CommuteRepository;
import com.example.demo.batchTest.Employee.config.JpaPagingItemReaderJobConfiguration;
import com.example.demo.batchTest.Goout.config.JpaGooutItemReaderJobConfiguration;
import com.example.demo.batchTest.Overtime.config.JpaOvertimeItemReaderJobConfiguration;
import com.example.demo.batchTest.Salary.config.JpaSalaryJobConfiguration;
import com.example.demo.batchTest.Salary.model.Salary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.HashMap;
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final CommuteRepository commuteRepository;
    private final JpaOvertimeItemReaderJobConfiguration jpaOvertimeItemReaderJobConfiguration;
    private final JpaGooutItemReaderJobConfiguration jpaGooutItemReaderJobConfiguration;
    private final JpaPagingItemReaderJobConfiguration jpaPagingItemReaderJobConfiguration;
    private final JpaSalaryJobConfiguration jpaSalaryJobConfiguration;
    private final CommuteStepConfiguration commuteStepConfiguration;
    @Bean
    public Job jpaPagingItemReaderJobCommute() {
        // job init
        HashMap<Integer, Salary> salaryMap = new HashMap<Integer, Salary>(); // 계산 결과 저장 공간
//        String salaryDate = LocalDate.now().toString(); // 배치 처리한 날 (ex : 2023-01-01)

        return jobBuilderFactory.get("calculateSalaryJob").incrementer(new RunIdIncrementer())
                .start(jpaPagingItemReaderJobConfiguration.jpaPagingItemReaderStep(salaryMap))//직원들을 불러와서 Map 초기화 시킨다.
                .next(commuteStepConfiguration.jpaCommuteItemReaderStep(salaryMap))// 출퇴근 기록 수정
                .next(jpaOvertimeItemReaderJobConfiguration.jpaOvertimeItemReaderStep(salaryMap))//초과근무 기록 수정
                .next(jpaGooutItemReaderJobConfiguration.jpaGooutItemReaderStep(salaryMap))
                .next(jpaSalaryJobConfiguration.jpaSalarySaveStep(salaryMap))
                .build();
    }
}
