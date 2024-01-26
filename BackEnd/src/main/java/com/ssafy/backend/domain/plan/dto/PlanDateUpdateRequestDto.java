package com.ssafy.backend.domain.plan.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanDateUpdateRequestDto {

    private LocalDate startDate;

    private LocalDate endDate;
}
