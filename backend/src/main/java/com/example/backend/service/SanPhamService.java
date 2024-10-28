package com.example.backend.service;

import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.SanPham;
import com.example.backend.entity.SanPhamChiTiet;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SanPhamService {

    List<SanPham> findAllProduct();

    ResponseEntity<SanPham> saveEdit(SanPhamDto sanPhamDTO);

    ResponseEntity<List<SanPham>> deleteProduct(Long id);

    ResponseEntity<?> searchAllProduct(String search);

    List<SanPham> searchDateProduct(String searchDate);

    ResponseEntity<?> taoSanPham(SanPhamDto sanPhamDTO);

    List<Object> chiTietSanPham(long id_SanPham);

    List<SanPhamChiTiet> spct_list();

    ResponseEntity<?> getSanPhamTheoGia(Float gia1, Float gia2);

}
