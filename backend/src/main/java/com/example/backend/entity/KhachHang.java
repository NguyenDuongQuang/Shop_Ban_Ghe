package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "khach_hang")
@Entity
public class KhachHang extends Base implements UserDetails {
    @Column(name = "ho_ten", columnDefinition = "nvarchar(50) not null")
    private String hoTen;

    @Column(name = "so_dien_thoai", columnDefinition = "nvarchar(10) null")
    private String soDienThoai;

    @Column(name = "email", columnDefinition = "nvarchar(200) null")
    private String email;

    @Column(name = "ngay_sinh", columnDefinition = "Date null")
    private Date ngaySinh;

    @Column(name = "dia_chi", columnDefinition = "nvarchar(250) null")
    private String diaChi;

    @Column(name = "mat_khau", columnDefinition = "nvarchar(250) null")
    private String matKhau;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "khachHang")
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
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
