package com.sw.AurudaLogin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class StorageTravelPlaceId {

    @Column(name ="storage_id")
    private Long storageId;
    @Column(name = "travel_place_id")
    private Long travelPlaceId;
}
