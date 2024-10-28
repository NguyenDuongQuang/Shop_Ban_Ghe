package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dia_chi")
@Entity
public class DiaChi extends Base implements Serializable {
    @Column(name = "dia_chi", columnDefinition = "nvarchar(500) null")
    private String diaChi;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @Column(name = "dia_chi_mac_dinh")
    private boolean diaChiMacDinh = false;
}
