package com.example.backend.dto;

import com.example.backend.entity.LoaiSanPham;
import lombok.Data;

import java.util.Date;

@Data
public class SanPhamDto {

    private Long id;

    private Long id_SPCT;

    private String tenSanPham;

    private Float gia;

    private String origin;

    private Integer trangThai;

    private Date ngayTao;

    private String nguoiTao;

    private LoaiSanPham loaiSanPham;

    private Long  loaiSanPham_id;


    //Dùng để add to cart bên phía customer


    private Long san_pham_id;

    private int soLuongDaChon;

    private int donGia;

    private int soLuong;

    private int tongTien;

    private Long id_hoaDon;

    private Integer soLuongHienCo;

    private Long anh_id;

    private String anh_san_pham;

    private String email_user;
}
