package com.example.backend.service;

import com.example.backend.dto.HoaDonDTO;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.HoaDon;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface MuaNgayService {
    ResponseEntity<?> muaNgayCheckOut(SanPhamDto dto);

    ResponseEntity<HoaDon> getBill();


    ResponseEntity datHang(HoaDonDTO dto);
}
