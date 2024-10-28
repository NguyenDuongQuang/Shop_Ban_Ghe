package com.example.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class KhachHangDTO {

    private Long id;

    private String hoTen;

    private String soDienThoai;

    private long id_hoaDon;

    private String email;

    private Date ngaySinh;

    private String diaChi;


}
