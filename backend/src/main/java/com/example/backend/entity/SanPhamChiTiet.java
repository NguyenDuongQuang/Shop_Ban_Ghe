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
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet extends Base implements Serializable {

    @Column(name = "so_luong", columnDefinition = "int null")
    private Integer soLuong;

    @Column(name = "so_luong_tam", columnDefinition = "int null")
    private Integer soLuongTam;

    @Column(name = "trang_thai", columnDefinition = "bit")
    private boolean trangThai;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "san_pham_id", referencedColumnName = "id")
    private SanPham sanPham;


}
