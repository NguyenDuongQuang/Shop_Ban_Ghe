package com.example.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class HoaDonDTO {

    private Long id;

    private String maHoaDon;

    private String ghiChu;

    private String email;

    private String soDienThoai;

    private int tienShip;

    private int tongTienHoaDon;

    private int tongTienDonHang;

    private List<Long> id_hoaDon;

    private String email_user;

    private String diaChi;


    private String nguoiTao;

    private String nguoi_thao_tac;

    private int loaiHoaDon;

    private int tienTraLai;

    private int tienKhachDua;

}
