package com.HelloRolha.HR.feature.refreshToken.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String token;
}
