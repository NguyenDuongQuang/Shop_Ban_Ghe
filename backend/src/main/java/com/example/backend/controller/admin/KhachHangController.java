package com.example.backend.controller.admin;

import com.example.backend.entity.DiaChi;
import com.example.backend.entity.KhachHang;
import com.example.backend.repository.DiaChiRepository;
import com.example.backend.repository.GioHangRepository;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/khachHang")
public class KhachHangController {

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    KhachHangRepository customerRepository;

    @Autowired
    DiaChiRepository addressRepository;


    @Autowired
    GioHangRepository gioHangRepository;

    @GetMapping("/danhSach")
    public List<KhachHang> listCustomer() {
        return khachHangService.findAllCustomer();
    }

    @PostMapping(value = "/themMoi")
    public ResponseEntity<KhachHang> createCustomer(@RequestBody KhachHang khachHangCreate) {
        return khachHangService.createCustomer(khachHangCreate);
    }

    @PostMapping("/xoaKhachHang")
    public ResponseEntity<List<KhachHang>> deleteCustomer(@RequestBody KhachHang khachHang) {
        Long id = khachHang.getId();
        return khachHangService.deleteCustomer(id);
    }

    @GetMapping("/chinhSua/{id}")
    public KhachHang customerEdit(@PathVariable("id") Long id) {
        return customerRepository.findByID(id);
    }

    @PutMapping("/luu-chinh-sua")
    public ResponseEntity<KhachHang> saveUpdate(@RequestBody KhachHang khachHangUpdate) {
        return khachHangService.editCustomer(khachHangUpdate);
    }

    @RequestMapping("/timKiem={search}")
    public List<KhachHang> searchAllCustomer(@PathVariable("search") String search) {
        return khachHangService.searchAllCustomer(search);
    }

    @RequestMapping("/timKiemNgay={searchDate}")
    public List<KhachHang> searchDateCustomer(@PathVariable("searchDate") String searchDate) {
        return khachHangService.searchDateCustomer(searchDate);
    }

    @RequestMapping("/themDiaChi")
    public String addAddress(@RequestParam("dia_chi") String dia_chi,
                             @RequestParam("khach_hang_id") long khach_hang_id) {
        Optional<KhachHang> optionalCustomer = customerRepository.findById(khach_hang_id);
        if (optionalCustomer.isPresent()) {
            KhachHang khachHang = optionalCustomer.get();
            DiaChi diaChi = new DiaChi();
            diaChi.setKhachHang(khachHang);
            diaChi.setDiaChi(dia_chi);
            diaChi.setDiaChiMacDinh(false);
            addressRepository.save(diaChi);
        }
        return "add address done";
    }

    @PostMapping("/them-dia-chi-mac-dinh")
    public String setAddress(@RequestParam("dia_chi_id") long dia_chi_id,
                             @RequestParam("khach_hang_id") long khach_hang_id) {
        List<DiaChi> diaChis = addressRepository.findByCustomerID(khach_hang_id);
        for (DiaChi diaChi : diaChis) {
            diaChi.setDiaChiMacDinh(false);
            addressRepository.save(diaChi);
        }

        DiaChi diaChi = addressRepository.findbyCustomerAndID(khach_hang_id, dia_chi_id);
        diaChi.setDiaChiMacDinh(true);
        addressRepository.save(diaChi);
        return "true";
    }

    @PostMapping("/register")
    public ResponseEntity Register(@RequestBody KhachHang create) {
        return khachHangService.Register(create);
    }
}

