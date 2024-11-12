package com.example.backend.controller.customer.banHang;


import com.example.backend.repository.HoaDonChiTietRepository;
import com.example.backend.repository.HoaDonRepository;
import com.example.backend.repository.TrangThaiRepository;
import com.example.backend.service.BillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/hoaDon/chiTietHoaDon")
public class ChiTietHoaDonController {
    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    TrangThaiRepository trangThaiRepository;
    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    //Trả ra hoá đơn theo ID, danh sách hoá đn, lịch s của hoá ơn đó
    @GetMapping("/choXacNhan/id={id}")
    public ResponseEntity<?> CTChoXacNhan(@PathVariable("id") long hoa_don_id) {
        return hoaDonDatHangService.CTChoXacNhan(hoa_don_id);
    }

    @GetMapping("/choGiaoHang/id={id}")
    public ResponseEntity<?> CTChoGiaoHang(@PathVariable("id") long hoa_don_id) {
        return hoaDonDatHangService.CTChoGiaoHang(hoa_don_id);
    }

    @GetMapping("/dangGiaoHang/id={id}")
    public ResponseEntity<?> CTDangGiaoHang(@PathVariable("id") long hoa_don_id) {
        return hoaDonDatHangService.CTDangGiaoHang(hoa_don_id);
    }

    @GetMapping("/daGiaoHang/id={id}")
    public ResponseEntity<?> CTDaGiaoHang(@PathVariable("id") long hoa_don_id) {
        return hoaDonDatHangService.CTDaGiaoHang(hoa_don_id);
    }

    @GetMapping("/daHuy/id={id}")
    public ResponseEntity<?> CTDaHuy(@PathVariable("id") long hoa_don_id) {
        return hoaDonDatHangService.CTDaHuy(hoa_don_id);
    }

//    @GetMapping("/xacNhanDaGiao/id={id}")
//    public ResponseEntity<?> CTXacNhanDaGiao(@PathVariable("id") long hoa_don_id) {
//        return hoaDonDatHangService.CTXacNhanDaGiao(hoa_don_id);
//    }


}
