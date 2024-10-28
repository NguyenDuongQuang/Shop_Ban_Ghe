package com.example.backend.service.Impl;


import com.example.backend.controller.message.Message;
import com.example.backend.entity.GioHang;
import com.example.backend.entity.KhachHang;
import com.example.backend.entity.Role;
import com.example.backend.entity.UserRole;
import com.example.backend.repository.GioHangRepository;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRoleRepository;
import com.example.backend.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class KhachHangServceImpl implements KhachHangService {
    @Autowired
    KhachHangRepository customerRepository;

    @Autowired
    GioHangRepository cartRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public List<KhachHang> findAllCustomer() {
        List<KhachHang> khachHangs = customerRepository.findAllCustomer();
        return khachHangs;
    }

    @Override
    public ResponseEntity<KhachHang> createCustomer(KhachHang khachHangCreate) {
        String errorMessage;
        Message errprResponse;

        Optional<KhachHang> checkEmail = Optional.ofNullable(customerRepository.findByEmail(khachHangCreate.getEmail()));
        if (checkEmail.isPresent()) {
            errorMessage = "trùng email khách hàng";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        if (khachHangCreate.getHoTen() == null || khachHangCreate.getSoDienThoai() == null || khachHangCreate.getEmail() == null
                || khachHangCreate.getNgaySinh() == null || khachHangCreate.getDiaChi() == null || khachHangCreate.getMatKhau() == null) {
            errorMessage = "Nhập thông tin đầy đủ";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        // SDT
        if (khachHangCreate.getSoDienThoai().length() != 10) {
            errorMessage = "Số điện thoại phải đủ 10 số";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        //Email
        String email = khachHangCreate.getEmail();
        String emailRegex = "^[A-Za-z0-9+_.-]+@.+";//kiểm tra định dạng email
        Pattern pattern = Pattern.compile(emailRegex);//tạo Pattern để kiểm tra email
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            errorMessage = "Địa chỉ Eamil không đúng định dạng";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        Date currentDate = new Date();
        Date dateOfBirth = khachHangCreate.getNgaySinh();

        if (dateOfBirth.after(currentDate)) {
            errorMessage = "Ngày sinh không thể nằm ở tương lai";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            KhachHang khachHang = new KhachHang();
            khachHang.setHoTen(khachHangCreate.getHoTen());
            khachHang.setSoDienThoai(khachHangCreate.getSoDienThoai());
            khachHang.setEmail(khachHangCreate.getEmail());
            khachHang.setNgaySinh(khachHangCreate.getNgaySinh());
            khachHang.setDiaChi(khachHangCreate.getDiaChi());
            khachHang.setMatKhau(khachHangCreate.getMatKhau());
            customerRepository.save(khachHang);

            GioHang gioHang = new GioHang();
            gioHang.setKhachHang(khachHang);
            cartRepository.save(gioHang);
            return ResponseEntity.ok(khachHang);
        } catch (Exception e) {
            return new ResponseEntity(new Message(e.getMessage(), TrayIcon.MessageType.ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<KhachHang> editCustomer(KhachHang khachHangEdit) {
        String errorMessage;
        Message errprResponse;

        if (khachHangEdit.getHoTen() == null || khachHangEdit.getSoDienThoai() == null || khachHangEdit.getEmail() == null
                || khachHangEdit.getNgaySinh() == null || khachHangEdit.getDiaChi() == null) {
            errorMessage = "Nhập thông tin đầy đủ";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        // SDT
//        String phoneNumber = customerCreate.getPhoneNumber();
        if (khachHangEdit.getSoDienThoai().length() != 10) {
            errorMessage = "Số điện thoại phải đủ 10 số";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        //Email
        String email = khachHangEdit.getEmail();
        String emailRegex = "^[A-Za-z0-9+_.-]+@.+";//kiểm tra định dạng email
        Pattern pattern = Pattern.compile(emailRegex);//tạo Pattern để kiểm tra email
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            errorMessage = "Địa chỉ Eamil không đúng định dạng";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        //Ngày sinh
        Date dateBirth = khachHangEdit.getNgaySinh();
        Date dateC = new Date();
        if (dateBirth.after(dateC)) {
            errorMessage = "Ngày sinh không vượt quá thời gian hiện tại";
            errprResponse = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errprResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<KhachHang> optionalCustomer = customerRepository.findById(khachHangEdit.getId());
            if (optionalCustomer.isPresent()) {
                KhachHang khachHang = optionalCustomer.get();
                khachHang.setHoTen(khachHangEdit.getHoTen());
                khachHang.setSoDienThoai(khachHangEdit.getSoDienThoai());
                khachHang.setEmail(khachHangEdit.getEmail());
                khachHang.setNgaySinh(khachHangEdit.getNgaySinh());
                khachHang.setDiaChi(khachHangEdit.getDiaChi());
//                khachHang.setMatKhau(khachHangEdit.getMatKhau());
                customerRepository.save(khachHang);
                return ResponseEntity.ok(khachHang);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return new ResponseEntity(new Message(e.getMessage(), TrayIcon.MessageType.ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<List<KhachHang>> deleteCustomer(Long id) {
        try {
            Optional<KhachHang> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                KhachHang khachHang = optionalCustomer.get();
                khachHang.setDeleted(true);
                customerRepository.save(khachHang);

                List<KhachHang> khachHangList = findAllCustomer();
                return ResponseEntity.ok(khachHangList);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public List<KhachHang> searchAllCustomer(String search) {
        List<KhachHang> khachHangList = customerRepository.findCustomerAll(search);
        return khachHangList;
    }

    @Override
    public List<KhachHang> searchDateCustomer(String searchDate) {
        LocalDate searchdate = LocalDate.parse(searchDate);
        List<KhachHang> khachHangList = customerRepository.findCustomerDate(searchdate);
        return khachHangList;
    }

    @Override
    public ResponseEntity Register(KhachHang create) {
        KhachHang khachHangCheckEmail = customerRepository.findByEmail(create.getEmail());
        KhachHang khachHangCheckSDT = customerRepository.findBySDT(create.getSoDienThoai());
        if (khachHangCheckEmail != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trùng email");
        } else if (khachHangCheckSDT != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Trùng số điện thoại");
        } else {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
            inputDateFormat.setTimeZone(TimeZone.getTimeZone("ICT"));
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String ngaySinhFormatted = outputDateFormat.format(create.getNgaySinh());
            KhachHang khachHang = new KhachHang();
            khachHang.setHoTen(create.getHoTen());
            khachHang.setNgaySinh(new Date(ngaySinhFormatted));
            khachHang.setSoDienThoai(create.getSoDienThoai());
            khachHang.setDiaChi(create.getDiaChi());
            khachHang.setEmail(create.getEmail());
            khachHang.setMatKhau(passwordEncoder.encode(create.getMatKhau()));
            customerRepository.save(khachHang);

            Role role = roleRepository.find("CUSTOMER");

            Set<UserRole> userRoleSet = new HashSet<>();
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setKhachHang(khachHang);
            userRoleSet.add(userRole);

            userRoleRepository.save(userRole);

            GioHang gioHang = new GioHang();
            gioHang.setKhachHang(khachHang);
            cartRepository.save(gioHang);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
}
