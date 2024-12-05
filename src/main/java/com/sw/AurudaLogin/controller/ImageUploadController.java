package com.sw.AurudaLogin.controller;


import com.sw.AurudaLogin.dto.image.ImageResponseDto;
import com.sw.AurudaLogin.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auruda/")
public class ImageUploadController {

    private final StorageService storageService;
    /**
     *
     * 이미지 업로드 API
     *
     * @Param image 업로들할 이미지 파일
     * @return 업로드된 이미지의 URL
     *
     *
     */

    //이미지를 저장하고 url 반환
    @PostMapping("/image")
    public ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile image) {
        try{
            String imageUrl = storageService.uploadFile(image);
            return ResponseEntity.ok().body(new ImageResponseDto(imageUrl));
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //이미지 url을 받아 해딩 이미지를 삭제
    @DeleteMapping("/image")
    public ResponseEntity<String> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        storageService.deleteImageFile(imageUrl);
        return ResponseEntity.ok().body("이미지 삭제 성공");
    }



}
