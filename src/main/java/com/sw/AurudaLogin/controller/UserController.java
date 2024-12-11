package com.sw.AurudaLogin.controller;

import com.sw.AurudaLogin.config.jwt.TokenProvider;
import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.dto.*;
import com.sw.AurudaLogin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auruda/users")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    //자신의 정보 조회
    @GetMapping("/me")
    public ResponseEntity<Object> getMyDetails(@RequestHeader("User-Id") Long userId){


        User user = userService.findById(userId);



        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .point(user.getPoint())
                .Grade(user.getGrade().getCommentary())
                .createdAt(user.getCreatedAt().toString())
                .role(user.getRole().toString())
                .build();

        return ResponseEntity.ok(userResponse);

    }
    //회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> UserDeactivation(@RequestHeader("User-Id") Long userId){



        //유저 찾기
        User user = userService.findById(userId);

        userService.deleteUser(user.getId());
        return ResponseEntity.ok("회원 탈퇴에 성공했습니다.");

    }

    //회원 수정(닉네임,이미지)
    @PutMapping("/me")
    public ResponseEntity<Object> updateUser(@RequestHeader("User-Id") Long userId,@Valid @RequestBody UpdateUserRequest request, BindingResult  bindingResult){
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        // 1. 유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
        }

        // 5. 오류 메시지가 존재하면 이를 반환
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body(errorMessages);
        }


        //유저 찾기
        User user = userService.findById(userId);

        userService.update(user.getId(), request);

        return ResponseEntity.ok("회원 수정  성공");

    }

    //회원 비밀번호 수정
    @PutMapping("/me/password")
    public ResponseEntity<Object> updateUserPassword(@RequestHeader("User-Id") Long userId, @Valid @RequestBody PassworUpdateRequestDto request, BindingResult  bindingResult){
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        // 1. 유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
        }

        // 4. 비밀번호 일치 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            errorMessages.put("passwordCheck", "비밀번호가 일치하지 않습니다.");
        }

        // 5. 오류 메시지가 존재하면 이를 반환
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body(errorMessages);
        }

        User user = userService.findById(userId);

        userService.updatePassword(user.getId() ,request);

        return ResponseEntity.ok().body("비밀 번호 수정 성공");
    }


    //회원 리스트 조회(관리자 전용)
    @GetMapping("/admin")
    public ResponseEntity<List<AdminUserListResponse>> getUserList(){
        List<AdminUserListResponse> users = userService.findAll().stream().map(AdminUserListResponse::new).toList();
        return ResponseEntity.ok().body(users);
    }

    //회원 상세 조회(관리자 전용)
    @GetMapping("/admin/{user_id}")
    public ResponseEntity<Object> getUser(@PathVariable Long user_id){


        User user = userService.findById(user_id);

        AdminUserResponse adminUserResponse = AdminUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .point(user.getPoint())
                .Grade(user.getGrade().getCommentary())
                .createdAt(user.getCreatedAt().toString())
                .role(user.getRole().toString())
                .build();

        return ResponseEntity.ok(adminUserResponse);

    }

    //회원 삭제(관리자)
    @DeleteMapping("/admin/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id){

        System.out.println("ddsd");

        User user = userService.findById(user_id);
        // 서비스에서 유저 삭제
        userService.deleteUser(user.getId());

        // 삭제 성공 시 204 No Content 반환
        return ResponseEntity.ok("회원을 삭제했습니다.");

    }





}