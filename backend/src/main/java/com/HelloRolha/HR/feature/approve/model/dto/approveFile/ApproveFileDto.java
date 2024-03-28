package com.HelloRolha.HR.feature.approve.model.dto.approveFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveFileDto {
    private String name;
    private String downloadUrl;


}
