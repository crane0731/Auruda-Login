package com.sw.AurudaLogin.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

        /*
    파일을 업로드하고 접근 가능한 url을 반환

    @param file 업로드할 파일
    @return 업로드된 파일의 url
     */

    String uploadFile(MultipartFile file);
    void deleteImageFile(String fileName);

}
