package com.example.backend.controller.admin;

import com.example.backend.dto.HinhAnhDTO;
import com.example.backend.dto.SanPhamChiTietDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.SanPhamChiTietService;
import com.example.backend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sanPhamChiTiet")
@CrossOrigin("*")
public class SanPhamChiTietController {

    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    SanPhamService sanPhamService;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    ImageRepository hinhAnhRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    Long spct_id;

    //Hien thi
    @GetMapping("/danhSach")
    public ResponseEntity<List<SanPhamChiTiet>> getColor() {
        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/dsCTSP")
    public ResponseEntity<List<SanPhamChiTiet>> getProduct(@RequestParam("san_pham_id") Long id) {
        List<SanPhamChiTiet> product = sanPhamChiTietRepository.findSpctByIdSp(id);
        for (SanPhamChiTiet sanPhamChiTiet : product) {
            Anh hinhAnh = hinhAnhRepository.findAnhMacDinh(sanPhamChiTiet.getSanPham().getId());
            if (hinhAnh == null) {
                sanPhamChiTiet.setTrangThai(false);
                sanPhamChiTietRepository.save(sanPhamChiTiet);
            }

        }
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/themAnh/{id}")
    public ResponseEntity<?> getHinhAnh(@PathVariable("id") long id) {
        List<SanPhamChiTiet> product = sanPhamChiTietRepository.findByProductID(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/themAnhSpctId/{id}")
    public ResponseEntity<?> getHinhAnhBSpct(@PathVariable("id") long id) {
        List<SanPhamChiTiet> product = sanPhamChiTietRepository.findByProduct(id);
        return ResponseEntity.ok().body(product);
    }

    //Hien thi theo id
    @GetMapping("/chinhSua/{id}")
    public SanPhamChiTiet editProductDetails(@PathVariable("id") Long id) {
        return sanPhamChiTietRepository.findByID(id);
    }

    //Sửa và lưu
    @PutMapping("/luuChinhSua")
    public ResponseEntity<SanPhamChiTiet> saveUpdate(@RequestBody SanPhamChiTiet sanPhamChiTiet) {
        return sanPhamChiTietService.saveEdit(sanPhamChiTiet);
    }

    //Xóa
    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<List<SanPhamChiTiet>> deleteProductDetails(@PathVariable("id") Long id) {
        return sanPhamChiTietService.deleteProductDetails(id);
    }

//    @PostMapping("/themMoi")
//    public SanPhamChiTiet saveCreate(@RequestBody HoaDonChiTietDTO detailsDTO) {
//        KichCo size = productSizeRepository.findByID(detailsDTO.getIdSize());
//        SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
//        sanPhamChiTiet.setKichCo(size);
//        sanPhamChiTiet.setSoLuong(detailsDTO.getQuantity());
//        sanPhamChiTiet.setSoLuongTam(detailsDTO.getQuantity());
//        sanPhamChiTietRepository.save(sanPhamChiTiet);
//        return sanPhamChiTiet;
//    }

    //Tìm kiếm
    @RequestMapping("/timKiem={search}")
    public List<SanPhamChiTiet> searchAll(@PathVariable("search") String search) {
        return sanPhamChiTietService.searchAllProductDetails(search);
    }

    @RequestMapping("/timKiemNgay={searchDate}")
    public List<SanPhamChiTiet> searchDate(@PathVariable("searchDate") String search) {
        return sanPhamChiTietService.searchDateProductDetails(search);
    }

    @GetMapping("/get/SanPhamChiTiet")
    public SanPhamChiTiet getSPCT(@RequestParam long id_SPCT) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(id_SPCT);
        return sanPhamChiTiet;
    }

    @PutMapping("/ChinhSuaSoLuongSPCT")
    public ResponseEntity chinhSuaSoLuongSPCT(@RequestBody SanPhamChiTiet sanPhamChiTiet) {
        return sanPhamChiTietService.chinhSuaSoLuongSPCT(sanPhamChiTiet);
    }

    @PostMapping("/themAnhSanPham")
    public ResponseEntity<?> themAnhSanPham(@RequestBody HinhAnhDTO hinhAnhDTO) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(hinhAnhDTO.getId_spct());
        spct_id = sanPhamChiTiet.getId();
        Anh hinhAnh = null;
        for (String tenAnh : hinhAnhDTO.getTen_anh()) {
            hinhAnh = new Anh();
            hinhAnh.setSanPham(sanPhamChiTiet.getSanPham());
            hinhAnh.setTenAnh(tenAnh);
            hinhAnh.setAnhMacDinh(false);
            hinhAnhRepository.save(hinhAnh);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("mess", "Thêm ảnh thành công");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/hienThiAnh/{id}")
    public ResponseEntity<?> hienThiAnh(@PathVariable("id") long id_spct) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(id_spct);
        List<Anh> hinhAnhs = hinhAnhRepository.getHinhAnhs(sanPhamChiTiet.getSanPham().getId());
        return ResponseEntity.ok().body(hinhAnhs);
    }

    @PutMapping("/setAnhMacDinh")
    public ResponseEntity<?> setAnhMacDinh(@RequestBody HinhAnhDTO hinhAnhDTO) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(hinhAnhDTO.getId_spct());
        Anh check = hinhAnhRepository.findAnhMacDinh(sanPhamChiTiet.getSanPham().getId());

        if (check != null) {
            check.setAnhMacDinh(false);
            hinhAnhRepository.save(check);
            Anh hinhAnh = hinhAnhRepository.findByID(hinhAnhDTO.getId_hinh_anh());
            hinhAnh.setAnhMacDinh(true);
            hinhAnhRepository.save(hinhAnh);

            SanPham sanPham = sanPhamChiTiet.getSanPham();
            sanPham.setTrangThai(0);
            sanPhamRepository.save(sanPham);

            sanPhamChiTiet.setTrangThai(true);
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            Map<String, Object> response = new HashMap<>();
            response.put("mess", "Đặt ảnh mặc định thành công");
            return ResponseEntity.ok().body(response);
        } else {
            Anh hinhAnh = hinhAnhRepository.findByID(hinhAnhDTO.getId_hinh_anh());
            hinhAnh.setAnhMacDinh(true);
            hinhAnhRepository.save(hinhAnh);

            SanPham sanPham = sanPhamChiTiet.getSanPham();
            sanPham.setTrangThai(0);
            sanPhamRepository.save(sanPham);

            sanPhamChiTiet.setTrangThai(true);
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            Map<String, Object> response = new HashMap<>();
            response.put("mess", "Đặt ảnh mặc định thành công");
            return ResponseEntity.ok().body(response);
        }
    }

    @Transactional
    @PutMapping("/xoaAnh")
    public ResponseEntity<?> xoaAnh(@RequestBody HinhAnhDTO hinhAnhDTO) {
        hinhAnhRepository.xoaAnh(hinhAnhDTO.getId_hinh_anh());
        List<Anh> hinhAnhs = hinhAnhRepository.findByIDProduct(spct_id);
        return ResponseEntity.ok().body(hinhAnhs);
    }

    @PostMapping("/update/trangThai")
    public ResponseEntity<?> updateTrangThai(@RequestBody SanPhamChiTietDTO dto) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(dto.getId());
        sanPhamChiTiet.setTrangThai(dto.isStatus());
        if (dto.isStatus() == false) {
            List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findCartBySPCTID(sanPhamChiTiet.getId());
            for (GioHangChiTiet gioHangChiTiet : gioHangChiTiets) {
                gioHangChiTiet.setDeleted(true);
                gioHangChiTietRepository.save(gioHangChiTiet);
            }

            List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findBySPCTID(sanPhamChiTiet.getId());
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                hoaDonChiTiet.setDeleted(true);
                hoaDonChiTietRepository.save(hoaDonChiTiet);
            }
        } else if (dto.isStatus() == true) {
            List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findCartBySPCTIDDeleteTrue(sanPhamChiTiet.getId());
            for (GioHangChiTiet gioHangChiTiet : gioHangChiTiets) {
                gioHangChiTiet.setDeleted(false);
                gioHangChiTietRepository.save(gioHangChiTiet);
            }

            List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findBySPCTIDDeleteTrue(sanPhamChiTiet.getId());
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                hoaDonChiTiet.setDeleted(false);
                hoaDonChiTietRepository.save(hoaDonChiTiet);
            }
        }
        sanPhamChiTietRepository.save(sanPhamChiTiet);
        Map<String, String> respone = new HashMap<>();
        respone.put("Mess", "done");
        return ResponseEntity.ok().body(respone);
    }
}