package com.example.backend.service.Impl;

import com.example.backend.entity.HoaDon;
import com.example.backend.repository.HoaDonRepository;
import com.example.backend.service.DatHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatHangServiceImpl implements DatHangService {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Override
    public List<HoaDon> findHoaDonByTrangThai(long trang_thai_id) {
        List<HoaDon> hoaDonList = hoaDonRepository.findHoaDonByTrangThai(trang_thai_id);
        return hoaDonList;
    }
}
