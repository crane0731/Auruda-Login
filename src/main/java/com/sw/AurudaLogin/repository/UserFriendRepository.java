package com.sw.AurudaLogin.repository;

import com.sw.AurudaLogin.domain.Status;
import com.sw.AurudaLogin.domain.UserFriend;
import com.sw.AurudaLogin.domain.UserFriendId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFriendRepository extends JpaRepository<UserFriend, UserFriendId> {

    Optional<UserFriend> findUserFriendById(UserFriendId userFriendId);





    // friend_id가 특정 값이고 status가 특정 값인 UserFriend 리스트 조회
    List<UserFriend> findByFriend_IdAndStatus(Long friendId, Status status);

    // 특정 userId가 user_id 또는 friend_id에 존재하고, status가 APPROVED인 UserFriend 조회
    List<UserFriend> findByUser_IdOrFriend_IdAndStatus(Long userId, Long friendId, Status status);

}
