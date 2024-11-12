package com.example.backend.service;

import com.example.backend.dto.GioHangDTO;
import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.HoaDon;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BanHangOnlineService {
    ResponseEntity<?> checkout(GioHangDTO dto);

    ResponseEntity<HoaDon> getHoaDon(long id_hoa_don);

    ResponseEntity<?> getHoaDonChiTiet(long id_hoa_don);

    ResponseEntity<HoaDon> getBill();

    ResponseEntity<?> addDiscount(HoaDonDTO hoaDonDTO);

    ResponseEntity datHang(HoaDonDTO dto);
}
