package com.example.backend.controller.customer.sanPham;

import com.example.backend.dto.SPCTDTO;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.Anh;
import com.example.backend.entity.SanPham;
import com.example.backend.repository.ImageRepository;
import com.example.backend.repository.SanPhamChiTietRepository;
import com.example.backend.repository.SanPhamRepository;
import com.example.backend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/customer/sanPham")
public class ProductController {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private ImageRepository imageRepository;


    @GetMapping("/danhSach")
    public ResponseEntity<?>getProductCus(){
        List<SanPham>list=sanPhamRepository.findAll();
        List<SanPhamDto> sanPhamDtos=new ArrayList<>();
        for(SanPham sp :list){
            SanPhamDto sanPhamDto=new SanPhamDto();
            sanPhamDto.setId(sp.getId());
            sanPhamDto.setTenSanPham(sp.getTenSanPham());
            sanPhamDto.setGia(sp.getGia());
            sanPhamDto.setTrangThai(sp.getTrangThai());
            String hinhAnh = imageRepository.getTenAnhSanPham_HienThiDanhSach(sp.getId());
            sanPhamDto.setAnh_san_pham(hinhAnh);
            sanPhamDtos.add(sanPhamDto);
        }
        return  ResponseEntity.ok(sanPhamDtos);
    }

    @GetMapping("/timKiemTheoTen/{search}")
    public ResponseEntity<?> timKiemTheoTen(@PathVariable("search") String search){
        List<SanPham>list=sanPhamRepository.findByName(search);
        List<SanPhamDto> sanPhamDtos=new ArrayList<>();
        for(SanPham sp :list){
            SanPhamDto sanPhamDto=new SanPhamDto();
            sanPhamDto.setId(sp.getId());
            sanPhamDto.setTenSanPham(sp.getTenSanPham());
            sanPhamDto.setGia(sp.getGia());
            sanPhamDto.setTrangThai(sp.getTrangThai());
            String hinhAnh = imageRepository.getTenAnhSanPham_HienThiDanhSach(sp.getId());
            sanPhamDto.setAnh_san_pham(hinhAnh);
            sanPhamDtos.add(sanPhamDto);
        }
        return  ResponseEntity.ok(sanPhamDtos);
    }


    @GetMapping("/loc/loai_san_pham")
    public ResponseEntity<?> locTheoLoai(@RequestParam long idloaiSanPham){
        List<SanPham>list=sanPhamRepository.findByLoaiSanPham(idloaiSanPham);
        List<SanPhamDto> sanPhamDtos=new ArrayList<>();
        for(SanPham sp :list){
            SanPhamDto sanPhamDto=new SanPhamDto();
            sanPhamDto.setId(sp.getId());
            sanPhamDto.setTenSanPham(sp.getTenSanPham());
            sanPhamDto.setGia(sp.getGia());
            sanPhamDto.setTrangThai(sp.getTrangThai());
            String hinhAnh = imageRepository.getTenAnhSanPham_HienThiDanhSach(sp.getId());
            sanPhamDto.setAnh_san_pham(hinhAnh);
            sanPhamDtos.add(sanPhamDto);
        }
        return  ResponseEntity.ok(sanPhamDtos);
    }

    @GetMapping("/loc/gia")
    public ResponseEntity<?> locTheoGia(@RequestParam Float gia1, @RequestParam Float gia2  ){
        return sanPhamService.getSanPhamTheoGia(gia1,gia2);
    }

    @GetMapping("/getSanPham/id={id}")
    public SanPham getSanPham(@PathVariable("id") long id_SanPham){
        SanPham sanPham = sanPhamRepository.findByID(id_SanPham);
        return sanPham;
    }
    @GetMapping("/getAnhMacDinhSanPham/{id}")
    public ResponseEntity<?> getAnhMacDinhSanPham(@PathVariable("id") long id_sanPham) {
        String hinhAnhs = imageRepository.getTenAnhSanPham_HienThiDanhSach(id_sanPham);
        Map<String, String> respone = new HashMap<>();
        respone.put("anhMacDinh", hinhAnhs);
        return ResponseEntity.ok().body(respone);
    }

    @GetMapping("/getAnhSanPham/{id}")
    public ResponseEntity<?> getAnhMacDinh(@PathVariable("id") long id_sanPham) {
        List<Anh> hinhAnhs = imageRepository.getHinhAnhByProductID(id_sanPham);
        return ResponseEntity.ok().body(hinhAnhs);
    }

    @PostMapping("/api/getSoLuong")
    public ResponseEntity<Integer> getSoLuong(@RequestBody SPCTDTO dto) {
        Integer soLuong = sanPhamChiTietRepository.getSoLuongHienCp(dto.getSanPhamId());
        return ResponseEntity.ok().body(soLuong);
    }
}
