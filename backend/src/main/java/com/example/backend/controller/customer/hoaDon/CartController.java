package com.example.backend.controller.customer.hoaDon;

import com.example.backend.dto.GioHangChiTietDTO;
import com.example.backend.entity.GioHang;
import com.example.backend.entity.GioHangChiTiet;
import com.example.backend.entity.KhachHang;
import com.example.backend.entity.SanPhamChiTiet;
import com.example.backend.repository.*;
import com.example.backend.service.Impl.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequestMapping("/gioHang")
@CrossOrigin("*")
public class CartController {

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    GioHangService gioHangService;


    @GetMapping("/danhSach/{email}")

    public ResponseEntity<?>cart(@PathVariable("email") String email){
        KhachHang khachHang = khachHangRepository.findByEmail(email);
        GioHang gioHang = gioHangRepository.findbyCustomerID(khachHang.getId());
        long idCart = gioHang.getId();
        List<GioHangChiTietDTO>ghct=new ArrayList<>();
        List<GioHangChiTiet> cartList = gioHangChiTietRepository.findByCartID(idCart);
        for(GioHangChiTiet gioHangChiTiet : cartList){
            SanPhamChiTiet sanPhamChiTiet=gioHangChiTiet.getSanPhamChiTiet();
            if(sanPhamChiTiet.getSoLuong()==0){
                gioHangChiTiet.setSoLuong(0);
                gioHangChiTiet.setThanhTien(BigDecimal.ZERO);
                gioHangChiTietRepository.save(gioHangChiTiet);
            }
            GioHangChiTietDTO dto = new GioHangChiTietDTO();
            String hinhAnhs = imageRepository.getAnhMacDinh(sanPhamChiTiet.getSanPham().getId());
            dto.setId(gioHangChiTiet.getId());
            dto.setSanPhamChiTiet(sanPhamChiTiet);
            dto.setSoLuong(gioHangChiTiet.getSoLuong());
            dto.setDonGia(gioHangChiTiet.getDonGia());
            dto.setThanhTien(gioHangChiTiet.getThanhTien());
            dto.setAnh_san_pham(hinhAnhs);

            ghct.add(dto);
        }
        return ResponseEntity.ok().body(ghct);
    }
    @PostMapping("/xoa/gioHangChiTiet")
    public List<GioHangChiTiet> deletedCartDetails(@RequestBody GioHangChiTiet request) {
        Long id_cart_details = request.getId();

        Optional<GioHangChiTiet> shoppingCart = gioHangChiTietRepository.findById(id_cart_details);
        long id_cart = 0;
        if (shoppingCart.isPresent()) {
            GioHangChiTiet cartDetials = shoppingCart.get();
            id_cart = cartDetials.getGioHang().getId();
            cartDetials.setDeleted(true);
            gioHangChiTietRepository.save(cartDetials);
        }

        List<GioHangChiTiet> cartList = gioHangChiTietRepository.findByCartID(id_cart);
        return cartList;
    }

    @PostMapping("/update/soLuongGioHangChiTiet")
    public ResponseEntity<?> updateSoLuongGioHangChiTiet(@RequestBody GioHangChiTietDTO dto) {
        return gioHangService.updateSoLuong(dto);
    }
}
