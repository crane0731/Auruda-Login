package com.sw.AurudaLogin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StorageTravelPlace {

    @EmbeddedId
    private StorageTravelPlaceId id; //복합키 사용

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storageId") //복합키와 매핑
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("travelPlaceId") //복합키와 매핑
    @JoinColumn(name = "travel_place_id", nullable = false)
    private TravelPlace travelPlace;

}
