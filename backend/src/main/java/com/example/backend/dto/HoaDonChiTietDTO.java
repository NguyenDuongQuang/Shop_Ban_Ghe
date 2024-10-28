package com.example.backend.dto;

import com.example.backend.entity.HoaDon;
import com.example.backend.entity.SanPhamChiTiet;
import lombok.Data;

@Data
public class HoaDonChiTietDTO {
    private Long id;

    private Long idProduct;

    private String product;

    private Integer quantity;

    private Integer soLuong;

    private Integer donGia;

    private Integer thanhTien;

    private HoaDon hoaDon;

    private SanPhamChiTiet sanPhamChiTiet;

    private String anhSanPham;

    private int soLuongcapNhat;

    private String email_user;
}
