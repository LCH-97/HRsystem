package com.example.demo.batchTest.Goout.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GooutDto {
    private Long duration;
    private Integer employeeId;
}
