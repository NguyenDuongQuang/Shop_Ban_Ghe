package com.example.backend.service.Impl;


import com.example.backend.dto.UserDTO;
import com.example.backend.entity.KhachHang;
import com.example.backend.entity.NhanVien;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class StaffDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private NhanVienRepository staffRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NhanVien nhanVien = this.staffRepository.findByEmail(username);
        if (nhanVien != null) {
            UserDTO dto = new UserDTO();
            dto.setName(nhanVien.getHoTen());
            dto.setEmail(nhanVien.getEmail());
            dto.setMatKhau(nhanVien.getMatKhau());
            dto.setRole(nhanVien.getUserRoles());
            dto.setSoDienThoai(nhanVien.getSoDienThoai());
            return dto;
        }

        KhachHang khachHang = this.khachHangRepository.findByEmail(username);
        if (khachHang != null) {
            UserDTO dto = new UserDTO();
            dto.setName(khachHang.getHoTen());
            dto.setEmail(khachHang.getEmail());
            dto.setMatKhau(khachHang.getMatKhau());
            dto.setRole(khachHang.getUserRoles());
            dto.setSoDienThoai(khachHang.getSoDienThoai());
            return dto;
        }

        throw new UsernameNotFoundException("Thông tin đăng nhập không hợp lệ !!");

    }
}