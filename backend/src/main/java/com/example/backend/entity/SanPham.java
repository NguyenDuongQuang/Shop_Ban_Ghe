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
@Table(name = "san_pham")
public class SanPham extends Base implements Serializable {

    @Column(name = "ten_san_pham", columnDefinition = "nvarchar(256) null")
    private String tenSanPham;

    @Column(name = "mota", columnDefinition = "nvarchar(256) null")
    private String moTa;

    @Column(name = "gia", columnDefinition = "nvarchar(256) null")
    private Float gia;

    @Column(name = "trang_thai", columnDefinition = "int null")
    private Integer trangThai;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loaiSanPham_id", referencedColumnName = "id")
    private LoaiSanPham loaiSanPham;
}
