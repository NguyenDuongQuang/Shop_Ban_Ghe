package com.example.backend.service.Impl;

import com.example.backend.controller.message.Message;
import com.example.backend.entity.NhanVien;
import com.example.backend.entity.Role;
import com.example.backend.entity.UserRole;
import com.example.backend.helper.UserFoundException;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRoleRepository;
import com.example.backend.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    NhanVienRepository staffRepository;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;


    @Override
    public List<NhanVien> findAllStaff() {
        List<NhanVien> staffs = staffRepository.findAllStaff();
        return staffs;
    }

    @Override
    public ResponseEntity<NhanVien> createStaff(NhanVien staffCreate) {
        String errorMessage;
        Message errorRespone;

        Optional<NhanVien> checkEmail = Optional.ofNullable(staffRepository.findByEmail(staffCreate.getEmail()));
        if (checkEmail.isPresent()){
            errorMessage = "Trùng email nhân viên";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }

        if (staffCreate.getHoTen() == null ||
                staffCreate.getGioiTinh() == null ||
                staffCreate.getDiaChi() == null || staffCreate.getSoDienThoai() == null ||
                staffCreate.getNgaySinh() == null || staffCreate.getEmail() == null) {
            errorMessage = "Nhập đầy đủ thông tin";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }

        //SDT
        if (staffCreate.getSoDienThoai().length() != 10) {
            errorMessage = "SDT phải đủ 10 số";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }
        //email
        String email = staffCreate.getEmail();
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            errorMessage = "Mail không đúng định dạng";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }

        //dateOfbirth
        Date dateC = new Date();
        Date dateOfBirth = staffCreate.getNgaySinh();
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(birthDate, currentDate).getYears();

        if(age <= 16){
            errorMessage = "Nhan vien duoi 16 tuoi";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }


        if (dateOfBirth.after(dateC)) {
            errorMessage = "Ngày sinh không được vượt qua thời gian hiện tại";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }
        try {
            NhanVien staff = new NhanVien();
            staff.setHoTen(staffCreate.getHoTen());
            staff.setGioiTinh(staffCreate.getGioiTinh());
            staff.setEmail(staffCreate.getEmail());
            staff.setDiaChi(staffCreate.getDiaChi());
            staff.setNgaySinh(staffCreate.getNgaySinh());
            staff.setMatKhau(staffCreate.getMatKhau());
            staff.setSoDienThoai(staffCreate.getSoDienThoai());
//            staff.setSoCanCuoc(staffCreate.getSoCanCuoc());
            staffRepository.save(staff);

            Role role = roleRepository.find("STAFF");

            Set<UserRole> userRoleSet = new HashSet<>();
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setStaff(staff);
            userRoleSet.add(userRole);
            userRoleRepository.save(userRole);
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new Message(e.getMessage(), TrayIcon.MessageType.ERROR), HttpStatus.BAD_REQUEST);
        }

    }


    // StaffUpdate
    @Override
    public ResponseEntity<NhanVien> editStaff(NhanVien staffEdit) {
        String errorMessage;
        Message errorRespone;

        if (staffEdit.getHoTen() == null || staffEdit.getGioiTinh() == null ||
                staffEdit.getDiaChi() == null || staffEdit.getSoDienThoai() == null ||
                staffEdit.getNgaySinh() == null || staffEdit.getEmail() == null
        ) {
            errorMessage = "Nhập đầy đủ thông tin";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }

        //SDT
        if (staffEdit.getSoDienThoai().length() != 10) {
            errorMessage = "Số điện thoại phải đủ 10 số";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }
        //email
        String email = staffEdit.getEmail();
        //dinh dang email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        //partern de ktra email
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            errorMessage = "Mail is not in the correct format";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }


        //dateOfbirth
        Date dateOfBirth = staffEdit.getNgaySinh();
        Date dateC = new Date();
        if (dateOfBirth.after(dateC)) {
            errorMessage = "Ngày sinh không được vượt quá thời gian hiện tại";
            errorRespone = new Message(errorMessage, TrayIcon.MessageType.ERROR);
            return new ResponseEntity(errorRespone, HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<NhanVien> optionalStaff = staffRepository.findById(staffEdit.getId());
            if (optionalStaff.isPresent()) {
                NhanVien staff = optionalStaff.get();
                staff.setHoTen(staffEdit.getHoTen());
                staff.setEmail(staffEdit.getEmail());
                staff.setDiaChi(staffEdit.getDiaChi());
                staff.setNgaySinh(staffEdit.getNgaySinh());
                staff.setSoDienThoai(staffEdit.getSoDienThoai());
                staff.setGioiTinh(staffEdit.getGioiTinh());
                staff.setTrangThai(staffEdit.getTrangThai());
                staffRepository.save(staff);
                return ResponseEntity.ok(staff);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return new ResponseEntity(new Message(e.getMessage(), TrayIcon.MessageType.ERROR), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<List<NhanVien>> deleteStaff(Long id) {
        try {
            Optional<NhanVien> optionalStaff = staffRepository.findById(id);
            if (optionalStaff.isPresent()) {
                NhanVien staff = optionalStaff.get();
                staff.setDeleted(true);
                staffRepository.save(staff);
                List<NhanVien> staffList = findAllStaff();
                return ResponseEntity.ok(staffList);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @Override
    public List<NhanVien> searchAllStaff(String search) {
        List<NhanVien> staffList = staffRepository.findStaffAll(search);
        return staffList;
    }

    @Override
    public List<NhanVien> searchDateStaff(String searchDate) {
        LocalDate searchdate = LocalDate.parse(searchDate);
        List<NhanVien> staffList = staffRepository.findStaffDate(searchdate);
        return staffList;
    }


    @Override
    public NhanVien createStaffV1(NhanVien user, Set<UserRole> userRoles) throws Exception {
        NhanVien local = staffRepository.findByEmail(user.getEmail());
        if (local != null) {
            throw new UserFoundException();
        } else {
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            local = this.staffRepository.save(user);
        }
        return local;
    }

    @Override
    public NhanVien getStaff(String email) {
        return staffRepository.findByEmail(email);

    }

    @Override
    public void deleteUser(Long userId) {
        staffRepository.deleteById(userId);
    }
}

