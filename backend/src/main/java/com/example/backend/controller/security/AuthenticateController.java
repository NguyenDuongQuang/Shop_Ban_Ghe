package com.example.backend.controller.security;

import com.example.backend.config.JwtUtils;
import com.example.backend.entity.KhachHang;
import com.example.backend.entity.NhanVien;
import com.example.backend.helper.UserNotFoundException;
import com.example.backend.model.request.JwtRequest;
import com.example.backend.model.response.JwtResponse;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.service.Impl.StaffDetailsServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
public class AuthenticateController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StaffDetailsServiceImpl staffDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    // generte token
    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) {
        try {
            authenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
            UserDetails userDetails = this.staffDetailsService.loadUserByUsername(jwtRequest.getEmail());
            String token = this.jwtUtils.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        NhanVien nhanVien = this.nhanVienRepository.findByEmail(username);
        if (nhanVien != null) {
            if (nhanVien.getTrangThai() == 1) {
                throw new DisabledException("Nhân viên đã nghỉ việc");
            }
        } else {
            KhachHang khachHang = this.khachHangRepository.findByEmail(username);
            if (khachHang == null) {
                throw new BadCredentialsException("Sai thông tin đăng nhập");
            }
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Error");
        }
    }

    // return the details of current user
    @GetMapping("/current-user")
    public NhanVien getCurrentUser(Principal principal) {
        return (NhanVien) this.staffDetailsService.loadUserByUsername(principal.getName());
    }
}
