package com.example.demo.batchTest.Employee.config;

import com.example.demo.batchTest.Commute.model.Commute;
import com.example.demo.batchTest.Employee.model.Employee;
import com.example.demo.batchTest.Goout.model.Goout;
import com.example.demo.batchTest.Overtime.model.Overtime;
import com.example.demo.batchTest.Salary.model.Salary;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

//    @Bean
//    public Job jpaPagingItemReaderJob() {
//        return jobBuilderFactory.get("jpaPagingItemReaderJob").incrementer(new RunIdIncrementer())
//                .start(jpaPagingItemReaderStep()) //직원 데이터 가져오기                 //직원 데이터로 출퇴근 기록 가져오기 && 가져온 걸로 계산하기
//                .on("*").end()
//                .end()
//                .build();
//    }

    @Bean
    public Step jpaPagingItemReaderStep(HashMap<Integer, Salary> salaryMap) {
        return stepBuilderFactory.get("jpaPagingItemReaderStep").allowStartIfComplete(true)
                .<Employee, Employee>chunk(chunkSize)
                .reader(jpaPagingItemReader()) // JobParameters를 전달하기 위해 reader에 null 전달
                .writer(jpaPagingItemWriter(salaryMap))
                .build();
    }

    @Bean
    @StepScope // Step 실행 시점에 JobParameters를 받기 위해 @StepScope 사용
    public JpaPagingItemReader<Employee> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Employee>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Employee p")
//                .parameterValues(Collections.singletonMap("requestDate", requestDate)) // JobParameters에서 받은 requestDate를 쿼리 파라미터에 설정
                .build();
    }

    private ItemWriter<? super Employee> jpaPagingItemWriter(HashMap<Integer,Salary> salaryMap) {
        return list -> {
            for (Employee employee: list) {
                log.info("Current Employee = {}", employee.toString());
                salaryMap.put(employee.getId(),Salary.builder().employee(employee).salaryDate(LocalDate.now(ZoneId.of("Asia/Seoul"))).build());
            }
        };
    }
}