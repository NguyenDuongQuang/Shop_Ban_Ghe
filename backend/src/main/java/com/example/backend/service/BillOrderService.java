package com.example.backend.service;

import com.example.backend.entity.HoaDon;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BillOrderService {
    List<HoaDon> findHoaDonByTrangThai(long trang_thai_id);

    ResponseEntity<?> updateTrangThai(long trang_thai_id, long id_bill);


    List<HoaDon> searchAllBill(long trang_thai_id, String search);

    List<HoaDon> searchDateBill(long trang_thai_id, String searchDate);

    ResponseEntity<Map<String, Boolean>> capNhatTrangThaiHuyDon(long trang_thai_id, long id_bill, String ghiChu);

    ResponseEntity<?> CTChoXacNhan(long id_hoa_don);

    ResponseEntity<?> CTChoGiaoHang(long id_hoa_don);

    ResponseEntity<?> CTDangGiaoHang(long id_hoa_don);

    ResponseEntity<?> CTDaGiaoHang(long id_hoa_don);

    ResponseEntity<?> CTDaHuy(long id_hoa_don);
}