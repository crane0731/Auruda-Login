package com.sw.AurudaLogin.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class UserFriendId implements Serializable {

    private Long userId;
    private Long friendId;

}
