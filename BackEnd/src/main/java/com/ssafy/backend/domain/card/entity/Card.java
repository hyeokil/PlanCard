package com.ssafy.backend.domain.card.entity;

import com.ssafy.backend.domain.place.entity.Place;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @OneToOne(fetch = FetchType.LAZY)
    private Place place;

    private String memo;


}
