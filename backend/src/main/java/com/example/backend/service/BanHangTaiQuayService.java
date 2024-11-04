package com.example.backend.service;

import com.example.backend.dto.HoaDonChiTietDTO;
import com.example.backend.dto.HoaDonDTO;
import com.example.backend.dto.KhachHangDTO;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.HoaDon;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface BanHangTaiQuayService {
    ResponseEntity<?>addHoaDon(HoaDonDTO hoaDonDTO);

    ResponseEntity themSP(SanPhamDto sanPhamDto);

    List<HoaDon>deleteHoaDon(HoaDonDTO hoaDonDTO);

    ResponseEntity<?>addKH(KhachHangDTO khachHangDTO);

    ResponseEntity huyHoaDon(HoaDonDTO hoaDonDTO);

    ResponseEntity thanhToan(HoaDonDTO hoaDonDTO);

    ResponseEntity<?>deleteHDCT(HoaDonChiTietDTO hoaDonChiTietDTO);
}
