package com.HelloRolha.HR.feature.approve.model.dto.approve;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApproveList {
    private Integer id;
    private String employeeName;
    private String title;
    private String content;
    private int status;
    private LocalDateTime createAt;
    private String confirmer1Name;
    private String confirmer2Name;
}
