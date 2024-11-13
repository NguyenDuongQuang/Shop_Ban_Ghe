package com.example.backend.controller.customer.donHang;


import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.BillOrderService;
import com.example.backend.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/donHang")
public class DanhSachController {

    @Autowired
    HoaDonService hoaDonService;

    @Autowired
    TrangThaiRepository trangThaiRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("/choXacNhan/{email}")
    public ResponseEntity<?> getDSChoXacNhan(@PathVariable("email") String email){
        TrangThai trangThai = trangThaiRepository.findByID(1L);
        return hoaDonService.getDanhSachHoaDon(email, trangThai);
    }

    @GetMapping("/choGiaoHang/{email}")
    public ResponseEntity<?> getDSChoGiaoHang(@PathVariable("email") String email){
        TrangThai trangThai = trangThaiRepository.findByID(2L);
        return hoaDonService.getDanhSachHoaDon(email, trangThai);
    }

    @GetMapping("/dangGiaoHang/{email}")
    public ResponseEntity<?> getDSDangGiaoHang(@PathVariable("email") String email){
        TrangThai trangThai = trangThaiRepository.findByID(3L);
        return hoaDonService.getDanhSachHoaDon(email, trangThai);
    }

    @GetMapping("/daGiaoHang/{email}")
    public ResponseEntity<?> getDSDaGiaoHang(@PathVariable("email") String email){
        TrangThai trangThai = trangThaiRepository.findByID(4L);
        return hoaDonService.getDanhSachHoaDon(email, trangThai);
    }

    @GetMapping("/daHuy/{email}")
    public ResponseEntity<?> getDSDaHuy(@PathVariable("email") String email){
        TrangThai trangThai = trangThaiRepository.findByID(5L);
        return hoaDonService.getDanhSachHoaDon(email, trangThai);
    }

    @PostMapping("/huyDonCustomer")
    public ResponseEntity<?> CustomerHuyDon(@RequestBody HoaDonDTO dto){
        HoaDon hoaDon = hoaDonRepository.findByID(dto.getId());
        TrangThai trangThai = trangThaiRepository.findByID(5L);
        KhachHang khachHang = khachHangRepository.findByEmail(dto.getNguoi_thao_tac());
        hoaDon.setTrangThai(trangThai);
        hoaDon.setGhiChu("Khách huỷ");
        hoaDonRepository.save(hoaDon);

        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + sanPhamChiTiet.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }

//        hoaDonDatHangService.createTimeLine("Huy don", 5L,hoaDon.getId(), khachHang.getHoTen());
        List<HoaDon> hoaDonReturn = hoaDonRepository.getDSChoXacNhan(khachHang.getId(), 1L);
        return ResponseEntity.ok().body(hoaDonReturn);
    }

    @PostMapping("/daNhanDuocHang")
    public ResponseEntity<?> daNhanDuocHang(@RequestBody HoaDonDTO dto){
        HoaDon hoaDon = hoaDonRepository.findByID(dto.getId());
        TrangThai trangThai = trangThaiRepository.findByID(9L);
        KhachHang khachHang = khachHangRepository.findByEmail(dto.getNguoi_thao_tac());
        hoaDon.setTrangThai(trangThai);
        hoaDonRepository.save(hoaDon);

//        hoaDonDatHangService.createTimeLine("Đã nhận được hàng", 9L,hoaDon.getId(), khachHang.getHoTen());
        List<HoaDon> hoaDonReturn = hoaDonRepository.getDSChoXacNhan(khachHang.getId(), 3L);
        return ResponseEntity.ok().body(hoaDonReturn);
    }

    @PostMapping("/daNhanTatCa")
    public ResponseEntity<?> daNhanTatCa(@RequestBody HoaDonDTO dto){
        KhachHang khachHang = khachHangRepository.findByEmail(dto.getNguoi_thao_tac());
        List<HoaDon> hoaDons = hoaDonRepository.getDSChoXacNhan(khachHang.getId(), 3L);
        for(HoaDon hoaDon: hoaDons){
            TrangThai trangThai = trangThaiRepository.findByID(4L);
            hoaDon.setTrangThai(trangThai);
            hoaDonRepository.save(hoaDon);
//
//            hoaDonDatHangService.createTimeLine("Giao hàng thành công", 4L,hoaDon.getId(), khachHang.getHoTen());
            List<HoaDon> hoaDonReturn = hoaDonRepository.getDSChoXacNhan(khachHang.getId(), 3L);
            return ResponseEntity.ok().body(hoaDonReturn);
        }
        return null;
    }
}
