package com.example.backend.service.Impl;

import com.example.backend.dto.HoaDonChiTietDTO;
import com.example.backend.dto.HoaDonDTO;
import com.example.backend.dto.KhachHangDTO;
import com.example.backend.dto.SanPhamDto;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.BanHangTaiQuayService;
import com.example.backend.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BanHangTaiQuaySeviceImpl implements BanHangTaiQuayService {

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    TrangThaiRepository trangThaiRepository;

    @Override
    public ResponseEntity<?> addHoaDon(HoaDonDTO hoaDonDTO) {
        List<HoaDon>list=hoaDonRepository.getDanhSachHoaDonCho();
        int count=list.size();
        if(count<8){
            NhanVien nhanVien=nhanVienRepository.findByEmail(hoaDonDTO.getEmail_user());
            TrangThai trangThai=trangThaiRepository.findByID(6L);
            HoaDon hoaDon=new HoaDon();
            hoaDon.setTrangThai(trangThai);
            hoaDon.setLoaiHoaDon(1);
            hoaDon.setCreatedby(nhanVien.getHoTen());
            hoaDon.setCreatedDate(new Date());
            hoaDonRepository.save(hoaDon);
            hoaDon.setMaHoaDon("HD" +hoaDon.getId());
            hoaDonRepository.save(hoaDon);
            return ResponseEntity.ok(hoaDon.getId());

        }else {
            Map<String,String>jStringMap=new HashMap<>();
            jStringMap.put("message","Hóa Đơn Vượt Quá Số Lượng ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jStringMap);
        }
    }

    @Override
    public ResponseEntity themSP(SanPhamDto sanPhamDto) {
        return null;
    }

    @Override
    public List<HoaDon> deleteHoaDon(HoaDonDTO hoaDonDTO) {
        HoaDon hoaDon=hoaDonRepository.findByID(hoaDonDTO.getId());
        TrangThai trangThai=trangThaiRepository.findByID(2L);
        hoaDon.setTrangThai(trangThai);
        hoaDonRepository.save(hoaDon);

        List<HoaDonChiTiet >hoaDonChiTiets=hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        for (HoaDonChiTiet hoaDonChiTiet :hoaDonChiTiets){
            SanPhamChiTiet sanPhamChiTiet=hoaDonChiTiet.getSanPhamChiTiet();
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }
        List<HoaDon> hd=hoaDonRepository.getDanhSachHoaDonCho();
        return hd;
    }

    @Override
    public ResponseEntity<?> addKH(KhachHangDTO khachHangDTO) {
        return null;
    }

    @Override
    public ResponseEntity huyHoaDon(HoaDonDTO hoaDonDTO) {
        HoaDon hoaDon = hoaDonRepository.findByID(hoaDonDTO.getId());
        TrangThai trangThai = trangThaiRepository.findByID(2L);
        hoaDon.setTrangThai(trangThai);
        hoaDonRepository.save(hoaDon);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hủy Đơn Hàng Thành Công");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity thanhToan(HoaDonDTO hoaDonDTO) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteHDCT(HoaDonChiTietDTO hoaDonChiTietDTO) {
        return null;
    }
}
