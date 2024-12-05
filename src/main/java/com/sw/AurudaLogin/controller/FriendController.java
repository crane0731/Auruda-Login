package com.sw.AurudaLogin.controller;

import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.domain.UserFriend;
import com.sw.AurudaLogin.dto.FriendListResponse;
import com.sw.AurudaLogin.dto.FriendRequest;

import com.sw.AurudaLogin.dto.UserFriendRequest;
import com.sw.AurudaLogin.service.UserFriendService;
import com.sw.AurudaLogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auruda/friends")
public class FriendController {

    private final UserFriendService userFriendService;
    private final UserService userService;


    //친구 요청 등록
    @PostMapping("/request")
    public ResponseEntity<String> requestFriend(@RequestHeader("User-Id") Long userId,@RequestBody FriendRequest request) {

        User user = userService.findById(userId);

        try {
            userFriendService.saveFriend(user, request);

            return ResponseEntity.ok("친구 요청에 성공하였습니다.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    //친구 요청 수락
    @PostMapping("/approve")
    public ResponseEntity<String> approvedRequestFriend(@RequestBody UserFriendRequest request) {


            UserFriend userFriend = userFriendService.getUserFriend(request.getUserEmail(), request.getFriendEmail());
            userFriendService.updateStatusToApproved(userFriend);
            return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }

    //친구 요청 거절
    @PostMapping("/reject")
    public ResponseEntity<String> rejectedRequestFriend( @RequestBody UserFriendRequest request) {

            UserFriend userFriend = userFriendService.getUserFriend(request.getUserEmail(), request.getFriendEmail());
            userFriendService.updateStatusToRejected(userFriend);
            return ResponseEntity.ok("친구 요청을 거절했습니다.");

    }


    //친구 삭제
    @DeleteMapping("/{friend_email}")
    public ResponseEntity<String> deleteFriend(@RequestHeader("User-Id") Long userId, @PathVariable String friend_email) {

        User user = userService.findById(userId);
        String userEmail = user.getEmail();



            UserFriend userFriend = userFriendService.getUserFriend(userEmail,friend_email);

            userFriendService.deleteUserFriend(userFriend);

            return ResponseEntity.ok("친구 삭제를 성공했습니다.");

    }


    // status 상태에 따라 친구리스트 출력(approved)
    @GetMapping("/approve")
    public ResponseEntity<List<FriendListResponse>> approvedFriendsList(@RequestHeader("User-Id") Long userId) {


        User user = userService.findById(userId);
        String userEmail = user.getEmail();

        // UserFriendDTO 리스트에서 user와 friend 중 userEmail과 일치하지 않는 객체만 추출하여 FriendListResponse로 변환
        List<FriendListResponse> friendList = userFriendService.getUserFriendByUserEmailAndApproved(userEmail).stream()
                .map(dto -> {
                    // userEmail과 일치하지 않는 경우 friend나 user 반환
                    if (!dto.getUser().getEmail().equals(userEmail)) {
                        return new FriendListResponse(dto.getUser());
                    } else {
                        return new FriendListResponse(dto.getFriend());
                    }
                })
                .toList(); // 결과를 리스트로 반환

        return ResponseEntity.ok(friendList);
    }

    //status 상태에 따라 친구리스트 출력(pending)
    @GetMapping("/pend")
    public ResponseEntity<List<FriendListResponse>> pendingFriendsList(@RequestHeader("User-Id") Long userId) {

        User user = userService.findById(userId);
        String userEmail = user.getEmail();

        List<FriendListResponse> friendList = userFriendService.getFriendsByUserEmailAndPending(userEmail).stream()
                .map(FriendListResponse::new).toList();

        return ResponseEntity.ok(friendList);
    }

}
