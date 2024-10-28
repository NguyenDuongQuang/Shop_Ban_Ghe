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
@Table(name = "hinh_anh")
public class Anh extends Base implements Serializable {
    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private SanPham sanPham;

    @Column(name = "name", columnDefinition = "nvarchar(256) null")
    private String tenAnh;

    @Column(name = "anh_mac_dinh", columnDefinition = "bit")
    private Boolean anhMacDinh;
}
