package com.example.backend.controller.customer.banHang;

import com.example.backend.entity.HoaDon;
import com.example.backend.service.DatHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hoaDon/taiQuay/daHuy")
@CrossOrigin("*")
public class DonHangHuyController {

    @Autowired
    DatHangService datHangService;
    @GetMapping("/danhSach")
    public List<HoaDon> listHD(){
        return datHangService.findHoaDonByTrangThai(2);
    }
}
