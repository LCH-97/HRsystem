package com.HelloRolha.HR.feature.approve.model;

import com.HelloRolha.HR.common.entity.ApproveLineBaseEntity;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveLine extends ApproveLineBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "confirmer1_id")
    private Employee confirmer1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "confirmer2_id")
    private Employee confirmer2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approve_id")
    private Approve approve;



}
