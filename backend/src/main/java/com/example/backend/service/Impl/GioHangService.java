package com.example.backend.service.Impl;

import com.example.backend.dto.GioHangChiTietDTO;
import com.example.backend.entity.GioHangChiTiet;
import com.example.backend.entity.SanPhamChiTiet;
import com.example.backend.repository.GioHangChiTietRepository;
import com.example.backend.repository.GioHangRepository;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class GioHangService {

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepositoryl;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    public ResponseEntity<?> updateSoLuong(GioHangChiTietDTO dto) {
        Map<String, String> respone = new HashMap<>();

        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findById(dto.getId()).get();
        SanPhamChiTiet sanPhamChiTiet = dto.getSanPhamChiTiet();

        int soLuongSanPhamHienCo = sanPhamChiTiet.getSoLuong();
        int soLuongUpdate = dto.getSoLuongCapNhat();
        int soLuongDuocThemTiep = sanPhamChiTiet.getSoLuong() - gioHangChiTiet.getSoLuong();

        if (soLuongSanPhamHienCo < soLuongUpdate && soLuongUpdate > gioHangChiTiet.getSoLuong()) {
            respone.put("err", "Bạn đã có " + sanPhamChiTiet.getSoLuong() + " sản phẩm này trong giỏ hàng, bạn không thể thêm tiếp vì vượt quá số lượng của sản phẩm");
            return ResponseEntity.badRequest().body(respone);
        } else if (sanPhamChiTiet.isTrangThai() == false) {
            respone.put("err", "Sản phẩm đã ngừng kinh doanh ");
            return ResponseEntity.badRequest().body(respone);
        } else if (soLuongUpdate > soLuongSanPhamHienCo && soLuongUpdate > soLuongDuocThemTiep) {
            respone.put("err", "Bạn đã có " + gioHangChiTiet.getSoLuong() + " sản phẩm này trong giỏ hàng, bạn chỉ có thể thêm tiếp được tối đa " + soLuongDuocThemTiep + " sản phẩm này");
            return ResponseEntity.badRequest().body(respone);
        } else {
            gioHangChiTiet.setSoLuong(soLuongUpdate);
            gioHangChiTiet.setThanhTien(BigDecimal.valueOf(soLuongUpdate * sanPhamChiTiet.getSanPham().getGia()));
            gioHangChiTietRepository.save(gioHangChiTiet);

            respone.put("success", "Cập nhật số lượng thành công");
            return ResponseEntity.ok().body(respone);
        }
    }
}

