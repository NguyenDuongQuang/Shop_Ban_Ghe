package com.example.backend.controller.customer.banHang;

import com.example.backend.entity.HoaDon;
import com.example.backend.service.BillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/hoaDon/datHang/daHuy")
public class HuyDonController  {
    @Autowired
    BillOrderService hoaDonDatHangService;

    @GetMapping("/danhSach")
    public List<HoaDon> listBill5() {
        return hoaDonDatHangService.findHoaDonByTrangThai(5);
    }

    @RequestMapping("/timKiem={search}")
    public List<HoaDon> searchAllBill5(@PathVariable("search") String search) {
        return hoaDonDatHangService.searchAllBill(5, search);

    }

    @RequestMapping("/timKiemNgay={searchDate}")
    public List<HoaDon> searchDateBill5(@PathVariable("searchDate") String searchDate) {
        return hoaDonDatHangService.searchDateBill(5, searchDate);
    }
}
