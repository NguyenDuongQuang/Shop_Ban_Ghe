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
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet extends Base implements Serializable {

    @Column(name = "so_luong", columnDefinition = "int null")
    private int soLuong;

    @Column(name = "don_gia", columnDefinition = "int null")
    private int donGia;

    @Column(name = "thanh_tien", columnDefinition = "int null")
    private int thanhTien;

    @ManyToOne
    @JoinColumn(name = "hoaDon_id", referencedColumnName = "id")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "san_pham_chi_tiet_id", referencedColumnName = "id")
    private SanPhamChiTiet sanPhamChiTiet;

}
