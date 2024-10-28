package com.example.backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hoa_don")
public class HoaDon extends Base implements Serializable {

    @Column(name = "ma_hoa_don", columnDefinition = "nvarchar(256) null")
    private String maHoaDon;

    @Column(name = "ghi_chu", columnDefinition = "nvarchar(50) null")
    private String ghiChu;

    @Column(name = "nguoi_nhan", columnDefinition = "nvarchar(256) null")
    private String nguoiNhan;

    @Column(name = "email_nguoi_nhan", columnDefinition = "nvarchar(256) null")
    private String emailNguoiNhan;

    @Column(name = "sdtnguoi_nhan", columnDefinition = "nvarchar(50) null")
    private String SDTNguoiNhan;

    @Column(name = "dia_chi_giao_hang", columnDefinition = "nvarchar(256) null")
    private String diaChiGiaoHang;

    @Column(name = "tien_ship", columnDefinition = "int null")
    private int tienShip;

    @Column(name = "tien_giam", columnDefinition = "int null")
    private int tienGiam;

    @Column(name = "tong_tien_don_hang", columnDefinition = "int null")
    private int tongTienDonHang;

    @Column(name = "tong_tien_hoa_don", columnDefinition = "int null")
    private int tongTienHoaDon;

    @Column(name = "loai_hoa_don", columnDefinition = "int null")
    private int loaiHoaDon;

    @ManyToOne
    @JoinColumn(name = "trang_thai_id", referencedColumnName = "id")
    private TrangThai trangThai;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id", referencedColumnName = "id")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "nhan_vien_id", referencedColumnName = "id")
    private NhanVien nhanVien;

}
