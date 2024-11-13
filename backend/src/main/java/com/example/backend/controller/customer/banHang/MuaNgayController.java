package com.example.backend.controller.customer.banHang;

import com.example.backend.dto.HoaDonChiTietDTO;
import com.example.backend.dto.HoaDonDTO;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.HoaDon;
import com.example.backend.entity.HoaDonChiTiet;
import com.example.backend.entity.SanPhamChiTiet;
import com.example.backend.repository.HoaDonChiTietRepository;
import com.example.backend.repository.HoaDonRepository;
import com.example.backend.repository.ImageRepository;
import com.example.backend.service.MuaNgayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/muaNgay")
public class MuaNgayController {
    @Autowired
    MuaNgayService muaNgayService;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    ImageRepository hinhAnhRepository;


    @PostMapping("/check-out")
    public ResponseEntity<?> muaNgayCheckOut(@RequestBody SanPhamDto dto) {
        return muaNgayService.muaNgayCheckOut(dto);
    }

    @GetMapping("/getHoaDon/{id}")
    public ResponseEntity<HoaDon> getHoaDonMuaNgay(@PathVariable("id") long id_HoaDon) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_HoaDon);
        return ResponseEntity.ok(hoaDon);
    }

    @GetMapping("/getHoaDonChiTiet/{id}")
    public ResponseEntity<?> getHoaDonChiTiet(@PathVariable("id") long id_HoaDon) {
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(id_HoaDon);
        List<HoaDonChiTietDTO> dto = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            HoaDon hoaDon = hoaDonChiTiet.getHoaDon();
            String anh_san_pham = hinhAnhRepository.getAnhMacDinh(sanPhamChiTiet.getSanPham().getId());

            HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
            hoaDonChiTietDTO.setId(hoaDonChiTiet.getId());
            hoaDonChiTietDTO.setIdProduct(sanPhamChiTiet.getSanPham().getId());
            hoaDonChiTietDTO.setSoLuong(hoaDonChiTiet.getSoLuong());
            hoaDonChiTietDTO.setDonGia(hoaDonChiTiet.getDonGia());
            hoaDonChiTietDTO.setThanhTien(hoaDonChiTiet.getThanhTien());
            hoaDonChiTietDTO.setHoaDon(hoaDon);
            hoaDonChiTietDTO.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTietDTO.setAnhSanPham(anh_san_pham);

            dto.add(hoaDonChiTietDTO);
        }
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/check-out")
    public ResponseEntity<HoaDon> getBill() {
        return muaNgayService.getBill();
    }



    @PostMapping("/datHang")
    public ResponseEntity datHang(@RequestBody HoaDonDTO dto) {
        return muaNgayService.datHang(dto);
    }
}
