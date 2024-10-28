package com.example.backend.controller.admin;

import com.example.backend.entity.LoaiSanPham;
import com.example.backend.repository.LoaiSanPhamRepository;
import com.example.backend.service.LoaiSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/loaiSanPham")
@CrossOrigin("*")
public class LoaiSanPhamController {
    @Autowired
    LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    LoaiSanPhamService loaiSanPhamService;

    @GetMapping("/danhSach")
    public ResponseEntity<List<LoaiSanPham>> getLine() {
        List<LoaiSanPham> list = loaiSanPhamRepository.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/themMoi")
    public ResponseEntity<LoaiSanPham> saveCreate(@RequestBody LoaiSanPham loaiSanPham) {
        return loaiSanPhamService.saveCreate(loaiSanPham);
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<List<LoaiSanPham>> delete(@PathVariable("id") Long id) {
        return loaiSanPhamService.deleteProductLine(id);
    }

    @GetMapping("/chinhSua/{id}")
    public LoaiSanPham edit(@PathVariable("id") Long id) {
        return loaiSanPhamRepository.findByID(id);
    }

    @PutMapping("/luuChinhSua")
    public ResponseEntity<LoaiSanPham> saveUpdate(@RequestBody LoaiSanPham loaiSanPham) {
        return loaiSanPhamService.saveEdit(loaiSanPham);
    }
}
