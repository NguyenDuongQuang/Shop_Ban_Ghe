package com.example.backend.controller.customer.sanPham;

import com.example.backend.dto.GioHangDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customer/cart")
@CrossOrigin("*")
public class AddToCartController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestBody GioHangDTO dto) {

            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.getSanPhamChiTiet(dto.getSan_pham_id());
            if (sanPhamChiTiet.isTrangThai()) {

                KhachHang khachHang = khachHangRepository.findByEmail(dto.getEmail());
                SanPham sanPham = sanPhamRepository.findByID(dto.getSan_pham_id());

                // Fetch or create gioHang
                GioHang gioHang = gioHangRepository.findbyCustomerID(khachHang.getId());
                if (gioHang == null) {
                    gioHang = new GioHang();
                    gioHang.setKhachHang(khachHang);
                    gioHangRepository.save(gioHang);
                }

                Optional<GioHangChiTiet> optionalGioHangChiTiet =
                        gioHangChiTietRepository.checkGioHangChiTiet(sanPhamChiTiet.getId(), gioHang.getId());

                if (optionalGioHangChiTiet.isPresent()) {
                    GioHangChiTiet gioHangChiTiet = optionalGioHangChiTiet.get();
                    int soLuongDuocThemTiep = sanPhamChiTiet.getSoLuongTam() - gioHangChiTiet.getSoLuong();
                    int check = soLuongDuocThemTiep - dto.getSoLuong();

                    if (gioHangChiTiet.getSoLuong() == sanPhamChiTiet.getSoLuong()) {
                        Map<String, String> respone = new HashMap<>();
                        respone.put("err", "Bạn đã có " + sanPhamChiTiet.getSoLuong() +
                                " sản phẩm này trong giỏ hàng, bạn không thể thêm tiếp vì vượt quá số lượng của sản phẩm");
                        return ResponseEntity.badRequest().body(respone);
                    } else if (check < 0) {
                        Map<String, String> respone = new HashMap<>();
                        respone.put("err", "Bạn đã có " + gioHangChiTiet.getSoLuong() +
                                " sản phẩm này trong giỏ hàng, bạn chỉ có thể thêm tiếp được tối đa " + soLuongDuocThemTiep + " sản phẩm này");
                        return ResponseEntity.badRequest().body(respone);
                    } else {
                        int soLuongMoi = gioHangChiTiet.getSoLuong() + dto.getSoLuong();
                        float thanhTienMoi = sanPham.getGia() * soLuongMoi;
                        gioHangChiTiet.setSoLuong(soLuongMoi);
                        gioHangChiTiet.setThanhTien(BigDecimal.valueOf(thanhTienMoi));
                        gioHangChiTietRepository.save(gioHangChiTiet);
                    }
                } else {
                    float thanhTien = dto.getSoLuong() * sanPham.getGia();
                    GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
                    gioHangChiTiet.setGioHang(gioHang);
                    gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                    gioHangChiTiet.setDonGia(dto.getDonGia());
                    gioHangChiTiet.setSoLuong(dto.getSoLuong());
                    gioHangChiTiet.setThanhTien(BigDecimal.valueOf(thanhTien));
                    gioHangChiTietRepository.save(gioHangChiTiet);
                }

                Map<String, String> respone = new HashMap<>();
                respone.put("done", "Thêm sản phẩm vào giỏ hàng thành công");
                return ResponseEntity.ok().body(respone);
            } else {
                Map<String, String> respone = new HashMap<>();
                respone.put("err", "Chúng tôi đã ngừng kinh doanh sản phẩm này");
                return ResponseEntity.ok().body(respone);
            }
        }

}
