package com.example.backend.controller.customer.sanPham;

import com.example.backend.dto.GioHangDTO;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/cart")
public class AddToCartController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;
    @PostMapping("/addToCart")
    public ResponseEntity<?>addToCart(@RequestBody GioHangDTO gioHangDTO){
        return ResponseEntity.ok().body("Thêm sản phẩm vào giỏ hàng thành công");
    }
}
