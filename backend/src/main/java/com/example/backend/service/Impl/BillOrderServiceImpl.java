package com.example.backend.service.Impl;

import com.example.backend.dto.HoaDonChiTietDTO;
import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.BillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class BillOrderServiceImpl implements BillOrderService {

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    TrangThaiRepository trangThaiRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    ImageRepository hinhAnhRepository;

    @Autowired
    MailService  mailService;
    @Override
    public List<HoaDon> findHoaDonByTrangThai(long trang_thai_id) {
        List<HoaDon> hoaDonList = hoaDonRepository.findHoaDonByTrangThai(trang_thai_id);
        return hoaDonList;
    }
    @Transactional
    @Override
    public ResponseEntity<?> updateTrangThai(long trang_thai_id, long id_bill) {
        Map<String, String> respone = new HashMap<>();
        HoaDon hoaDon = hoaDonRepository.findByID(id_bill);
        Optional<TrangThai> optionalTrangThai = trangThaiRepository.findById(trang_thai_id);
        if (optionalTrangThai.isPresent()) {
            if (trang_thai_id == 2) {
                List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                    SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findByID(hoaDonChiTiet.getSanPhamChiTiet().getId());
                    if (hoaDonChiTiet.getSanPhamChiTiet().getId() == hoaDonChiTiet.getSanPhamChiTiet().getId()) {
                        int soLuong = sanPhamChiTiet.getSoLuong();
                        int soLuongSPHoaDon = hoaDonChiTiet.getSoLuong();
                        int soLuongUpdate = soLuong - soLuongSPHoaDon;


                        if (hoaDon.getLoaiHoaDon() == 1) {
                            TrangThai trangThai = optionalTrangThai.get();
                            hoaDon.setTrangThai(trangThai);
                            hoaDonRepository.save(hoaDon);
                            List<HoaDon> result = findHoaDonByTrangThai(1);
                            respone.put("success", result.toString());
                            return ResponseEntity.ok().body(respone);
                        } else if (hoaDon.getLoaiHoaDon() == 0) {
                            if (soLuongUpdate < 0) {
                                respone.put("err", "Số lượng cửa sản phẩm " + sanPhamChiTiet.getSanPham().getTenSanPham() + " không đủ để giao hàng, vui lòng thử lại sau");
                                return ResponseEntity.badRequest().body(respone);
                            }else {
                                sanPhamChiTiet.setSoLuong(soLuongUpdate);
                                sanPhamChiTiet.setSoLuongTam(soLuongUpdate);
                                if (soLuongUpdate >= 0) {
                                    hoaDonChiTietRepository.deleteByID(6);

                                    List<GioHangChiTiet> ghct = gioHangChiTietRepository.findCartBySPCTID(sanPhamChiTiet.getId());
                                    for (GioHangChiTiet gioHangChiTiet1 : ghct) {
                                        gioHangChiTietRepository.deleteById(gioHangChiTiet1.getId());
                                    }
                                    sanPhamChiTiet.setTrangThai(false);

                                    TrangThai trangThai = optionalTrangThai.get();
                                    hoaDon.setTrangThai(trangThai);
                                    hoaDonRepository.save(hoaDon);
                                    sanPhamChiTiet.setTrangThai(true);

                                    List<HoaDon> result = findHoaDonByTrangThai(1);
                                    respone.put("success", result.toString());
                                    return ResponseEntity.ok().body(respone);
                                }
                                sanPhamChiTietRepository.save(sanPhamChiTiet);
                            }
                        }
                    }
                }
            }
        }

        TrangThai trangThai = optionalTrangThai.get();
        hoaDon.setTrangThai(trangThai);
        hoaDonRepository.save(hoaDon);
        List<HoaDon> result = findHoaDonByTrangThai(1);
        respone.put("success", result.toString());
        return ResponseEntity.ok().body(respone);
    }
    @Override
    public List<HoaDon> searchAllBill(long trang_thai_id, String search) {
        List<HoaDon> hoaDonList = hoaDonRepository.findBill(trang_thai_id, search);
        return hoaDonList;
    }

    @Override
    public List<HoaDon> searchDateBill(long trang_thai_id, String searchDate) {
        LocalDate search = LocalDate.parse(searchDate);
        List<HoaDon> hoaDonList = hoaDonRepository.findBillByDate(trang_thai_id, search);
        return hoaDonList;
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> capNhatTrangThaiHuyDon(long trang_thai_id, long id_bill, String
            ghiChu) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_bill);
        Optional<TrangThai> optionalTrangThai = trangThaiRepository.findById(trang_thai_id);
        if (optionalTrangThai.isPresent()) {
            List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());

            if (hoaDon.getTrangThai().getId() == 3) {
                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                    SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
                    sanPhamChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + sanPhamChiTiet.getSoLuong());
                    sanPhamChiTiet.setTrangThai(true);
                    sanPhamChiTietRepository.save(sanPhamChiTiet);

                    TrangThai trangThai = optionalTrangThai.get();
                    hoaDon.setTrangThai(trangThai);
                    hoaDon.setGhiChu(ghiChu);
                    hoaDonRepository.save(hoaDon);
                }
                TrangThai trangThai = optionalTrangThai.get();
                hoaDon.setTrangThai(trangThai);
                hoaDon.setGhiChu(ghiChu);
                hoaDonRepository.save(hoaDon);

            } else if (hoaDon.getTrangThai().getId() == 1) {
                if (hoaDon.getLoaiHoaDon() == 0) {
                    TrangThai trangThai = optionalTrangThai.get();
                    hoaDon.setTrangThai(trangThai);
                    hoaDon.setGhiChu(ghiChu);
                    hoaDonRepository.save(hoaDon);
                } else if (hoaDon.getLoaiHoaDon() == 1) {
                    for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
                        sanPhamChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + sanPhamChiTiet.getSoLuong());
                        sanPhamChiTiet.setTrangThai(true);
                        sanPhamChiTietRepository.save(sanPhamChiTiet);

                        TrangThai trangThai = optionalTrangThai.get();
                        hoaDon.setTrangThai(trangThai);
                        hoaDon.setGhiChu(ghiChu);
                        hoaDonRepository.save(hoaDon);
                    }
                }
                TrangThai trangThai = optionalTrangThai.get();
                hoaDon.setTrangThai(trangThai);
                hoaDon.setGhiChu(ghiChu);
                hoaDonRepository.save(hoaDon);
            }
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> CTChoXacNhan(long id_hoa_don) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_hoa_don);
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        List<HoaDonChiTietDTO> dto = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            HoaDon hoaDon2 = hoaDonChiTiet.getHoaDon();
            String anh_san_pham = hinhAnhRepository.getAnhSPByMauSacAndSPID(sanPhamChiTiet.getSanPham().getId());

            HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
            hoaDonChiTietDTO.setId(hoaDonChiTiet.getId());
            hoaDonChiTietDTO.setIdProduct(sanPhamChiTiet.getSanPham().getId());
            hoaDonChiTietDTO.setSoLuong(hoaDonChiTiet.getSoLuong());
            hoaDonChiTietDTO.setDonGia(hoaDonChiTiet.getDonGia());
            hoaDonChiTietDTO.setThanhTien(hoaDonChiTiet.getThanhTien());
            hoaDonChiTietDTO.setHoaDon(hoaDon2);
            hoaDonChiTietDTO.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTietDTO.setAnhSanPham(anh_san_pham);

            dto.add(hoaDonChiTietDTO);
        }


        Map<String, Object> response = new HashMap<>();
        response.put("list_HDCT", dto);
        response.put("hoaDon", hoaDon);

        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<?> CTChoGiaoHang(long id_hoa_don) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_hoa_don);
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        List<HoaDonChiTietDTO> dto = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            HoaDon hoaDon2 = hoaDonChiTiet.getHoaDon();
            String anh_san_pham = hinhAnhRepository.getAnhSPByMauSacAndSPID(sanPhamChiTiet.getSanPham().getId());

            HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
            hoaDonChiTietDTO.setId(hoaDonChiTiet.getId());
            hoaDonChiTietDTO.setIdProduct(sanPhamChiTiet.getSanPham().getId());
            hoaDonChiTietDTO.setSoLuong(hoaDonChiTiet.getSoLuong());
            hoaDonChiTietDTO.setDonGia(hoaDonChiTiet.getDonGia());
            hoaDonChiTietDTO.setThanhTien(hoaDonChiTiet.getThanhTien());
            hoaDonChiTietDTO.setHoaDon(hoaDon2);
            hoaDonChiTietDTO.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTietDTO.setAnhSanPham(anh_san_pham);

            dto.add(hoaDonChiTietDTO);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("list_HDCT", dto);
        response.put("hoaDon", hoaDon);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<?> CTDangGiaoHang(long id_hoa_don) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_hoa_don);
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        List<HoaDonChiTietDTO> dto = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            HoaDon hoaDon2 = hoaDonChiTiet.getHoaDon();
            String anh_san_pham = hinhAnhRepository.getAnhSPByMauSacAndSPID(sanPhamChiTiet.getSanPham().getId());

            HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
            hoaDonChiTietDTO.setId(hoaDonChiTiet.getId());
            hoaDonChiTietDTO.setIdProduct(sanPhamChiTiet.getSanPham().getId());
            hoaDonChiTietDTO.setSoLuong(hoaDonChiTiet.getSoLuong());
            hoaDonChiTietDTO.setDonGia(hoaDonChiTiet.getDonGia());
            hoaDonChiTietDTO.setThanhTien(hoaDonChiTiet.getThanhTien());
            hoaDonChiTietDTO.setHoaDon(hoaDon2);
            hoaDonChiTietDTO.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTietDTO.setAnhSanPham(anh_san_pham);

            dto.add(hoaDonChiTietDTO);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("list_HDCT", dto);
        response.put("hoaDon", hoaDon);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<?> CTDaGiaoHang(long id_hoa_don) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_hoa_don);
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        List<HoaDonChiTietDTO> dto = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            HoaDon hoaDon2 = hoaDonChiTiet.getHoaDon();
            String anh_san_pham = hinhAnhRepository.getAnhSPByMauSacAndSPID(sanPhamChiTiet.getSanPham().getId());

            HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
            hoaDonChiTietDTO.setId(hoaDonChiTiet.getId());
            hoaDonChiTietDTO.setIdProduct(sanPhamChiTiet.getSanPham().getId());

            hoaDonChiTietDTO.setSoLuong(hoaDonChiTiet.getSoLuong());
            hoaDonChiTietDTO.setDonGia(hoaDonChiTiet.getDonGia());
            hoaDonChiTietDTO.setThanhTien(hoaDonChiTiet.getThanhTien());
            hoaDonChiTietDTO.setHoaDon(hoaDon2);
            hoaDonChiTietDTO.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTietDTO.setAnhSanPham(anh_san_pham);

            dto.add(hoaDonChiTietDTO);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("list_HDCT", dto);
        response.put("hoaDon", hoaDon);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<?> CTDaHuy(long id_hoa_don) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_hoa_don);
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        List<HoaDonChiTietDTO> dto = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            HoaDon hoaDon2 = hoaDonChiTiet.getHoaDon();
           String anh_san_pham = hinhAnhRepository.getAnhSPByMauSacAndSPID(sanPhamChiTiet.getSanPham().getId());

            HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
            hoaDonChiTietDTO.setId(hoaDonChiTiet.getId());
            hoaDonChiTietDTO.setIdProduct(sanPhamChiTiet.getSanPham().getId());
            hoaDonChiTietDTO.setSoLuong(hoaDonChiTiet.getSoLuong());
            hoaDonChiTietDTO.setDonGia(hoaDonChiTiet.getDonGia());
            hoaDonChiTietDTO.setThanhTien(hoaDonChiTiet.getThanhTien());
            hoaDonChiTietDTO.setHoaDon(hoaDon2);
            hoaDonChiTietDTO.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTietDTO.setAnhSanPham(anh_san_pham);

            dto.add(hoaDonChiTietDTO);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("list_HDCT", dto);
        response.put("hoaDon", hoaDon);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<?> CTXacNhanDaGiao(long id_hoa_don) {
        HoaDon hoaDon = hoaDonRepository.findByID(id_hoa_don);
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
        List<HoaDonChiTietDTO> dto = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            HoaDon hoaDon2 = hoaDonChiTiet.getHoaDon();
            String anh_san_pham = hinhAnhRepository.getAnhSPByMauSacAndSPID(sanPhamChiTiet.getSanPham().getId());

            HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
            hoaDonChiTietDTO.setId(hoaDonChiTiet.getId());
            hoaDonChiTietDTO.setIdProduct(sanPhamChiTiet.getSanPham().getId());
            hoaDonChiTietDTO.setSoLuong(hoaDonChiTiet.getSoLuong());
            hoaDonChiTietDTO.setDonGia(hoaDonChiTiet.getDonGia());
            hoaDonChiTietDTO.setThanhTien(hoaDonChiTiet.getThanhTien());
            hoaDonChiTietDTO.setHoaDon(hoaDon2);
            hoaDonChiTietDTO.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTietDTO.setAnhSanPham(anh_san_pham);

            dto.add(hoaDonChiTietDTO);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("list_HDCT", dto);
        response.put("hoaDon", hoaDon);
        return ResponseEntity.ok().body(response);
    }
    @Override
    public List<HoaDon> findHoaDonByLoai(int loai_hoa_don) {
        List<HoaDon> hoaDonList = hoaDonRepository.findHoaDonByLoai(loai_hoa_don);
        return hoaDonList;
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> capNhatTrangThai_TatCa(long trang_thai_id,
                                                                       long trang_thai_id_sau, String thaoTac, String nguoiThaoTac) {
        List<HoaDon> list = hoaDonRepository.findHoaDonByTrangThai(trang_thai_id);
        for (HoaDon hoaDon : list) {
            Optional<TrangThai> optionalTrangThai = trangThaiRepository.findById(trang_thai_id_sau);
            if (optionalTrangThai.isPresent()) {
                TrangThai trangThai = optionalTrangThai.get();
                hoaDon.setTrangThai(trangThai);
                hoaDonRepository.save(hoaDon);
                try {
                    mailService.guiMailKhiThaoTac(hoaDon.getEmailNguoiNhan(), hoaDon);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public List<HoaDon> capNhatTrangThai_DaChon(HoaDonDTO hoaDonDTO, long trang_thai_id, String thaoTac, String
            nguoiThaoTac) {
        for (Long id_hoaDon : hoaDonDTO.getId_hoaDon()) {
            HoaDon hoaDon = hoaDonRepository.findByID(id_hoaDon);
            Optional<TrangThai> optionalTrangThai = trangThaiRepository.findById(trang_thai_id);
            if (optionalTrangThai.isPresent()) {
                TrangThai trangThai = optionalTrangThai.get();
                hoaDon.setTrangThai(trangThai);
                hoaDonRepository.save(hoaDon);
            }
        }
        List<HoaDon> hoaDonList = hoaDonRepository.findHoaDonByTrangThai(trang_thai_id - 1);
        return hoaDonList;
    }

    @Override
    public List<HoaDon> capNhatTrangThaiHuy_DaChon(HoaDonDTO hoaDonDTO, String nguoiThaoTac, String ghiChu) {
        for (Long id_hoaDon : hoaDonDTO.getId_hoaDon()) {
            HoaDon hoaDon = hoaDonRepository.findByID(id_hoaDon);
            Optional<TrangThai> optionalTrangThai = trangThaiRepository.findById(5L);
            if (optionalTrangThai.isPresent()) {
                TrangThai trangThai = optionalTrangThai.get();
                hoaDon.setTrangThai(trangThai);
                hoaDon.setGhiChu(ghiChu);
                hoaDonRepository.save(hoaDon);

                List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDon.getId());
                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                    SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
                    sanPhamChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + sanPhamChiTiet.getSoLuong());
                    sanPhamChiTiet.setSoLuongTam(hoaDonChiTiet.getSoLuong() + sanPhamChiTiet.getSoLuong());
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }
            }
        }
        List<HoaDon> hoaDonList = hoaDonRepository.findHoaDonByTrangThai(1L);
        return hoaDonList;
    }
}
