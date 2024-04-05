package com.HelloRolha.HR.feature.goout.service;

import com.HelloRolha.HR.feature.goout.model.Holiday;
import com.HelloRolha.HR.feature.goout.repo.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;

    @Transactional
    public Holiday addHoliday(LocalDate date, String description) {
        Holiday holiday = new Holiday();
        holiday.setDate(date);
        holiday.setDescription(description);
        return holidayRepository.save(holiday);
    }

    @Transactional
    public Holiday updateHoliday(Integer id, LocalDate date, String description) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공휴일 정보를 찾을 수 없습니다."));
        holiday.setDate(date);
        holiday.setDescription(description);
        return holidayRepository.save(holiday);
    }

    @Transactional
    public void deleteHoliday(Integer id) {
        holidayRepository.deleteById(id);
    }

    public List<Holiday> findAllHolidays() {
        return holidayRepository.findAll();
    }

    public Holiday findHolidayById(Integer id) {
        return holidayRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공휴일 정보를 찾을 수 없습니다."));
    }
}
