package com.HelloRolha.HR.feature.position.model.entity;

import com.HelloRolha.HR.common.entity.BaseEntity;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Position extends BaseEntity {
    @Column(unique = true)
    private Integer positionNum;
    private String positionName;

    @OneToMany(mappedBy = "position")
    List<Employee> employees;
}
