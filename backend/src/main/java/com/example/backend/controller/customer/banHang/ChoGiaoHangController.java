package com.example.backend.controller.customer.banHang;

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
@RequestMapping("/hoaDon/datHang/choGiaoHang")
public class ChoGiaoHangController {
    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    NhanVienRepository nhanVienRepository;
    @GetMapping("/danhSach")
    public List<HoaDon> listBill2() {
        return hoaDonDatHangService.findHoaDonByTrangThai(2);
    }

    @PostMapping("/capNhatTrangThai/dangGiaoHang")
    public ResponseEntity<Map<String, Boolean>> updateStatus3(@RequestBody HoaDonDTO hoaDonDTO) {
        Long id = hoaDonDTO.getId();
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        hoaDonDatHangService.updateTrangThai(3, id);
//        hoaDonDatHangService.createTimeLine("Xác nhận giao đơn hàng", 3, id, nhanVien.getHoTen());
        return ResponseEntity.ok().build();
    }
}
