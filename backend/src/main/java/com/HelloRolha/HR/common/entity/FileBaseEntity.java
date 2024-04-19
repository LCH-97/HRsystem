package com.HelloRolha.HR.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FileBaseEntity extends BaseEntity{
    private String originalFilename; // 원본 파일 이름을 저장할 필드 추가
    private String fileName; // S3에 저장된 파일명 (UUID 포함)
    private String path;
}
