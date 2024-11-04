package com.example.backend.service;

import com.example.backend.entity.HoaDon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DatHangService {
    List<HoaDon> findHoaDonByTrangThai(long trang_thai_id);
}
