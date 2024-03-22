package com.HelloRolha.HR.feature.approve.model.dto.Approve;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApproveUpdate {
    private Integer id;

    private String content;


}
