package com.example.demo.batchTest.Salary.config;

import com.example.demo.batchTest.Commute.model.dto.CommuteDto;
import com.example.demo.batchTest.Salary.model.Salary;
import com.example.demo.batchTest.Salary.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaSalaryJobConfiguration {
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SalaryRepository salaryRepository;
    private final int chunkSize = 10;
    @Bean
    public Step jpaSalarySaveStep(HashMap<Integer, Salary> salaryMap) {
        return stepBuilderFactory.get("jpaSalarySaveStep").allowStartIfComplete(true) // 빈 이름 변경
                .<Salary, Object[]>chunk(chunkSize)
                .reader(salaryJpaPagingItemReader()) // 메서드 이름 변경
                .writer(salaryJpaPagingItemWriter(salaryMap)) // 메서드 이름 변경
                .build();
    }

    public ItemReader<Salary> salaryJpaPagingItemReader() { // 메서드 이름 변경
//        LocalDate targetDate = LocalDate.of(2023,1,1);
        String queryString = "SELECT c.employee.id, SUM(c.sumTime) " +
                "FROM Commute c " +
                "WHERE MONTH(c.createAt) = MONTH( '2023-01-01' )" +
                "GROUP BY c.employee";


        return new JpaPagingItemReaderBuilder<Salary>()
                .name("salaryJpaPagingItemReader") // 빈 이름 변경
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(queryString)
//                .queryString("SELECT p FROM Commute p")
                .build();
    }

    private ItemWriter<Object[]> salaryJpaPagingItemWriter(HashMap<Integer, Salary> salaryMap) { // 메서드 이름 변경

        List<Salary> salaries = new ArrayList<>();
        return list -> {
            for (Integer id: salaryMap.keySet()) {
                Salary salary = salaryMap.get(id);
                salary.calculateTotalSalary();
                salary.setSalaryDate(LocalDate.now());



                salaries.add(salary);

            }
            salaryRepository.saveAllAndFlush(salaries);
        };
    }
}
