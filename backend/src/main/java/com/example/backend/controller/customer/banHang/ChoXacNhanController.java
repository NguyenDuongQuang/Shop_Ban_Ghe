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
@RequestMapping("/hoaDon/datHang/choXacNhan")
public class ChoXacNhanController {

    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @GetMapping("/danhSach")
    public List<HoaDon> listBill1() {

        return hoaDonDatHangService.findHoaDonByTrangThai(1L);
    }
    @PostMapping("/capNhatTrangThai/daXacNhan")
    public ResponseEntity<?> updateStatus2(@RequestBody HoaDonDTO hoaDonDTO)  {
        Long id = hoaDonDTO.getId();
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
//        hoaDonDatHangService.createTimeLine("Xác nhận đơn", 2, id, nhanVien.getHoTen());
        return ResponseEntity.ok().body(hoaDonDatHangService.updateTrangThai(2, id));
    }
    @PostMapping("/capNhatTrangThai/huyDon")
    public ResponseEntity<Map<String, Boolean>> updateStatus5(@RequestBody HoaDonDTO hoaDonDTO) {
        Long id = hoaDonDTO.getId();
        String ghiChu = hoaDonDTO.getGhiChu();
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        hoaDonDatHangService.capNhatTrangThaiHuyDon(5, id, ghiChu);
//        hoaDonDatHangService.createTimeLine("Huỷ đơn", 5, id, nhanVien.getHoTen());
        return ResponseEntity.ok().build();
    }

}
