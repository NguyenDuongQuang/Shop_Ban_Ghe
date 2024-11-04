package com.example.backend.service.Impl;


import com.example.backend.controller.message.Message;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.LoaiSanPham;
import com.example.backend.entity.SanPham;
import com.example.backend.entity.SanPhamChiTiet;
import com.example.backend.repository.ImageRepository;
import com.example.backend.repository.LoaiSanPhamRepository;
import com.example.backend.repository.SanPhamChiTietRepository;
import com.example.backend.repository.SanPhamRepository;
import com.example.backend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

@Service
public class SanPhamServiceImpl implements SanPhamService {

    @Autowired
    SanPhamRepository repository;

    @Autowired
    LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    ImageRepository hinhAnhRepository;

    Long id_product;

    @Override
    public List<SanPham> findAllProduct() {
        List<SanPham> list = repository.findAll();
        return list;
    }

    @Override
    public ResponseEntity<SanPham> saveEdit(SanPhamDto sanPhamDTO) {
        String errorMessage;
        Message errorResponse;
        if (sanPhamDTO.getGia() <= 0) {
            errorMessage = "Giá tiền phải lớn hơn 0";
            errorResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<SanPham> optional = repository.findById(sanPhamDTO.getId());
            if (optional.isPresent()) {
                SanPham sanPham = optional.get();
                sanPham.setTenSanPham(sanPhamDTO.getTenSanPham());
                sanPham.setGia(sanPhamDTO.getGia());
                sanPham.setTrangThai(0);
                sanPham.setLoaiSanPham(sanPhamDTO.getLoaiSanPham());
                repository.save(sanPham);
                return ResponseEntity.ok(sanPham);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return new ResponseEntity(new Message(e.getMessage(), TrayIcon.MessageType.ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<List<SanPham>> deleteProduct(Long id) {
        try {
            Optional<SanPham> optional = repository.findById(id);
            if (optional.isPresent()) {
                SanPham sanPham = optional.get();
                sanPham.setDeleted(true);
                sanPham.setTrangThai(0);
                repository.save(sanPham);
                List<SanPham> list = findAllProduct();
                return ResponseEntity.ok(list);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<?> searchAllProduct(String search) {
        List<SanPham> list = repository.findByAll(search);
        List<SanPhamDto> sanPhamDTOList = new ArrayList<>();
        for (SanPham pham : list) {
            SanPhamDto sanPhamDTO = new SanPhamDto();
            sanPhamDTO.setLoaiSanPham(pham.getLoaiSanPham());
            sanPhamDTO.setId(pham.getId());
            sanPhamDTO.setSan_pham_id(pham.getId());
            sanPhamDTO.setTenSanPham(pham.getTenSanPham());
            sanPhamDTO.setGia(pham.getGia());

            String anhSanPham = hinhAnhRepository.getTenAnhSanPham_HienThiDanhSach(pham.getId());
            sanPhamDTO.setAnh_san_pham(anhSanPham);

            sanPhamDTOList.add(sanPhamDTO);
        }
        return ResponseEntity.ok().body(sanPhamDTOList);
    }

    @Override
    public List<SanPham> searchDateProduct(String searchDate) {
        LocalDate search = LocalDate.parse(searchDate);
        List<SanPham> list = repository.findByDate(search);
        return list;
    }

    @Override
    public ResponseEntity<?> taoSanPham(SanPhamDto sanPhamDTO) {
        SanPham optionalSanPham = repository.checkLap(sanPhamDTO.getTenSanPham());
        String errorMessage;
        Message errors;

        if (optionalSanPham != null) {
            errorMessage = "Trùng tên sản phẩm";
            errors = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", errorMessage);
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> response = new HashMap<>();


        LoaiSanPham loaiSanPham = loaiSanPhamRepository.findByID(sanPhamDTO.getLoaiSanPham_id());

        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(sanPhamDTO.getTenSanPham());
        sanPham.setGia(sanPhamDTO.getGia());
        sanPham.setTrangThai(0);
        sanPham.setLoaiSanPham(loaiSanPham);
        repository.save(sanPham);

        id_product = sanPham.getId();

        List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();
        SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
        sanPhamChiTiet.setSanPham(sanPham);
        sanPhamChiTiet.setTrangThai(true);
        sanPhamChiTiet.setSoLuong(sanPhamDTO.getSoLuong());
        sanPhamChiTiet.setSoLuongTam(sanPhamDTO.getSoLuong());
        sanPhamChiTietRepository.save(sanPhamChiTiet);
        sanPhamChiTietList.add(sanPhamChiTiet);
        response.put("list", sanPhamChiTietList);
        response.put("id_product", sanPham.getId());
        return ResponseEntity.ok().body(response);
    }

    @Override
    public List<Object> chiTietSanPham(long id_SanPham) {
        SanPham sanPham = repository.findByID(id_SanPham);
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findByProductID(id_SanPham);
        List<Object> respone = new ArrayList<>();
        respone.add(sanPham);
        respone.add(sanPhamChiTiets);
        return respone;
    }

    @Override
    public List<SanPhamChiTiet> spct_list() {
        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findSpctByIdSp(id_product);
        return list;
    }

    @Override
    public ResponseEntity<?> getSanPhamTheoGia(Float gia1, Float gia2) {
        try {
            List<SanPham> sanPhams = repository.findTheoGia(gia1, gia2);
            List<SanPhamDto> sanPhamDTOList = new ArrayList<>();

            for (SanPham pham : sanPhams) {
                SanPhamDto sanPhamDTO = new SanPhamDto();
                sanPhamDTO.setLoaiSanPham(pham.getLoaiSanPham());
                sanPhamDTO.setId(pham.getId());
                sanPhamDTO.setSan_pham_id(pham.getId());
                sanPhamDTO.setTenSanPham(pham.getTenSanPham());
                sanPhamDTO.setGia(pham.getGia());

                String anhSanPham = hinhAnhRepository.getTenAnhSanPham_HienThiDanhSach(pham.getId());
                sanPhamDTO.setAnh_san_pham(anhSanPham);
                sanPhamDTOList.add(sanPhamDTO);
            }
            return ResponseEntity.ok().body(sanPhamDTOList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());        }
    }

}
