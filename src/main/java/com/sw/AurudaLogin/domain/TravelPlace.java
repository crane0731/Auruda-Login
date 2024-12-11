package com.sw.AurudaLogin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "travel_place")
public class TravelPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_place_id")
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name="city", nullable = false)
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name="travel_count")
    private Long travelCount;

    @Enumerated(EnumType.STRING)
    @Column(name="category")
    private Category category;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lng")
    private double lng;

    @Column(name = "photo_url")
    private String photoUrl;


    public void addTravelCount() {
        this.travelCount +=1;
    }

//    @Enumerated(EnumType.STRING)
//    @Column(name = "theme")
//    private Theme theme;

}
