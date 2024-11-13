package com.example.backend.controller.admin.donHang;

import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.HoaDon;
import com.example.backend.entity.NhanVien;
import com.example.backend.repository.HoaDonRepository;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.service.BillOrderService;
import com.example.backend.service.Impl.InHoaDonService;
import com.example.backend.service.Impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/hoaDon/datHang/choXacNhan")
public class ChoXacNhanController {

    @Autowired
    BillOrderService hoaDonDatHangService;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    InHoaDonService inHoaDonService;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    MailService mailService;

    @GetMapping("/danhSach")
    public List<HoaDon> listBill1() {

        return hoaDonDatHangService.findHoaDonByTrangThai(1L);
    }
    @PostMapping("/capNhatTrangThai/daXacNhan")
    public ResponseEntity<?> updateStatus2(@RequestBody HoaDonDTO hoaDonDTO)  {
        Long id = hoaDonDTO.getId();
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        return ResponseEntity.ok().body(hoaDonDatHangService.updateTrangThai(2, id));
    }
    @PostMapping("/capNhatTrangThai/huyDon")
    public ResponseEntity<Map<String, Boolean>> updateStatus5(@RequestBody HoaDonDTO hoaDonDTO) {
        Long id = hoaDonDTO.getId();
        String ghiChu = hoaDonDTO.getGhiChu();
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        hoaDonDatHangService.capNhatTrangThaiHuyDon(5, id, ghiChu);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/inHoaDon/{id}")
    public ResponseEntity<byte[]> inHoaDon(@PathVariable("id") long id) {
        return inHoaDonService.hoaDonDatHangPdf(id);
    }

    @GetMapping("/guiMail/{id}")
    public void guiMail(@PathVariable("id") long id) {
        HoaDon hoaDon = hoaDonRepository.findByID(id);
        if(hoaDon.getEmailNguoiNhan() != null || hoaDon.getEmailNguoiNhan().isEmpty()){
            try {
                mailService.guiMailKhiThaoTac(hoaDon.getEmailNguoiNhan(), hoaDon);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/timKiem={search}")
    public List<HoaDon> searchAllBill1(@PathVariable("search") String search) {
        return hoaDonDatHangService.searchAllBill(1, search);
    }

    @RequestMapping("/timKiemNgay={searchDate}")
    public List<HoaDon> searchDateBill1(@PathVariable("searchDate") String searchDate) {
        return hoaDonDatHangService.searchDateBill(1, searchDate);
    }

    @PostMapping("/xacNhanDon/tatCa")
    public ResponseEntity<Map<String, Boolean>> updateStatusAll2(@RequestBody HoaDonDTO hoaDonDTO) {
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        hoaDonDatHangService.capNhatTrangThai_TatCa(1,2,"Xác nhận đơn",nhanVien.getHoTen());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/xacNhanDon/daChon")
    public List<HoaDon> updateStatusSelect2(@RequestBody HoaDonDTO hoaDonDTO) {
        String email = hoaDonDTO.getEmail_user();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        return hoaDonDatHangService.capNhatTrangThai_DaChon(hoaDonDTO, 2,"Xác nhận đơn",nhanVien.getHoTen());
    }

    @PutMapping("/huyDon/daChon")
    public List<HoaDon> updateStatusSelect5(@RequestBody HoaDonDTO hoaDonDTO) {
        String email = hoaDonDTO.getEmail_user();
        String ghiChu= hoaDonDTO.getGhiChu();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email);
        return hoaDonDatHangService.capNhatTrangThaiHuy_DaChon(hoaDonDTO, nhanVien.getHoTen(),ghiChu);
    }

}
