//package com.example.backend.validator;
//
//
//import org.springframework.http.ResponseEntity;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ProductValidate {
//
//    public enum ErrorCode {
//
//        Null_san_pham_id,
//
//        Null_soLuongDaChon,
//
//        Null_donGia,
//
//        Null_soLuong,
//
//        Null_tongTien,
//
//        Null_id_hoaDon,
//
//        Null_soLuongHienCo,
//
//        Null_tenSanPham,
//
//        Null_loai_san_pham,
//
//        Null_nha_san_xuat,
//
//        Null_chat_lieu,
//
//        Null_gia_ban,
//
//        Null_so_luong,
//
//        Null_mau_sac,
//
//        Null_kich_co,
//
//        Invalid_gia_ban,
//
//        Invalid_so_luong
//
//    }
//
//    //Mua ngay
//    public static ResponseEntity<?> checkOut(ProductDto sanPhamDTO) {
//        Map<String, String> errors = new HashMap<>();
//        checkSanPhamID(sanPhamDTO.getProduct_id(), errors);
//        checkSoLuongHienCo(sanPhamDTO.getQuantityHienCo(), errors);
//        checkSoLuongDaChon(sanPhamDTO.getQuantity(), sanPhamDTO.getQuantityHienCo(), errors);
//
//        if (errors.isEmpty()) {
//            return ResponseEntity.ok().build();
//        }
//
//        return ResponseEntity.badRequest().body(errors);
//
//    }
//
//    public static void checkSoLuongHienCo(Integer SoLuongHienCO, Map<String, String> errors) {
//        if (SoLuongHienCO == null) {
//            errors.put(ErrorCode.Null_soLuongHienCo.name(), "Không có số lượng hiện có");
//        }
//    }
//
////    public static void checkMaMauSac(String maMauSac, Map<String, String> errors) {
////        if (maMauSac == null || maMauSac.trim().isEmpty()) {
////            errors.put(ErrorCode.Null_maMauSac.name(), "Phải chọn màu sắc và kích cỡ");
////        }
////    }
//
////    public static void checkKichCoDaChon(String kichCoDaChon, Map<String, String> errors) {
////        if (kichCoDaChon == null || kichCoDaChon.trim().isEmpty()) {
////            errors.put(ErrorCode.Null_kichCoDaChon.name(), "Phải chọn màu sắc và kích cỡ");
////        }
////    }
//
//    public static void checkSoLuongDaChon(Integer soLuongDaChon, Integer soLuongDaCo, Map<String, String> errors) {
//        if (soLuongDaChon == null) {
//            errors.put(ErrorCode.Null_soLuongDaChon.name(), "Số lượng không được để trống");
//        }
//
//        if (soLuongDaChon <= 0) {
//            errors.put(ErrorCode.Null_soLuongDaChon.name(), "Số lượng phải lớn hơn 0");
//        }
//
//        if (soLuongDaCo != null && soLuongDaChon > soLuongDaCo) {
//            errors.put(ProductValidate.ErrorCode.Null_soLuongDaChon.name(), "Số lượng đã chọn không được lớn hơn số lượng đang có");
//        }
//    }
//
//    public static void checkDonGia(Integer donGia, Map<String, String> errors) {
//        if (donGia == null || donGia == 0) {
//            errors.put(ProductValidate.ErrorCode.Null_donGia.name(), "Đơn giá không được bỏ trống");
//        }
//    }
//
//    public static void checkTongTien(Integer tongTien, Map<String, String> errors) {
//        if (tongTien == null) {
//            errors.put(ProductValidate.ErrorCode.Null_tongTien.name(), "Tổng tiền không được bỏ trống");
//        }
//    }
//
//    public static void checkSanPhamID(Long sanPham_id, Map<String, String> errors) {
//        if (sanPham_id == null) {
//            errors.put(ProductValidate.ErrorCode.Null_san_pham_id.name(), "Không có sản phẩm nào được tìm thấy");
//        }
//    }
//
//    public static ResponseEntity<?> checkTaoSanPham(ProductDto sanPhamDTO) {
//        Map<String, String> errors = new HashMap<>();
//        checkTenSanPham(sanPhamDTO.getName_product(), errors);
//        checkGiaBan(sanPhamDTO.getPrice(), errors);
//        checkSoLuong(sanPhamDTO.getQuantity(), errors);
//        checkLoaiSanPham(sanPhamDTO.getCategory_id(), errors);
//
//        if (errors.isEmpty()) {
//            return ResponseEntity.ok().build();
//        }
//
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//
//    private static void checkSoLuong(Integer soLuong, Map<String, String> errors) {
//        if (soLuong == 0) {
//            errors.put(ProductValidate.ErrorCode.Null_so_luong.name(), "Bạn chưa nhập số lượng sản phẩm");
//        } else {
//            if (soLuong < 0) {
//                errors.put(ProductValidate.ErrorCode.Invalid_so_luong.name(), "Số lượng sản phẩm phải lớn hơn 0");
//            }
//        }
//    }
//
//    private static void checkGiaBan(Float gia, Map<String, String> errors) {
//        if (gia == null) {
//            errors.put("gia", "Bạn chưa nhập giá bán");
//        } else {
//            try {
//                Float giaFloat = gia;
//                if (giaFloat <= 0) {
//                    errors.put("gia", "Giá tiền phải lớn hơn 0");
//                }
//            } catch (NumberFormatException e) {
//                errors.put("gia", "Giá tiền không hợp lệ, vui lòng nhập số");
//            }
//        }
//    }
//
//
//    private static void checkLoaiSanPham(Long loaiSanPham_id, Map<String, String> errors) {
//        if (loaiSanPham_id == null) {
//            errors.put(ProductValidate.ErrorCode.Null_loai_san_pham.name(), "Vui lòng chọn loại sản phẩm");
//        }
//    }
//
//    private static void checkTenSanPham(String tenSanPham, Map<String, String> errors) {
//        if (tenSanPham == null || tenSanPham == "") {
//            errors.put(ProductValidate.ErrorCode.Null_tenSanPham.name(), "Bạn chưa nhập tên sản phẩm");
//        }
//    }
//
//}