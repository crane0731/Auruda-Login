package com.sw.AurudaLogin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class TravelPlanTravelPlaceId implements Serializable {

    @Column(name = "travel_plan_id")  // 필드 매핑
    private Long travelPlanId;

    @Column(name = "travel_place_id")  // 필드 매핑
    private Long travelPlaceId;


}
