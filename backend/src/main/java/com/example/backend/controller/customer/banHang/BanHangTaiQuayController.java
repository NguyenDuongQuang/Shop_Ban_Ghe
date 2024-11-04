package com.example.backend.controller.customer.banHang;

import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.HoaDon;
import com.example.backend.repository.*;
import com.example.backend.service.BanHangTaiQuayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/banHang/taiQuay")
public class BanHangTaiQuayController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private BanHangTaiQuayService banHangTaiQuayService;

    @GetMapping("/danhSachHoaDon")
    public List<HoaDon>getHoaDon(){
        List<HoaDon> list=hoaDonRepository.getDanhSachHoaDonCho();
        return list;
    }

    @PostMapping("/taoHoaDon")
    public ResponseEntity<?>add(@RequestBody HoaDonDTO hoaDonDTO){
        return banHangTaiQuayService.addHoaDon(hoaDonDTO);
    }

    @PostMapping("/xoaHoaDon")
    public List<HoaDon> xoaHoaDon(@RequestBody HoaDonDTO hoaDonDTO) {
        return banHangTaiQuayService.deleteHoaDon(hoaDonDTO);
    }
    @PostMapping("/huyDon")
    public ResponseEntity huyDon(@RequestBody HoaDonDTO hoaDonDTO) {
        return banHangTaiQuayService.huyHoaDon(hoaDonDTO);
    }


}
