package com.HelloRolha.HR.feature.goout.model;

import com.HelloRolha.HR.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Holiday extends BaseEntity {
    private LocalDate date;     //공휴일의 날짜입니다.
    private String description; //공휴일에 대한 설명입니다. 예를 들어, "크리스마스"나 "신정" 같은 공휴일의 이름을 저장할 수 있습니다.
}
