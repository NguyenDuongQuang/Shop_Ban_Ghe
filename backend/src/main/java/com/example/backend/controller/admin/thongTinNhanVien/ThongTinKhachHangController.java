package com.example.backend.controller.admin.thongTinNhanVien;

import com.example.backend.dto.KhachHangDTO;
import com.example.backend.entity.KhachHang;
import com.example.backend.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class ThongTinKhachHangController {
    @Autowired
    KhachHangRepository khachHangRepository;

    @GetMapping("/thongTin/{email}")
    public ResponseEntity<?> getThongTinKhach(@PathVariable("email") String email){
        KhachHang khachHang = khachHangRepository.findByEmail(email);
        return ResponseEntity.ok().body(khachHang);
    }

    @PostMapping("/suaThongTinKhach")
    public ResponseEntity<?> suaThongTinKhach(@RequestBody KhachHangDTO khachHangDTO){
        KhachHang khachHang = khachHangRepository.findByEmail(khachHangDTO.getEmail());
        khachHang.setHoTen(khachHangDTO.getHoTen());
        khachHang.setSoDienThoai(khachHangDTO.getSoDienThoai());
        khachHang.setEmail(khachHangDTO.getEmail());
        khachHang.setNgaySinh(khachHangDTO.getNgaySinh());
        khachHang.setDiaChi(khachHangDTO.getDiaChi());
        khachHangRepository.save(khachHang);

        KhachHang result = khachHangRepository.findByEmail(khachHang.getEmail());
        return ResponseEntity.ok().body(khachHang);
    }
}
