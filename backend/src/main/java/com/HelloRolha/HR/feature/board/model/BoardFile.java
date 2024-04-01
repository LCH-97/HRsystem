package com.HelloRolha.HR.feature.board.model;

import com.HelloRolha.HR.feature.goout.model.Goout;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String filename; // S3에 저장된 파일명 (UUID 포함)
    private String originalFilename; // 원본 파일 이름을 저장할 필드 추가

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Board_id")
    private Board board;
}