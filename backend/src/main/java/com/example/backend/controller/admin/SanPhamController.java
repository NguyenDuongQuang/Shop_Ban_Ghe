package com.example.backend.controller.admin;

import com.example.backend.dto.SPCTDTO;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.Anh;
import com.example.backend.entity.SanPham;
import com.example.backend.entity.SanPhamChiTiet;
import com.example.backend.repository.ImageRepository;
import com.example.backend.repository.SanPhamChiTietRepository;
import com.example.backend.repository.SanPhamRepository;
import com.example.backend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/sanPham")
@CrossOrigin("*")
public class SanPhamController {

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    SanPhamService sanPhamService;

    @Autowired
    ImageRepository hinhAnhRepository;


    @GetMapping("/danhSach")
    public ResponseEntity<?> getProduct() {
        List<SanPham> list = sanPhamRepository.findAll();
        List<SanPhamDto> dtoList = new ArrayList<>();
        for (SanPham sp : list) {
            SanPhamDto sanPhamDTO = new SanPhamDto();
            sanPhamDTO.setId(sp.getId());
            sanPhamDTO.setTenSanPham(sp.getTenSanPham());
            sanPhamDTO.setGia(sp.getGia());
            sanPhamDTO.setTrangThai(sp.getTrangThai());
            String hinh_anh = hinhAnhRepository.getTenAnhSanPham_HienThiDanhSach(sp.getId());
            sanPhamDTO.setAnh_san_pham(hinh_anh);

            dtoList.add(sanPhamDTO);
        }
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/ChiTietSanPham")
    public List<Object> chiTietSanPham(@RequestParam long id_SanPham) {
        return sanPhamService.chiTietSanPham(id_SanPham);
    }

    //Tạo mới và gen ra sản phẩm chi tiết
    @PostMapping("/TaoSanPham")
    public ResponseEntity<?> saveCreate(@RequestBody SanPhamDto sanPhamDTO) {
//        ResponseEntity<?> response = ProductValidate.checkTaoSanPham(sanPhamDTO);
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            return response;
//        } else {
//            return sanPhamService.taoSanPham(sanPhamDTO);
//        }
        return sanPhamService.taoSanPham(sanPhamDTO);
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<List<SanPham>> deleteProduct(@PathVariable("id") Long id) {
        return sanPhamService.deleteProduct(id);
    }

    @DeleteMapping("/xoa-san-pham-chi-tiet/{id}")
    public void deleteProductDetails(@RequestBody SanPhamChiTiet sanPhamChiTiet) {
        sanPhamRepository.deleteById(sanPhamChiTiet.getId());
    }

    @GetMapping("/chinhSua/{id}")
    public SanPham editProduct(@PathVariable("id") Long id) {
        return sanPhamRepository.findByID(id);
    }

    @PutMapping("/luuChinhSua")
    public ResponseEntity<SanPham> saveUpdate(@RequestBody SanPhamDto sanPham) {
        return sanPhamService.saveEdit(sanPham);
    }

    @RequestMapping("/timKiem={search}")
    public ResponseEntity<?> searchAll(@PathVariable("search") String search) {
        return sanPhamService.searchAllProduct(search);
    }

    @RequestMapping("/timKiemNgay={searchDate}")
    public List<SanPham> searchDate(@PathVariable("searchDate") String search) {
        return sanPhamService.searchDateProduct(search);
    }

    @GetMapping("/getAnhSanPham/{id}")
    public ResponseEntity<?> getAnhMacDinh(@PathVariable("id") long id_sanPham) {
        List<Anh> hinhAnhs = hinhAnhRepository.getHinhAnhByProductID(id_sanPham);
        return ResponseEntity.ok().body(hinhAnhs);
    }

    @PostMapping("/themSanPhamTuongTu")
    public ResponseEntity<?> themSanPhamTuongTu(@RequestBody SanPhamDto sanPhamDTO) {
        Map<String, String> respone = new HashMap<>();
        SanPham sanPham = sanPhamRepository.findByID(sanPhamDTO.getId());
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.getSanPhamChiTiet(sanPhamDTO.getId());
        if (sanPhamChiTiet != null) {
            respone.put("err", "Sản phẩm này đã có");
            return ResponseEntity.badRequest().body(respone);
        } else {
            SanPhamChiTiet spct = new SanPhamChiTiet();
            spct.setSanPham(sanPham);
            spct.setTrangThai(true);
            spct.setSoLuong(sanPhamDTO.getSoLuong());
            spct.setSoLuongTam(sanPhamDTO.getSoLuong());
            sanPhamChiTietRepository.save(spct);

            respone.put("success", "Tạo mới thành công");
            return ResponseEntity.ok().body(respone);
        }
    }


    @PostMapping("/chinhSua-soLuong-SanPhamChiTiet")
    public ResponseEntity<?> ChinhSuaSoLuongSanPhamChiTiet(@RequestBody SPCTDTO spctdto) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(spctdto.getSpctId());
        sanPhamChiTiet.setSoLuong(spctdto.getSoLuong());
        sanPhamChiTiet.setSoLuongTam(spctdto.getSoLuong());
        sanPhamChiTietRepository.save(sanPhamChiTiet);
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findByProductID(spctdto.getSanPhamId());
        return ResponseEntity.ok().body(sanPhamChiTiets);
    }

    @Transactional
    @PostMapping("/xoa-SanPhamChiTiet")
    public ResponseEntity<?> XoaSanPhamChiTiet(@RequestBody SPCTDTO spctdto) {
        sanPhamChiTietRepository.deleteByIDSPT(spctdto.getSpctId());
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findByProductID(spctdto.getSanPhamId());
        return ResponseEntity.ok().body(sanPhamChiTiets);
    }
}

