package com.ssafy.backend.domain.plan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailListResponseDto implements Comparable<PlanDetailListResponseDto>{

    private Long id;

    private Long cardId;

    private String placePosition;

    private String cardMemo;

    private String placeName;

    private String placeAddress;

    private String placeImage;

    private Double latitude;

    private Double logitude;

    private int orderNumber;

    private int day;

    @Override
    public int compareTo(PlanDetailListResponseDto other) {
        // 먼저 day를 비교하고, day가 같으면 orderNumber를 비교
        int dayComparison = Integer.compare(this.day, other.day);
        if (dayComparison != 0) {
            return dayComparison;
        }

        return Integer.compare(this.orderNumber, other.orderNumber);
    }
}
