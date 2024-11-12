package com.example.backend.controller.customer.banHang;

import com.example.backend.dto.GioHangDTO;
import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.HoaDon;
import com.example.backend.service.BanHangOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/banHang/online")
public class BanHangOnlineController {

    @Autowired
    BanHangOnlineService banHangOnlineService;


    @PostMapping("/checkOut")
    public ResponseEntity<?> checkout(@RequestBody GioHangDTO dto) {
        return ResponseEntity.ok(banHangOnlineService.checkout(dto));
    }

    @GetMapping("/getHoaDon/{id}")
    public ResponseEntity<HoaDon> getHoaDon(@PathVariable("id") long id_HoaDon) {
        return banHangOnlineService.getHoaDon(id_HoaDon);
    }

    @GetMapping("/getHoaDonChiTiet/{id}")
    public ResponseEntity<?> getHoaDonChiTiet(@PathVariable("id") long id_HoaDon) {
        return banHangOnlineService.getHoaDonChiTiet(id_HoaDon);
    }

    @GetMapping("/check-out")
    public ResponseEntity<HoaDon> getBill() {
        return banHangOnlineService.getBill();
    }


    @PostMapping("/datHang")
    public ResponseEntity<?> datHang(@RequestBody HoaDonDTO dto) {
        return banHangOnlineService.datHang(dto);
    }
}