package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nhan_vien")
public class NhanVien extends Base implements UserDetails {

    @Column(name = "ho_ten", columnDefinition = "nvarchar(256) null")
    private String hoTen;

    @Column(name = "so_dien_thoai", columnDefinition = "nvarchar(50) null")
    private String soDienThoai;

    @Column(name = "email", columnDefinition = "nvarchar(256) not null unique")
    private String email;

    @Column(name = "mat_khau", columnDefinition = "nvarchar(256) null ")
    private String matKhau;

    @Column(name = "gioi_tinh", columnDefinition = "bit")
    private Boolean gioiTinh;

    @Column(name = "dia_chi", columnDefinition = "nvarchar(256) null ")
    private String diaChi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "ngay_sinh", columnDefinition = "Datetime null")
    private Date ngaySinh;

    @Column(name = "trang_thai", columnDefinition = "int null")
    private int trangThai;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "staff")
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> set = new HashSet<>();
        this.userRoles.forEach(userRole -> {
            set.add(new Authority(userRole.getRole().getRoleName()));
        });
        return set;
    }

    @Override
    public String getPassword() {
        return this.matKhau;
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
