package com.example.backend.controller.admin.password;

import com.example.backend.dto.password.FogotPasswordDTO;
import com.example.backend.entity.KhachHang;
import com.example.backend.entity.NhanVien;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.service.Impl.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
public class FogotPasswordController {
    @Autowired
    PasswordService passwordService;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    char[] key;

    String email;

    @PostMapping("/get-keys")
    public ResponseEntity<?> getKeys(@RequestBody FogotPasswordDTO fogotPasswordDTO) {
        char[] keys = passwordService.sendKey(fogotPasswordDTO);
        key = keys;

        NhanVien nhanVien = nhanVienRepository.findByEmail(fogotPasswordDTO.getEmail());
        KhachHang khachHang = khachHangRepository.findByEmail(fogotPasswordDTO.getEmail());
        Map<String, String> respone = new HashMap<>();

        if(nhanVien == null && khachHang == null){
            respone.put("error", "Không tìm thấy email tương ứng");
            return ResponseEntity.badRequest().body(respone);
        }else if(nhanVien != null){
            email = nhanVien.getEmail();
            respone.put("sucess", "Gửi mail yêu cầu thành công");
            return ResponseEntity.ok().body(respone);
        }else {
            email = khachHang.getEmail();
            respone.put("sucess", "Gửi mail yêu cầu thành công");
            return ResponseEntity.ok().body(respone);
        }
    }

    @PostMapping("/check-keys")
    public ResponseEntity<?> checkKeys(@RequestBody FogotPasswordDTO fogotPasswordDTO) {
        Map<String, String> respone = new HashMap<>();
        if(Arrays.equals(key, fogotPasswordDTO.getKeys().toCharArray())){
            respone.put("sucess", "Chuyển hướng sang trang đổi mật khẩu");
            return ResponseEntity.ok().body(respone);
        }else {
            respone.put("error", "Sai mã");
            return ResponseEntity.badRequest().body(respone);
        }
    }

    @PutMapping("/fogot-password")
    public ResponseEntity<?> fogotPassword(@RequestBody FogotPasswordDTO fogotPasswordDTO) {
        Map<String, String> respone = new HashMap<>();
        passwordService.fogotPassword(fogotPasswordDTO, email);
        respone.put("mess", "Đổi mật khẩu thành công");
        return ResponseEntity.ok().body(respone);
    }

}
