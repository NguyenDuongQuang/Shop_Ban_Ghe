package com.example.backend.service.Impl;

import com.example.backend.dto.HoaDonDTO;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.HoaDon;
import com.example.backend.entity.HoaDonChiTiet;
import com.example.backend.entity.SanPhamChiTiet;
import com.example.backend.entity.TrangThai;
import com.example.backend.repository.*;
import com.example.backend.service.BillOrderService;
import com.example.backend.service.MuaNgayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class MuaNgayServiceImpl implements MuaNgayService {
    @Autowired
    HoaDonRepository hoaDonRepository;


    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;


    @Autowired
    TrangThaiRepository trangThaiRepository;

    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    ImageRepository hinhAnhRepository;

    @Autowired
    MailService mailService;


    private Long idBill;


    @Transactional
    @Override
    public ResponseEntity<?> muaNgayCheckOut(SanPhamDto dto) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.getSanPhamChiTiet( dto.getSan_pham_id());
        int tongTien = dto.getDonGia() * dto.getSoLuong();
        HoaDon hoaDon = new HoaDon();
        hoaDon.setCreatedDate(new Date());
        hoaDon.setTongTienDonHang(tongTien);
        hoaDon.setTongTienHoaDon(tongTien);
        hoaDonRepository.save(hoaDon);
        hoaDon.setMaHoaDon("HD" + hoaDon.getId());
        hoaDonRepository.save(hoaDon);

        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setDonGia(Math.round(sanPhamChiTiet.getSanPham().getGia()));
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setThanhTien(tongTien);
        hoaDonChiTiet.setSoLuong(dto.getSoLuong());
        hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
        hoaDonChiTietRepository.save(hoaDonChiTiet);
        idBill = hoaDon.getId();
        Map<String, Object> response = new HashMap<>();
        response.put("id_hoa_don", hoaDon.getId());
        response.put("san_pham_chi_tiet", sanPhamChiTiet);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<HoaDon> getBill() {
        if (idBill != null) {
            Optional<HoaDon> optionalBill = hoaDonRepository.findById(idBill);
            if (optionalBill.isPresent()) {
                HoaDon hoaDon = optionalBill.get();
                return ResponseEntity.ok(hoaDon);
            }
        }
        return ResponseEntity.notFound().build();
    }



    @Transactional
    @Override
    public ResponseEntity datHang(HoaDonDTO dto) {
        HoaDon hoaDon = hoaDonRepository.findByID(dto.getId());
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(hoaDonChiTiet.getSanPhamChiTiet().getId());
            sanPhamChiTiet.setSoLuongTam(sanPhamChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }

        TrangThai trangThai = trangThaiRepository.findByID(1L);

        hoaDon.setGhiChu(dto.getGhiChu());
        hoaDon.setTongTienHoaDon(dto.getTongTienHoaDon());
        hoaDon.setTongTienDonHang(dto.getTongTienDonHang());
        hoaDon.setEmailNguoiNhan(dto.getEmail());
        hoaDon.setSDTNguoiNhan(dto.getSoDienThoai());
        hoaDon.setTienShip(dto.getTienShip());
        hoaDon.setDiaChiGiaoHang(dto.getDiaChi());
        hoaDon.setTrangThai(trangThai);
        hoaDon.setNguoiNhan(dto.getNguoiTao());
        hoaDonRepository.save(hoaDon);


        if (hoaDon.getEmailNguoiNhan() != null && !hoaDon.getEmailNguoiNhan().isEmpty()) {
            try {
                mailService.sendOrderConfirmationEmail(hoaDon.getEmailNguoiNhan(), hoaDon);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

//        hoaDonDatHangService.createTimeLine("Tạo đơn hàng", 1L, hoaDon.getId(), dto.getNguoiTao());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
