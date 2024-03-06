package com.HelloRolha.HR.feature.position.model.entity;

import com.HelloRolha.HR.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position extends BaseEntity {
    private Integer positionNum;
    private String positionName;
}
