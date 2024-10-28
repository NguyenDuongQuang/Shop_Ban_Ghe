package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "gio_hang_chi_tiet")
public class GioHangChiTiet extends Base implements Serializable {

    @ManyToOne
    @JoinColumn(name = "gio_hang_id")
    private GioHang gioHang;

    @ManyToOne
    @JoinColumn(name = "san_pham_chi_tiet_id")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "so_luong", columnDefinition = "int null")
    private int soLuong;

    @Column(name = "don_gia", columnDefinition = "int ")
    private int donGia;

    @Column(name = "thanh_tien", columnDefinition = "varchar(50) not null")
    private BigDecimal thanhTien;
}
