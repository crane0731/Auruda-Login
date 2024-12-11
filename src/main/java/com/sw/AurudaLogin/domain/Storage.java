package com.sw.AurudaLogin.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "storage_name")
    private String storageName;

    @OneToMany(mappedBy = "storage",cascade = CascadeType.ALL,orphanRemoval = true)
    @Column(name = "storage_travel_places")
    @Builder.Default // 기본값을 유지하도록 설정
    private List<StorageTravelPlace> storageTravelPlaces = new ArrayList<>();

    @CreatedDate //엔티티가 생성될 때 생성 시간 저장
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    public void addStorageTravelPlace(StorageTravelPlace storageTravelPlace) {
        storageTravelPlaces.add(storageTravelPlace);
        storageTravelPlace.setStorage(this);
    }

    public void removeStorageTravelPlace(StorageTravelPlace storageTravelPlace) {
        storageTravelPlaces.remove(storageTravelPlace);
        storageTravelPlace.setStorage(null);
    }

    public void deleteAllStorageTravelPlace() {
        if (storageTravelPlaces!= null) {
            for (StorageTravelPlace storageTravelPlace : new ArrayList<>(storageTravelPlaces)) { // replies 리스트를 복사하여 순회
                removeStorageTravelPlace(storageTravelPlace); // 개별적으로 removeReply 메서드 호출하여 관계를 정리하면서 삭제
            }
        }
    }
}
