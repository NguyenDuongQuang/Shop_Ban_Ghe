package com.example.backend.controller.admin.thongTinNhanVien;


import com.example.backend.dto.NhanVienDTO;
import com.example.backend.entity.NhanVien;
import com.example.backend.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class ThongTinNhanVienController {
    @Autowired
    NhanVienRepository nhanVienRepository;

    @GetMapping("/thongTin/{email}")
    public ResponseEntity<?> getThongTin(@PathVariable("email") String email){
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        return ResponseEntity.ok().body(nhanVien);
    }

    @PostMapping("/suaThongTin")
    public ResponseEntity<?> suaThongTin(@RequestBody NhanVienDTO nhanVienDTO){
        NhanVien nhanVien = nhanVienRepository.findByEmail(nhanVienDTO.getEmail());
        nhanVien.setHoTen(nhanVienDTO.getName());
        nhanVien.setGioiTinh(nhanVienDTO.getGioiTinh());
        nhanVien.setSoDienThoai(nhanVienDTO.getPhoneNumber());
        nhanVien.setNgaySinh(nhanVienDTO.getDateOfBirth());
        nhanVienRepository.save(nhanVien);

        NhanVien result = nhanVienRepository.findByEmail(nhanVien.getEmail());
        return ResponseEntity.ok().body(nhanVien);
    }
}
