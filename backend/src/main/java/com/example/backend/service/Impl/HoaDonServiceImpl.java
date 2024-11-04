package com.example.backend.service.Impl;

import com.example.backend.entity.HoaDon;
import com.example.backend.entity.KhachHang;
import com.example.backend.entity.TrangThai;
import com.example.backend.repository.HoaDonRepository;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonServiceImpl implements HoaDonService {
    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Override
    public ResponseEntity<?> getDanhSachHoaDon(String email, TrangThai trangThai) {
        KhachHang khachHang = khachHangRepository.findByEmail(email);
        List<HoaDon> hoaDons = hoaDonRepository.getDSChoXacNhan(khachHang.getId(), trangThai.getId());
        return ResponseEntity.ok().body(hoaDons);
    }
}
