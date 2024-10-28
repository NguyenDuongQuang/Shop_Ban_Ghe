package com.example.backend.service.Impl;


import com.example.backend.controller.message.Message;
import com.example.backend.entity.SanPham;
import com.example.backend.entity.SanPhamChiTiet;
import com.example.backend.repository.GioHangChiTietRepository;
import com.example.backend.repository.ImageRepository;
import com.example.backend.repository.SanPhamChiTietRepository;
import com.example.backend.repository.SanPhamRepository;
import com.example.backend.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {

    @Autowired
    SanPhamChiTietRepository repository;

    @Autowired
    ImageRepository hinhAnhRepository;

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

//    @Autowired
//    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Override
    public List<SanPhamChiTiet> findAllProductDetails() {
        List<SanPhamChiTiet> list = repository.findAll();
        return list;
    }

    @Override
    public List<SanPhamChiTiet> findProductDetails() {
        List<SanPhamChiTiet> list = repository.findProductDetails();
        return list;
    }

    @Override
    public ResponseEntity<SanPhamChiTiet> saveEdit(SanPhamChiTiet sanPhamChiTietUpdate) {
        String errorMessage;
        Message errorResponse;
        if (sanPhamChiTietUpdate.getSoLuong() < 0) {
            errorMessage = "Số lượng không được nhỏ hơn 0";
            errorResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<SanPhamChiTiet> optional = repository.findById(sanPhamChiTietUpdate.getId());
            if (optional.isPresent()) {
                SanPhamChiTiet sanPhamChiTiet = optional.get();
                if (sanPhamChiTietUpdate.getSoLuong() <= 0) {
                    sanPhamChiTiet.setTrangThai(false);
                } else {
                    sanPhamChiTiet.setTrangThai(true);
                }
                sanPhamChiTiet.setSoLuong(sanPhamChiTietUpdate.getSoLuong());
                sanPhamChiTiet.setSoLuongTam(sanPhamChiTietUpdate.getSoLuong());
                SanPham sanPham = sanPhamChiTietUpdate.getSanPham();

                if (sanPham != null) {
                    sanPhamChiTiet.setSanPham(sanPham);
                }
                if (sanPham.getGia() <= 0) {
                    errorMessage = "Giá tiền phải lớn hơn 0";
                    errorResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
                    return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
                }
                repository.save(sanPhamChiTiet);
                return ResponseEntity.ok(sanPhamChiTiet);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return new ResponseEntity(new Message(e.getMessage(), TrayIcon.MessageType.ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<SanPhamChiTiet>> deleteProductDetails(Long id) {
//        try {
//            Optional<SanPhamChiTiet> optional = repository.findById(id);
//            SanPham sp = sanPhamRepository.findByID(optional.get().getSanPham().getId());
//            if (optional.isPresent()) {
//                SanPhamChiTiet sanPhamChiTiet = optional.get();
//                sanPhamChiTiet.setDeleted(true);
//                repository.save(sanPhamChiTiet);
//                List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findCartBySPCTID(sanPhamChiTiet.getId());
//                for (GioHangChiTiet gioHangChiTiet : gioHangChiTiets) {
//                    gioHangChiTiet.setDeleted(true);
//                    gioHangChiTietRepository.save(gioHangChiTiet);
//                }
//
//                List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findBySPCTID(sanPhamChiTiet.getId());
//                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
//                    hoaDonChiTiet.setDeleted(true);
//                    hoaDonChiTietRepository.save(hoaDonChiTiet);
//                }
//                List<SanPhamChiTiet> list = repository.findSpctByIdSp(sp.getId());
//                return ResponseEntity.ok(list);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
        return null;
    }

    @Override
    public ResponseEntity<SanPhamChiTiet> saveCreate(SanPhamChiTiet sanPhamChiTietCreate) {
        String errorMessage;
        Message errorResponse;
        try {
            SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();

            return ResponseEntity.ok(sanPhamChiTiet);
        } catch (Exception e) {
            return new ResponseEntity(new Message(e.getMessage(), TrayIcon.MessageType.ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<SanPhamChiTiet> searchAllProductDetails(String search) {
        List<SanPhamChiTiet> list = repository.findByAll(search);
        return list;
    }

    @Override
    public List<SanPhamChiTiet> searchDateProductDetails(String searchDate) {
        LocalDate search = LocalDate.parse(searchDate);
        List<SanPhamChiTiet> list = repository.findByDate(search);
        return list;
    }

    @Override
    public ResponseEntity chinhSuaSoLuongSPCT(SanPhamChiTiet sanPhamChiTiet) {
        SanPhamChiTiet sanPhamChiTiets = repository.findByID(sanPhamChiTiet.getId());
        sanPhamChiTiets.setSoLuong(sanPhamChiTiet.getSoLuong());
        sanPhamChiTiets.setSoLuongTam(sanPhamChiTiet.getSoLuong());
        repository.save(sanPhamChiTiets);
        return new ResponseEntity(HttpStatus.OK);
    }
}