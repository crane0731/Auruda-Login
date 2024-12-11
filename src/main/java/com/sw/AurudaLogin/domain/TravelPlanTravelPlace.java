package com.sw.AurudaLogin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlanTravelPlace {

    @EmbeddedId
    private TravelPlanTravelPlaceId id; //복합 키 사용

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("travelPlanId")
    @JoinColumn(name="travel_plan_id")
    private TravelPlan travelPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("travelPlaceId")
    @JoinColumn(name = "travel_place_id")
    private TravelPlace travelPlace;

    @Column(name = "day", nullable = false)
    private String day;

    public TravelPlanTravelPlace update(TravelPlan travelPlan, TravelPlace travelPlace, String day) {
        this.travelPlan = travelPlan;
        this.travelPlace = travelPlace;
        this.day = day;
        return this;
    }

    @Override
    public String toString() {
        return "TravelPlanTravelPlace{" +
                "id=" + id +
                ", travelPlan=" + travelPlan +
                ", travelPlace=" + travelPlace +
                ", day='" + day + '\'' +
                '}';
    }
}
