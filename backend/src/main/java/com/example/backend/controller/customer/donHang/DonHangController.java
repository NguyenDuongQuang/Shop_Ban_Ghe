package com.example.backend.controller.customer.donHang;

import com.example.backend.entity.HoaDon;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.service.BillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/hoaDon/khachHang/donHang")
public class DonHangController {
    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @GetMapping("/danhSach")
    public List<HoaDon> listBill1() {
        return hoaDonDatHangService.findHoaDonByLoai(0);
    }
}
