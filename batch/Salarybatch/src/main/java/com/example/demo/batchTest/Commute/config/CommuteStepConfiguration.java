package com.example.demo.batchTest.Commute.config;


import com.example.demo.batchTest.Commute.model.dto.CommuteDto;
import com.example.demo.batchTest.Commute.repository.CommuteRepository;
import com.example.demo.batchTest.Employee.config.JpaPagingItemReaderJobConfiguration;
import com.example.demo.batchTest.Goout.config.JpaGooutItemReaderJobConfiguration;
import com.example.demo.batchTest.Overtime.config.JpaOvertimeItemReaderJobConfiguration;
import com.example.demo.batchTest.Salary.config.JpaSalaryJobConfiguration;
import com.example.demo.batchTest.Salary.model.Salary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CommuteStepConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final CommuteRepository commuteRepository;
    private final JpaOvertimeItemReaderJobConfiguration jpaOvertimeItemReaderJobConfiguration;
    private final JpaGooutItemReaderJobConfiguration jpaGooutItemReaderJobConfiguration;
    private final JpaPagingItemReaderJobConfiguration jpaPagingItemReaderJobConfiguration;
    private final JpaSalaryJobConfiguration jpaSalaryJobConfiguration;
    private int chunkSize = 10;

//    @Bean
//    public Job jpaPagingItemReaderJobCommute() {
//        HashMap<Integer, Salary> salaryMap = new HashMap<Integer, Salary>(); // 계산 결과 저장 공간
//        String salaryDate = LocalDate.now().toString(); // 배치 처리한 날 (ex : 2023-01-01)
//        return jobBuilderFactory.get("jpaPagingItemReaderJobCommute").incrementer(new RunIdIncrementer())
//                .start(jpaPagingItemReaderJobConfiguration.jpaPagingItemReaderStep(salaryMap))//직원들을 불러와서 Map 초기화 시킨다.
//                .next(jpaCommuteItemReaderStep(salaryMap))// 출퇴근 기록 수정
//                .next(jpaOvertimeItemReaderJobConfiguration.jpaOvertimeItemReaderStep(salaryMap))//초과근무 기록 수정
//                .next(jpaGooutItemReaderJobConfiguration.jpaGooutItemReaderStep(salaryMap))
//                .next(jpaSalaryJobConfiguration.jpaSalarySaveStep(salaryMap))
//                .build();
//    }

    @Bean
    public Step jpaCommuteItemReaderStep(HashMap<Integer, Salary> salaryMap) {
        return stepBuilderFactory.get("jpaPagingItemReaderStepCommute").allowStartIfComplete(true) // 빈 이름 변경
                .<CommuteDto, Object[]>chunk(chunkSize)
                .reader(jpaPagingItemReaderCommute()) // 메서드 이름 변경
                .writer(jpaPagingItemWriterCommute(salaryMap)) // 메서드 이름 변경
                .build();
    }

    @Bean
    public ItemReader<CommuteDto> jpaPagingItemReaderCommute() { // 메서드 이름 변경
//        LocalDate targetDate = LocalDate.of(2023,1,1);
        String queryString = "SELECT c.employee.id, SUM(c.sumTime) " +
                "FROM Commute c " +
                "WHERE MONTH(c.createAt) = MONTH( '"
                +LocalDate.now(ZoneId.of("Asia/Seoul"))+
                "' )" +
                "GROUP BY c.employee";


        return new JpaPagingItemReaderBuilder<CommuteDto>()
                .name("jpaPagingItemReaderCommute") // 빈 이름 변경
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString(queryString)
//                .queryString("SELECT p FROM Commute p")
                .build();
    }

    private ItemWriter<Object[]> jpaPagingItemWriterCommute(HashMap<Integer,Salary> salaryMap) { // 메서드 이름 변경
//        List<Commute> commutes = new ArrayList<>();
        return list -> {
            for (Object[] result: list) {
                CommuteDto commute =CommuteDto.builder()
                        .employeeId((Integer) result[0])
                        .totalSumTime((Long)result[1])
                        .build();
               log.info("Current Commute = {}", commute.getTotalSumTime());
                salaryMap.get(commute.getEmployeeId()).setCommuteCount(commute.getTotalSumTime());

//                commutes.add(commute);
            }
//            commuteRepository.saveAllAndFlush(list);
        };
    }
}
