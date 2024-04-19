package com.HelloRolha.HR.feature.goout.model;

import com.HelloRolha.HR.common.entity.FileBaseEntity;
import com.HelloRolha.HR.feature.goout.model.Goout;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@SuperBuilder
public class GooutFile extends FileBaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Goout_id")
    private Goout goout;

}