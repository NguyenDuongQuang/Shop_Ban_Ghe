package com.example.backend.service;

import com.example.backend.entity.TrangThai;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface HoaDonService {
    ResponseEntity<?> getDanhSachHoaDon(String email, TrangThai trangThai);
}
