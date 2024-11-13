package com.example.backend.controller.admin.donHang;

import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.HoaDon;
import com.example.backend.entity.NhanVien;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.service.BillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/hoaDon/datHang/daGiaoHang")
public class DaGiaoController {
    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @GetMapping("/danhSach")
    public List<HoaDon> listBill4() {
        return hoaDonDatHangService.findHoaDonByTrangThai(4);
    }


    @RequestMapping("/timKiem={search}")
    public List<HoaDon> searchAllBill4(@PathVariable("search") String search) {
        return hoaDonDatHangService.searchAllBill(4, search);
    }

    @RequestMapping("/timKiemNgay={searchDate}")
    public List<HoaDon> searchDateBill4(@PathVariable("searchDate") String searchDate) {
        return hoaDonDatHangService.searchDateBill(4, searchDate);
    }

    @PostMapping("/capNhatTrangThai/huyDon")
    public ResponseEntity<Map<String, Boolean>> updateStatus5(@RequestBody HoaDonDTO hoaDonDTO) {
        Long id = hoaDonDTO.getId();
        String ghiChu = hoaDonDTO.getGhiChu();
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        hoaDonDatHangService.capNhatTrangThaiHuyDon(5, id, ghiChu);
        return ResponseEntity.ok().build();
    }


}
