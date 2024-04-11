package com.example.demo.batchTest.Goout.config;

import com.example.demo.batchTest.Commute.model.dto.CommuteDto;
import com.example.demo.batchTest.Goout.model.Goout;
import com.example.demo.batchTest.Goout.model.GooutDto;
import com.example.demo.batchTest.Salary.model.Salary;
import lombok.RequiredArgsConstructor;
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
import java.util.Collections;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaGooutItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

//    @Bean
//    public Job jpaPagingItemReaderJobGoout() {
//        return jobBuilderFactory.get("jpaPagingItemReaderJobGoout").incrementer(new RunIdIncrementer())
//                .start(jpaGooutItemReaderStep()).on("*").end().end()
//                .build();
//    }

    @Bean
    public Step jpaGooutItemReaderStep(HashMap<Integer, Salary> salaryMap) {

        return stepBuilderFactory.get("jpaPagingItemReaderStepGoout").allowStartIfComplete(true) // 빈 이름 변경
                .<Goout, Object[]>chunk(chunkSize)
                .reader(jpaPagingItemReaderGoout()) // 메서드 이름 변경
                .writer(jpaPagingItemWriterGoout(salaryMap)) // 메서드 이름 변경
                .build();
    }

    @Bean
    public JpaPagingItemReader<Goout> jpaPagingItemReaderGoout() { // 메서드 이름 변경
        String queryString = "SELECT "
                + "h.employee.id, "
                + "SUM(DATEDIFF(h.last, h.first) + 1) AS duration "
                + "FROM Goout h "
                + "WHERE h.status = 2 "
                + "GROUP BY h.employee.id";

        return new JpaPagingItemReaderBuilder<Goout>()
                .name("jpaPagingItemReaderGoout") // 빈 이름 변경
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(queryString) // Goout 엔티티에 맞게 변경
                .build();
    }

    @Bean
    public ItemWriter<Object[]> jpaPagingItemWriterGoout(HashMap<Integer, Salary> salaryMap) {
        return list -> {
            for (Object[] goout : list) {

                GooutDto gooutDto =GooutDto.builder()
                        .employeeId((Integer) goout[0])
                        .duration((Long) goout[1]).build();
                log.info("Current Goout = {}", gooutDto.toString());
                salaryMap.get(gooutDto.getEmployeeId()).setPaidVacationCount( gooutDto.getDuration());
            }
        };
    }
}
