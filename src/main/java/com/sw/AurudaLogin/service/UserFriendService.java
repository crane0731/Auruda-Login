package com.sw.AurudaLogin.service;

import com.sw.AurudaLogin.domain.Status;
import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.domain.UserFriend;

import com.sw.AurudaLogin.domain.UserFriendId;
import com.sw.AurudaLogin.dto.FriendRequest;
import com.sw.AurudaLogin.dto.UserFriendDto;
import com.sw.AurudaLogin.exception.CustomException;
import com.sw.AurudaLogin.exception.ErrorCode;
import com.sw.AurudaLogin.exception.ErrorMessage;
import com.sw.AurudaLogin.repository.UserFriendRepository;
import com.sw.AurudaLogin.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFriendService {

    private final UserFriendRepository userFriendRepository;
    private final UserRepository userRepository;




    //친구 등록
    @Transactional
    public void saveFriend(User user,FriendRequest request) {

        User friend = userRepository.findByEmail(request.getFriendEmail()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_FRIEND));

        // 중복 확인을 위해 UserFriendId 생성
        UserFriendId id = UserFriendId.builder()
                .userId(friend.getId())
                .friendId(user.getId())
                .build();

        // 중복된 친구 관계가 존재하는지 확인
        if (userFriendRepository.findUserFriendById(id).isPresent()) {
            throw new IllegalArgumentException("이미 중복된 신청입니다.");  // 중복이 있을 경우 예외 발생
        }

        UserFriendId userFriendId = UserFriendId.builder()
                .userId(user.getId())
                .friendId(friend.getId())
                .build();

        UserFriend userFriend=UserFriend.builder()
                .id(userFriendId)
                .user(user)
                .friend(friend)
                .status(Status.PENDING)
                .build();

        userFriendRepository.save(userFriend);
    }



    // 조회
    public UserFriend getUserFriend(String userEmail, String friendEmail) {

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));
        User friend = userRepository.findByEmail(friendEmail).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_FRIEND));

        // 첫 번째로 userId와 friendId로 조회
        UserFriendId userFriendId = UserFriendId.builder()
                .userId(user.getId())
                .friendId(friend.getId())
                .build();

        // 첫 번째 조회 시 친구 요청이 없으면, userId와 friendId를 바꿔서 다시 시도
        return userFriendRepository.findUserFriendById(userFriendId)
                .or(() -> { // Java 11+ Optional API를 사용하여 두 번째 조회 시도
                    UserFriendId swappedId = UserFriendId.builder()
                            .userId(friend.getId()) // user와 friend의 ID를 바꿈
                            .friendId(user.getId())
                            .build();
                    return userFriendRepository.findUserFriendById(swappedId);
                })
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_FRIEND_REQUEST));
    }

    //친구요청 상태 변경(수락)
    @Transactional
    public void updateStatusToApproved(UserFriend userFriend){
        userFriend.updateUserFriendStatus(Status.APPROVED);
    }

    //친구요청 상태 변경(거절)
    @Transactional
    public void updateStatusToRejected(UserFriend userFriend){
        userFriend.updateUserFriendStatus(Status.REJECTED);
    }

    //친구 삭제
    @Transactional
    public void deleteUserFriend(UserFriend userFriend){
        userFriendRepository.delete(userFriend);
    }


    public List<UserFriendDto> getUserFriendByUserEmailAndApproved(String userEmail) {

        // userEmail을 통해 User 엔티티를 찾음
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));

        // user_id 또는 friend_id가 user.getId()인 경우와 status가 APPROVED인 경우를 조회
        return userFriendRepository.findByUser_IdOrFriend_IdAndStatus(user.getId(), user.getId(), Status.APPROVED).stream()
                .map(userFriend -> new UserFriendDto(userFriend.getUser(), userFriend.getFriend())) // user와 friend 추출
                .toList(); // 결과를 리스트로 반환
    }

    public List<User> getFriendsByUserEmailAndPending(String userEmail) {

        // userEmail을 통해 User 엔티티를 찾음
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));

        //friend_id와 status로 UserFriend 리스트 조회 후, user 리스트 반환
        return userFriendRepository.findByFriend_IdAndStatus(user.getId(),Status.PENDING).stream()
                .map(UserFriend::getUser)//User(User 객체)를 추출
                .toList();
    }


}
