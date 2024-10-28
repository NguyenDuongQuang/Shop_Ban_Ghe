package com.example.backend.service;

import com.example.backend.entity.LoaiSanPham;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoaiSanPhamService {

    List<LoaiSanPham> findAllProductLine();

    ResponseEntity<LoaiSanPham> saveEdit(LoaiSanPham loaiSanPhamUpdate);

    ResponseEntity<List<LoaiSanPham>> deleteProductLine(Long id);

    ResponseEntity<LoaiSanPham> saveCreate(LoaiSanPham loaiSanPhamCreate);

    List<LoaiSanPham> searchAllProductLine(String search);

    List<LoaiSanPham> searchDateProductLine(String searchDate);

}
