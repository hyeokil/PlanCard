package com.ssafy.backend.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPlanResponseDto {

    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private int planMemberCount;

    private List<String> planMembersName;

}
