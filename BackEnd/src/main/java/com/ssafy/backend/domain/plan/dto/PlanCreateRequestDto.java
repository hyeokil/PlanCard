package com.ssafy.backend.domain.plan.dto;

import com.ssafy.backend.domain.plan.entity.Plan;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PlanCreateRequestDto {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "시작일은 필수 입력값입니다.")
    private LocalDate startDate;

    @NotBlank(message = "종료일은 필수 입력값입니다.")
    private LocalDate endDate;

    // DTO -> Entity
    public Plan toEntity() {
        return Plan.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}