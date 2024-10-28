package com.example.backend.dto;

import com.example.backend.entity.HoaDon;
import com.example.backend.entity.HoaDonChiTiet;
import lombok.Data;

import java.util.List;

@Data
public class ChiTietHoaDonDTO {
    List<HoaDonChiTiet> hoaDonChiTiets;
    HoaDon hoaDon;
}
