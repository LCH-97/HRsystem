package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GooutFileDto {
    private String name;
    private String downloadUrl;

    public GooutFileDto(String name, String downloadUrl) {
        this.name = name;
        this.downloadUrl = downloadUrl;
    }
}
