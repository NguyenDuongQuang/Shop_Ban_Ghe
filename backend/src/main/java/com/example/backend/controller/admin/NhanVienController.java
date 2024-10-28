package com.example.backend.controller.admin;

import com.example.backend.entity.NhanVien;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nhanVien")
@CrossOrigin("*")
public class NhanVienController {

    @Autowired
    NhanVienService nhanVienService;

    @Autowired
    NhanVienRepository nhanVienRepository;


    @RequestMapping("/danhSach")
    public List<NhanVien> listStaff() {
        return nhanVienService.findAllStaff();
    }

    @PostMapping("/themMoi")
    public ResponseEntity<NhanVien> createStaff(@RequestBody NhanVien staffCreate) {
        return nhanVienService.createStaff(staffCreate);
    }

    @PutMapping("/xoa")
    public ResponseEntity<List<NhanVien>> deleteStaff(@RequestBody NhanVien nhanVien) {
        Long id = nhanVien.getId();
        return nhanVienService.deleteStaff(id);
    }

    //Update
    @PutMapping("/luuChinhSua")
    public ResponseEntity<NhanVien> saveUpdate(@RequestBody NhanVien staffEdit) {
        return nhanVienService.editStaff(staffEdit);
    }


    @RequestMapping("/timKiem/{search}")
    public List<NhanVien> searchAllStaff(@PathVariable("search") String search) {
        return nhanVienService.searchAllStaff(search);
    }

    @RequestMapping("/timKiemNgay/{searchDate}")
    public List<NhanVien> searchDateStaff(@PathVariable("searchDate") String searchDate) {
        return nhanVienService.searchDateStaff(searchDate);
    }

    @RequestMapping("/chinhSua/{id}")
    public NhanVien editStaff (@PathVariable("id") Long id){
        return nhanVienRepository.findByID(id);
    }

}

