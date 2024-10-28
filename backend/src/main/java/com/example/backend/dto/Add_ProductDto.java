package com.example.backend.dto;

import lombok.Data;

import java.util.List;
@Data
public class Add_ProductDto {
    private String tenSanPham;
    private Float gia;
    private long loaiSanPham_id;
    private int soLuong;
    private int trangThai;
}
