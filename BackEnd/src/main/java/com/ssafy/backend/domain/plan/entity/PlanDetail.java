package com.ssafy.backend.domain.plan.entity;

import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.plan.entity.Plan;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private int orderNumber;

    private int day;

    public void update(int orderNumber, int day) {
        this.orderNumber = orderNumber;
        this.day = day;
    }


}
