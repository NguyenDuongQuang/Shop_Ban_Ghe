package com.example.backend.controller.customer.banHang;

import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.HoaDon;
import com.example.backend.entity.NhanVien;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.service.BillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/hoaDon/datHang/dangGiaoHang")
public class DangGiaoHangController {
    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @GetMapping("/danhSach")
    public List<HoaDon> listBill3() {
        return hoaDonDatHangService.findHoaDonByTrangThai(3);
    }

    @PostMapping("/capNhatTrangThai/daGiaoHang")
    public List<HoaDon> updateStatus4(@RequestBody HoaDonDTO hoaDonDTO) {
        Long id = hoaDonDTO.getId();
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        hoaDonDatHangService.updateTrangThai(4, id);
//        hoaDonDatHangService.createTimeLine("Xác nhận đã giao hàng", 4, id,nhanVien.getHoTen());
        return hoaDonDatHangService.findHoaDonByTrangThai(3);

    }
    @RequestMapping("/timKiem={search}")
    public List<HoaDon> searchAllBill3(@PathVariable("search") String search) {
        return hoaDonDatHangService.searchAllBill(3, search);
    }

    @RequestMapping("/timKiemNgay={searchDate}")
    public List<HoaDon> searchDateBill3(@PathVariable("searchDate") String searchDate) {
        return hoaDonDatHangService.searchDateBill(3, searchDate);
    }

}
