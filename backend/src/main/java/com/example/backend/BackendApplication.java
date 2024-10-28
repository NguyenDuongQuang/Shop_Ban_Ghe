package com.example.backend;

import com.example.backend.entity.NhanVien;
import com.example.backend.entity.Role;
import com.example.backend.entity.UserRole;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import  org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BackendApplication {

//        @Autowired
//    private NhanVienService nhanVienService;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    @Autowired
//    NhanVienRepository nhanVienRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
//    @Override
//    public void run(String... args) throws Exception {
//        if(roleRepository.find("ADMIN") == null){
//            Role role = new Role();
//            role.setRoleName("ADMIN");
//            roleRepository.save(role);
//        }
//
//        if(roleRepository.find("STAFF") == null){
//            Role role = new Role();
//            role.setRoleName("STAFF");
//            roleRepository.save(role);
//        }
//
//        if(roleRepository.find("CUSTOMER") == null){
//            Role role = new Role();
//            role.setRoleName("CUSTOMER");
//            roleRepository.save(role);
//        }
//
//        NhanVien nhanVien = null;
//        nhanVien = nhanVienRepository.findByEmail("admin@gmail.com");
//        if (nhanVien != null) {
//            String account = nhanVien.getEmail();
//        } else {
//            nhanVien = new NhanVien();
//            nhanVien.setDiaChi("Hà Nội");
//            nhanVien.setGioiTinh(true);
//            nhanVien.setMatKhau(this.bCryptPasswordEncoder.encode("123123"));
//            nhanVien.setEmail("admin@gmail.com");
//            nhanVien.setHoTen("SD94");
//
//            Role rolee = roleRepository.find("ADMIN");
//
//            Set<UserRole> userRoleSet = new HashSet<>();
//            UserRole userRole = new UserRole();
//            userRole.setRole(rolee);
//            userRole.setStaff(nhanVien);
//            userRoleSet.add(userRole);
//
//            NhanVien user = nhanVienService.createStaffV1(nhanVien, userRoleSet);
//        }
//    }
//

}
