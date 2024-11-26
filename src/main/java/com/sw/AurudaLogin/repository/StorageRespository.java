package com.sw.AurudaLogin.repository;

import com.sw.AurudaLogin.domain.Storage;
import com.sw.AurudaLogin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageRespository extends JpaRepository<Storage, Long> {

    List<Storage>findByUserId(Long id);
}
