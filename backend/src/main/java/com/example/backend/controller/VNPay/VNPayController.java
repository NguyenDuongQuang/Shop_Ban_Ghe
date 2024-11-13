package com.example.backend.controller.VNPay;

import com.example.backend.config.VNPayConfig;
import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.BillOrderService;
import com.example.backend.service.Impl.MailService;
import com.example.backend.service.Impl.VnpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
public class VNPayController {
    @Autowired
    VnpayService vnpayService;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    TrangThaiRepository trangThaiRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    MailService mailService;

    HoaDonDTO dto=null;

    @PostMapping("/payment/create")
    public ResponseEntity<?> createUrl(@RequestBody HoaDonDTO hoaDonDTO) {
        Map<String, String> respone = new HashMap<>();

        HoaDon hoaDon = hoaDonRepository.findByID(hoaDonDTO.getId());
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet spct = sanPhamChiTietRepository.findByID(hoaDonChiTiet.getSanPhamChiTiet().getId());
            int soLuong = spct.getSoLuong();
            int soLuongSPHoaDon = hoaDonChiTiet.getSoLuong();
            int soLuongUpdate = soLuong - soLuongSPHoaDon;

            if (soLuongUpdate < 0) {
                respone.put("err", "Hiện tại sản phẩm " + spct.getSanPham().getTenSanPham() + " chúng tôi chỉ còn số lượng là " + spct.getSoLuong() + " bạn vui lòng đặt hàng và thanh toán lại!");
                return ResponseEntity.badRequest().body(respone);
            }
        }
        dto = hoaDonDTO;
        return vnpayService.createPaymentMuaNgay(hoaDonDTO);
    }

    @PostMapping("/payment/MuaNgay/create")
    public ResponseEntity<?> MuaNgaycreateUrl(@RequestBody HoaDonDTO hoaDonDTO) {
        dto = hoaDonDTO;
        return vnpayService.createPaymentMuaNgay(hoaDonDTO);
    }

    @Transactional
    @RequestMapping("/payment/return")
    public ResponseEntity<String> returnPayment(HttpServletRequest request) {
        int paymentStatus = VNPayConfig.orderReturn(request);
        HoaDon hoaDon = hoaDonRepository.findByID(dto.getId());
        KhachHang khachHang = khachHangRepository.findByEmail(dto.getEmail_user());
        GioHang gioHang = gioHangRepository.findbyCustomerID(khachHang.getId());
        List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findByCartID(gioHang.getId());
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            gioHangChiTietRepository.deleteGioHangChiTiet(hoaDonChiTiet.getSanPhamChiTiet().getId());
        }

        for (GioHangChiTiet gioHangChiTiet : gioHangChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(gioHangChiTiet.getSanPhamChiTiet().getId());
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - gioHangChiTiet.getSoLuong());
            sanPhamChiTiet.setSoLuongTam(sanPhamChiTiet.getSoLuong() - gioHangChiTiet.getSoLuong());
            if (sanPhamChiTiet.getSoLuong() == 0) {

                hoaDonChiTietRepository.deleteByID(6);

                List<GioHangChiTiet> ghct = gioHangChiTietRepository.findCartBySPCTID(sanPhamChiTiet.getId());
                for (GioHangChiTiet gioHangChiTiet1 : ghct) {
                    gioHangChiTietRepository.deleteById(gioHangChiTiet1.getId());
                }

            } else {
                sanPhamChiTiet.setTrangThai(true);
            }
        }

        TrangThai trangThai = trangThaiRepository.findByID(1L);

        hoaDon.setGhiChu(dto.getGhiChu());
        hoaDon.setTongTienHoaDon(dto.getTongTienHoaDon());
        hoaDon.setTongTienDonHang(dto.getTongTienDonHang());
        hoaDon.setEmailNguoiNhan(dto.getEmail());
        hoaDon.setSDTNguoiNhan(dto.getSoDienThoai());
        hoaDon.setTienShip(dto.getTienShip());
        hoaDon.setDiaChiGiaoHang(dto.getDiaChi());
        hoaDon.setTrangThai(trangThai);
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNguoiNhan(khachHang.getHoTen());
        hoaDon.setLoaiHoaDon(1);
        hoaDonRepository.save(hoaDon);
        if (hoaDon.getEmailNguoiNhan() != null && !hoaDon.getEmailNguoiNhan().isEmpty()) {
            try {
                mailService.sendOrderConfirmationEmail(hoaDon.getEmailNguoiNhan(), hoaDon);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        if (paymentStatus == 1) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://127.0.0.1:5501/templates/banHang/online/vnpay/Success.html"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://127.0.0.1:5501/templates/banHang/online/vnpay/Error.html"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }

    @Transactional
    @RequestMapping("/payment/MuaNgay/return")
    public ResponseEntity<String> returnPaymentMuaNgay(HttpServletRequest request) {
        int paymentStatus = VNPayConfig.orderReturn(request);
        HoaDon hoaDon = hoaDonRepository.findByID(dto.getId());
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(hoaDonChiTiet.getSanPhamChiTiet().getId());
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong());
            sanPhamChiTiet.setSoLuongTam(sanPhamChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong());
            if (sanPhamChiTiet.getSoLuong() == 0) {

                hoaDonChiTietRepository.deleteByID(6);

                List<GioHangChiTiet> ghct = gioHangChiTietRepository.findCartBySPCTID(sanPhamChiTiet.getId());
                for (GioHangChiTiet gioHangChiTiet1 : ghct) {
                    gioHangChiTietRepository.deleteById(gioHangChiTiet1.getId());
                }

            } else {
                sanPhamChiTiet.setTrangThai(true);
            }
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }

        TrangThai trangThai = trangThaiRepository.findByID(1L);

        hoaDon.setGhiChu(dto.getGhiChu());
        hoaDon.setTongTienHoaDon(dto.getTongTienHoaDon());
        hoaDon.setTongTienDonHang(dto.getTongTienDonHang());
        hoaDon.setEmailNguoiNhan(dto.getEmail());
        hoaDon.setSDTNguoiNhan(dto.getSoDienThoai());
        hoaDon.setTienShip(dto.getTienShip());
        hoaDon.setDiaChiGiaoHang(dto.getDiaChi());
        hoaDon.setTrangThai(trangThai);
        hoaDon.setCreatedby(dto.getNguoiTao());
        hoaDon.setNguoiNhan(dto.getNguoiTao());
        hoaDon.setLoaiHoaDon(1);
        hoaDonRepository.save(hoaDon);
        if (hoaDon.getEmailNguoiNhan() != null && !hoaDon.getEmailNguoiNhan().isEmpty()) {
            try {
                mailService.sendOrderConfirmationEmail(hoaDon.getEmailNguoiNhan(), hoaDon);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        if (paymentStatus == 1) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://127.0.0.1:5501/templates/banHang/online/vnpay/Success.html"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://127.0.0.1:5501/templates/banHang/online/vnpay/Error.html"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }
}

