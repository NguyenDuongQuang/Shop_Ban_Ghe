package com.example.backend.service.Impl;

import com.example.backend.dto.HoaDonDTO;
import com.example.backend.entity.HoaDon;
import com.example.backend.entity.HoaDonChiTiet;
import com.example.backend.repository.HoaDonChiTietRepository;
import com.example.backend.repository.HoaDonRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class InHoaDonService {
    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    public ResponseEntity<byte[]> generatePdf(HoaDonDTO hoaDonDTO) {
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDonDTO.getId());
        Optional<HoaDon> optHoaDon = hoaDonRepository.findById(hoaDonDTO.getId());
        if (optHoaDon.isPresent()) {
            HoaDon hoaDon = optHoaDon.get();
            StringBuilder htmlContentBuilder = new StringBuilder();
            htmlContentBuilder.append("<html><head>");
            htmlContentBuilder.append("<meta charset=\"UTF-8\">");
            htmlContentBuilder.append("<title>Hóa đơn</title>");
            htmlContentBuilder.append("<style>");
            htmlContentBuilder.append("body {\n" +
                    "    font-family: Arial, sans-serif;\n" +
                    "    line-height: 1.6;\n" +
                    "    background-color: #f9f9f9;\n" +
                    "    padding: 20px;\n" +
                    "}\n" +
                    "\n" +
                    "h1 {\n" +
                    "    color: #338dbc;" +
                    "    text-align: center;\n" +
                    "    font-size: 24px;\n" +
                    "    margin-bottom: 10px;\n" +
                    "}\n" +
                    "\n" +
                    "p {\n" +
                    "    margin-bottom: 10px;\n" +
                    "    color: #333;" +
                    "}\n" +
                    "\n" +
                    "h3 {\n" +
                    "    margin-bottom: 10px;\n" +
                    "    color: #333;\n" +
                    "    text-align: center;" +
                    "}\n" +
                    "\n" +
                    "table {\n" +
                    "    width: 100%;\n" +
                    "    border-collapse: collapse;\n" +
                    "    margin-top: 20px;\n" +
                    "    margin-bottom: 30px;\n" +
                    "}\n" +
                    "\n" +
                    "th, td {\n" +
                    "    padding: 12px 15px;\n" +
                    "    border-bottom: 1px solid #ddd;\n" +
                    "}\n" +
                    "\n" +
                    "th {\n" +
                    "    background-color: #f2f2f2;\n" +
                    "}\n" +
                    "\n" +
                    "tr:hover {\n" +
                    "    background-color: #f5f5f5;\n" +
                    "}\n" +
                    "\n" +
                    "h1.order-details-title {\n" +
                    "    margin-top: 40px;\n" +
                    "}\n" +
                    "\n" +
                    "p.footer-text {\n" +
                    "    margin-top: 30px;\n" +
                    "    text-align: center;\n" +
                    "    color: #888;\n" +
                    "}\n" +
                    "\n" +
                    ".container {\n" +
                    "    max-width: 600px;\n" +
                    "    margin: 0 auto;\n" +
                    "}\n" +
                    "\n" +
                    ".header {\n" +
                    "    text-align: center;\n" +
                    "    margin-bottom: 30px;\n" +
                    "}\n" +
                    "\n" +
                    ".footer {\n" +
                    "    text-align: center;\n" +
                    "    margin-top: 50px;\n" +
                    "    padding-top: 20px;\n" +
                    "    border-top: 1px solid #ddd;\n" +
                    "    color: #888;\n" +
                    "}\n" +
                    "\n" +
                    ".logo {\n" +
                    "    width: 100px;\n" +
                    "    height: auto;\n" +
                    "}\n" +
                    "\n" +
                    ".product-table {\n" +
                    "    border: 1px solid #ddd;\n" +
                    "}\n" +
                    "\n" +
                    ".product-table th, .product-table td {\n" +
                    "    text-align: left;\n" +
                    "}\n" +
                    "\n" +
                    ".total-amount {\n" +
                    "    font-weight: bold;\n" +
                    "}\n" +
                    "\n" +
                    "/* Add more styles as needed */\n");
            htmlContentBuilder.append("</style>");
            htmlContentBuilder.append("<body>");

            //Các nội dung của html
            htmlContentBuilder.append("<h1>").append("SHOP BÁN GHẾ NGUYỄN DƯƠNG QUANG").append("</h1>");

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            // Thêm thông tin đơn hàng
            Date ngayTao = hoaDon.getCreatedDate();
            String formattedTienGiam;

            // kiem tra thong tin khach hang
            String khach;
            String soDienthoai;
            if(hoaDon.getNguoiNhan()==null){
                khach = "";
                soDienthoai= "";
            }else{
                khach = hoaDon.getNguoiNhan();
                soDienthoai = hoaDon.getSDTNguoiNhan();
            }

            DecimalFormat currencyFormatter = new DecimalFormat("###,### ₫", new DecimalFormatSymbols(Locale.getDefault()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedNgayTao = dateFormat.format(ngayTao);

            htmlContentBuilder.append("<h3>").append("Thông tin đơn hàng").append("</h1>");
            htmlContentBuilder.append("<p>Mã đơn hàng: ").append(hoaDon.getMaHoaDon()).append("</p>");
            htmlContentBuilder.append("<p>Ngày mua: ").append(formattedNgayTao).append("</p>");
            htmlContentBuilder.append("<p>Khách hàng: ").append(khach).append("</p>");
            htmlContentBuilder.append("<p>Số điện thoại khách hàng: ").append(soDienthoai).append("</p>");
            htmlContentBuilder.append("<p>Tiền khách đưa: ").append(currencyFormatter.format(hoaDonDTO.getTienKhachDua())).append("</p>");
            htmlContentBuilder.append("<p>Tiền trả lại: ").append(currencyFormatter.format(hoaDonDTO.getTienTraLai())).append("</p>");
            htmlContentBuilder.append("<p>Trạng thái đơn: Đã thanh toán</p>");
            htmlContentBuilder.append("<p>Nhân viên bán hàng: ").append(hoaDon.getNhanVien().getHoTen()).append("</p>");


            String formattedTongTienDonHang = numberFormat.format(hoaDon.getTongTienDonHang());
            String formattedTongTienHoaDon = numberFormat.format(hoaDon.getTongTienHoaDon());
            // Thêm chi tiết đơn hàng
            htmlContentBuilder.append("<h3>").append("Chi tiết đơn hàng").append("</h1>");
            htmlContentBuilder.append("<table>");
            htmlContentBuilder.append("<tr><th>Sản phẩm</th><th>Số lượng</th><th>Thành tiền</th></tr>");
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                NumberFormat fomatTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String fomatTienSanPham = fomatTien.format(hoaDonChiTiet.getThanhTien());
                htmlContentBuilder.append("<tr>");
                htmlContentBuilder.append("<td>").append(hoaDonChiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham())
//                        .append(" (").append(hoaDonChiTiet.getSanPhamChiTiet().getKichCo().getKichCo())
//                        .append("/").append(hoaDonChiTiet.getSanPhamChiTiet().getMauSac().getTenMauSac()).append(")")
                        .append("</td>");

                htmlContentBuilder.append("<td>").append(hoaDonChiTiet.getSoLuong()).append("</td>");
                htmlContentBuilder.append("<td>").append(fomatTienSanPham).append("</td>");
                htmlContentBuilder.append("</tr>");
            }
            htmlContentBuilder.append("</table>");

            // Thêm tổng tiền và các thông tin khác của hóa đơn nếu cần
            htmlContentBuilder.append("<p>Tổng giá trị đơn hàng: ").append(formattedTongTienDonHang).append("</p>");
//            htmlContentBuilder.append("<p>Tiền giảm: ").append(formattedTienGiam).append("</p>");
            htmlContentBuilder.append("<p>Tổng tiền thanh toán: ").append(formattedTongTienHoaDon).append("</p>");

            htmlContentBuilder.append("<h3>Xin chân thành cảm ơn sự ủng hộ của bạn dành cho ADUDAS STUDIO!</h3>");
            htmlContentBuilder.append("</body></html>");

            // Gọi phương thức tạo file PDF từ nội dung HTML, sử dụng thư viện iText
            byte[] pdfBytes = createPdfFromHtml(htmlContentBuilder);

            // Lưu file PDF vào thư mục dự án
            String filePath = "C:\\Users\\Quang\\Documents\\GitHub\\Shop_Ban_Ghe\\backend\\hoa_don_" + hoaDonDTO.getId() + ".pdf";
            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                fileOutputStream.write(pdfBytes);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi nếu cần thiết
            }

            // Thiết lập thông tin phản hồi
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "hoa_don.pdf");

            // Trả về file PDF dưới dạng byte[]
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<byte[]> HdDaThanhToanPdf(HoaDonDTO hoaDonDTO) {
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDonDTO.getId());
        Optional<HoaDon> optHoaDon = hoaDonRepository.findById(hoaDonDTO.getId());
        if (optHoaDon.isPresent()) {
            HoaDon hoaDon = optHoaDon.get();
            StringBuilder htmlContentBuilder = new StringBuilder();
            htmlContentBuilder.append("<html><head>");
            htmlContentBuilder.append("<meta charset=\"UTF-8\">");
            htmlContentBuilder.append("<title>Hóa đơn</title>");
            htmlContentBuilder.append("<style>");
            htmlContentBuilder.append("body {\n" +
                    "    font-family: Arial, sans-serif;\n" +
                    "    line-height: 1.6;\n" +
                    "    background-color: #f9f9f9;\n" +
                    "    padding: 20px;\n" +
                    "}\n" +
                    "\n" +
                    "h1 {\n" +
                    "    color: #338dbc;" +
                    "    text-align: center;\n" +
                    "    font-size: 24px;\n" +
                    "    margin-bottom: 10px;\n" +
                    "}\n" +
                    "\n" +
                    "p {\n" +
                    "    margin-bottom: 10px;\n" +
                    "    color: #333;" +
                    "}\n" +
                    "\n" +
                    "h3 {\n" +
                    "    margin-bottom: 10px;\n" +
                    "    color: #333;\n" +
                    "    text-align: center;" +
                    "}\n" +
                    "\n" +
                    "table {\n" +
                    "    width: 100%;\n" +
                    "    border-collapse: collapse;\n" +
                    "    margin-top: 20px;\n" +
                    "    margin-bottom: 30px;\n" +
                    "}\n" +
                    "\n" +
                    "th, td {\n" +
                    "    padding: 12px 15px;\n" +
                    "    border-bottom: 1px solid #ddd;\n" +
                    "}\n" +
                    "\n" +
                    "th {\n" +
                    "    background-color: #f2f2f2;\n" +
                    "}\n" +
                    "\n" +
                    "tr:hover {\n" +
                    "    background-color: #f5f5f5;\n" +
                    "}\n" +
                    "\n" +
                    "h1.order-details-title {\n" +
                    "    margin-top: 40px;\n" +
                    "}\n" +
                    "\n" +
                    "p.footer-text {\n" +
                    "    margin-top: 30px;\n" +
                    "    text-align: center;\n" +
                    "    color: #888;\n" +
                    "}\n" +
                    "\n" +
                    ".container {\n" +
                    "    max-width: 600px;\n" +
                    "    margin: 0 auto;\n" +
                    "}\n" +
                    "\n" +
                    ".header {\n" +
                    "    text-align: center;\n" +
                    "    margin-bottom: 30px;\n" +
                    "}\n" +
                    "\n" +
                    ".footer {\n" +
                    "    text-align: center;\n" +
                    "    margin-top: 50px;\n" +
                    "    padding-top: 20px;\n" +
                    "    border-top: 1px solid #ddd;\n" +
                    "    color: #888;\n" +
                    "}\n" +
                    "\n" +
                    ".logo {\n" +
                    "    width: 100px;\n" +
                    "    height: auto;\n" +
                    "}\n" +
                    "\n" +
                    ".product-table {\n" +
                    "    border: 1px solid #ddd;\n" +
                    "}\n" +
                    "\n" +
                    ".product-table th, .product-table td {\n" +
                    "    text-align: left;\n" +
                    "}\n" +
                    "\n" +
                    ".total-amount {\n" +
                    "    font-weight: bold;\n" +
                    "}\n" +
                    "\n" +
                    "/* Add more styles as needed */\n");
            htmlContentBuilder.append("</style>");
            htmlContentBuilder.append("<body>");

            //Các nội dung của html
            htmlContentBuilder.append("<h1>").append("SHOP BÁN GHẾ NGUYỄN DƯƠNG QUANG").append("</h1>");

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            // Thêm thông tin đơn hàng
            Date ngayTao = hoaDon.getCreatedDate();
            String formattedTienGiam;
//
            // kiem tra thong tin khach hang
            String khach;
            String soDienthoai;
            if(hoaDon.getNguoiNhan()==null){
                khach = "";
                soDienthoai= "";
            }else{
                khach = hoaDon.getNguoiNhan();
                soDienthoai = hoaDon.getSDTNguoiNhan();
            }

            DecimalFormat currencyFormatter = new DecimalFormat("###,### ₫", new DecimalFormatSymbols(Locale.getDefault()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedNgayTao = dateFormat.format(ngayTao);

            htmlContentBuilder.append("<h3>").append("Thông tin đơn hàng").append("</h1>");
            htmlContentBuilder.append("<p>Mã đơn hàng: ").append(hoaDon.getMaHoaDon()).append("</p>");
            htmlContentBuilder.append("<p>Ngày mua: ").append(formattedNgayTao).append("</p>");
            htmlContentBuilder.append("<p>Khách hàng: ").append(khach).append("</p>");
            htmlContentBuilder.append("<p>Số điện thoại khách hàng: ").append(soDienthoai).append("</p>");
            htmlContentBuilder.append("<p>Trạng thái đơn: Đã thanh toán</p>");
            htmlContentBuilder.append("<p>Nhân viên bán hàng: ").append(hoaDon.getNhanVien().getHoTen()).append("</p>");


            String formattedTongTienDonHang = numberFormat.format(hoaDon.getTongTienDonHang());
            String formattedTongTienHoaDon = numberFormat.format(hoaDon.getTongTienHoaDon());
            // Thêm chi tiết đơn hàng
            htmlContentBuilder.append("<h3>").append("Chi tiết đơn hàng").append("</h1>");
            htmlContentBuilder.append("<table>");
            htmlContentBuilder.append("<tr><th>Sản phẩm</th><th>Số lượng</th><th>Thành tiền</th></tr>");
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                NumberFormat fomatTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String fomatTienSanPham = fomatTien.format(hoaDonChiTiet.getThanhTien());
                htmlContentBuilder.append("<tr>");
                htmlContentBuilder.append("<td>").append(hoaDonChiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham())
//                        .append(" (").append(hoaDonChiTiet.getSanPhamChiTiet().getKichCo().getKichCo())
//                        .append("/").append(hoaDonChiTiet.getSanPhamChiTiet().getMauSac().getTenMauSac()).append(")")
                        .append("</td>");

                htmlContentBuilder.append("<td>").append(hoaDonChiTiet.getSoLuong()).append("</td>");
                htmlContentBuilder.append("<td>").append(fomatTienSanPham).append("</td>");
                htmlContentBuilder.append("</tr>");
            }
            htmlContentBuilder.append("</table>");

            // Thêm tổng tiền và các thông tin khác của hóa đơn nếu cần
            htmlContentBuilder.append("<p>Tổng giá trị đơn hàng: ").append(formattedTongTienDonHang).append("</p>");
//            htmlContentBuilder.append("<p>Tiền giảm: ").append(formattedTienGiam).append("</p>");
            htmlContentBuilder.append("<p>Tổng tiền thanh toán: ").append(formattedTongTienHoaDon).append("</p>");

            htmlContentBuilder.append("<h3>Xin chân thành cảm ơn sự ủng hộ của bạn dành cho ADUDAS STUDIO!</h3>");
            htmlContentBuilder.append("</body></html>");

            // Gọi phương thức tạo file PDF từ nội dung HTML, sử dụng thư viện iText
            byte[] pdfBytes = createPdfFromHtml(htmlContentBuilder);

            // Lưu file PDF vào thư mục dự án
            String filePath = "C:\\Users\\Quang\\Documents\\GitHub\\Shop_Ban_Ghe\\backend\\hoa_don_" + hoaDonDTO.getId() + ".pdf";
            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                fileOutputStream.write(pdfBytes);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi nếu cần thiết
            }

            // Thiết lập thông tin phản hồi
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "hoa_don.pdf");

            // Trả về file PDF dưới dạng byte[]
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<byte[]> hoaDonDatHangPdf(Long hoaDonId) {
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByIDBill(hoaDonId);
        Optional<HoaDon> optHoaDon = hoaDonRepository.findById(hoaDonId);
        String tinhTrangThanhToan= "";
        if (optHoaDon.isPresent()) {
            HoaDon hoaDon = optHoaDon.get();
            // Tạo nội dung HTML cho hóa đơn (thay đổi cho phù hợp với mẫu HTML của bạn)
            StringBuilder htmlContentBuilder = new StringBuilder();
            htmlContentBuilder.append("<html><head>");
            htmlContentBuilder.append("<meta charset=\"UTF-8\">");
            htmlContentBuilder.append("<title>Hóa đơn</title>");
            htmlContentBuilder.append("<style>");
            htmlContentBuilder.append("body {\n" +
                    "    font-family: Arial, sans-serif;\n" +
                    "    line-height: 1.6;\n" +
                    "    background-color: #f9f9f9;\n" +
                    "    padding: 20px;\n" +
                    "}\n" +
                    "\n" +
                    "h1 {\n" +
                    "    color: #338dbc;" +
                    "    text-align: center;\n" +
                    "    font-size: 24px;\n" +
                    "    margin-bottom: 10px;\n" +
                    "}\n" +
                    "\n" +
                    "p {\n" +
                    "    margin-bottom: 10px;\n" +
                    "    color: #333;" +
                    "}\n" +
                    "\n" +
                    "h3 {\n" +
                    "    margin-bottom: 10px;\n" +
                    "    color: #333;\n" +
                    "    text-align: center;" +
                    "}\n" +
                    "\n" +
                    "table {\n" +
                    "    width: 100%;\n" +
                    "    border-collapse: collapse;\n" +
                    "    margin-top: 20px;\n" +
                    "    margin-bottom: 30px;\n" +
                    "}\n" +
                    "\n" +
                    "th, td {\n" +
                    "    padding: 12px 15px;\n" +
                    "    border-bottom: 1px solid #ddd;\n" +
                    "}\n" +
                    "\n" +
                    "th {\n" +
                    "    background-color: #f2f2f2;\n" +
                    "}\n" +
                    "\n" +
                    "tr:hover {\n" +
                    "    background-color: #f5f5f5;\n" +
                    "}\n" +
                    "\n" +
                    "h1.order-details-title {\n" +
                    "    margin-top: 40px;\n" +
                    "}\n" +
                    "\n" +
                    "p.footer-text {\n" +
                    "    margin-top: 30px;\n" +
                    "    text-align: center;\n" +
                    "    color: #888;\n" +
                    "}\n" +
                    "\n" +
                    ".container {\n" +
                    "    max-width: 600px;\n" +
                    "    margin: 0 auto;\n" +
                    "}\n" +
                    "\n" +
                    ".header {\n" +
                    "    text-align: center;\n" +
                    "    margin-bottom: 30px;\n" +
                    "}\n" +
                    "\n" +
                    ".footer {\n" +
                    "    text-align: center;\n" +
                    "    margin-top: 50px;\n" +
                    "    padding-top: 20px;\n" +
                    "    border-top: 1px solid #ddd;\n" +
                    "    color: #888;\n" +
                    "}\n" +
                    "\n" +
                    ".logo {\n" +
                    "    width: 100px;\n" +
                    "    height: auto;\n" +
                    "}\n" +
                    "\n" +
                    ".product-table {\n" +
                    "    border: 1px solid #ddd;\n" +
                    "}\n" +
                    "\n" +
                    ".product-table th, .product-table td {\n" +
                    "    text-align: left;\n" +
                    "}\n" +
                    "\n" +
                    ".total-amount {\n" +
                    "    font-weight: bold;\n" +
                    "}\n" +
                    "\n" +
                    "/* Add more styles as needed */\n");
            htmlContentBuilder.append("</style>");
            htmlContentBuilder.append("<body>");

            //Các nội dung của html
            htmlContentBuilder.append("<h1>").append("SHOP BÁN GHẾ NGUYỄN DƯƠNG QUANG").append("</h1>");

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            // Thêm thông tin đơn hàng
            Date ngayTao = hoaDon.getCreatedDate();
            String formattedTienGiam;
//            KhuyenMai km = hoaDon.getKhuyenMai();
            String formattedTienShip;
            String ghiChu;
            //Kiem tra gi chu
            if (hoaDon.getGhiChu()==null){
                ghiChu="Không";
            }else{
                ghiChu = hoaDon.getGhiChu();
            }

            BigDecimal tienShip =BigDecimal.valueOf(hoaDon.getTienShip());

            if (tienShip == null){
                formattedTienShip = "0 đ";
            }else{
                formattedTienShip = tienShip + " đ";
            }
            //lấy ra tình trạng thanh toán
            if(hoaDon.getLoaiHoaDon() ==0){
                tinhTrangThanhToan = "Thanh toán khi nhận hàng";
            }
            if(hoaDon.getLoaiHoaDon() ==2){
                tinhTrangThanhToan = "Đã thanh toán";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedNgayTao = dateFormat.format(ngayTao);

            htmlContentBuilder.append("<h3>").append("Thông tin đơn hàng").append("</h1>");
            htmlContentBuilder.append("<p>Mã đơn hàng: ").append(hoaDon.getMaHoaDon()).append("</p>");
            htmlContentBuilder.append("<p>Ngày đặt: ").append(formattedNgayTao).append("</p>");
            htmlContentBuilder.append("<p>Khách hàng: ").append(hoaDon.getNguoiNhan()).append("</p>");
            htmlContentBuilder.append("<p>Số điện thoại khách hàng: ").append(hoaDon.getSDTNguoiNhan()).append("</p>");
            htmlContentBuilder.append("<p>Địa chỉ: ").append(hoaDon.getDiaChiGiaoHang()).append("</p>");
            htmlContentBuilder.append("<p>Trạng thái thanh toán: ").append(tinhTrangThanhToan).append("</p>");
            htmlContentBuilder.append("<p>Ghi chú: ").append(ghiChu).append("</p>");

            String formattedTongTienDonHang = numberFormat.format(hoaDon.getTongTienDonHang());
            String formattedTongTienHoaDon = numberFormat.format(hoaDon.getTongTienHoaDon());
            // Thêm chi tiết đơn hàng
            htmlContentBuilder.append("<h3>").append("Chi tiết đơn hàng").append("</h1>");
            htmlContentBuilder.append("<table>");
            htmlContentBuilder.append("<tr><th>Sản phẩm</th><th>Số lượng</th><th>Thành tiền</th></tr>");
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                NumberFormat fomatTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String fomatTienSanPham = fomatTien.format(hoaDonChiTiet.getThanhTien());
                htmlContentBuilder.append("<tr>");
                htmlContentBuilder.append("<td>").append(hoaDonChiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham())
//                        .append(" (").append(hoaDonChiTiet.getSanPhamChiTiet().getKichCo().getKichCo())
//                        .append("/").append(hoaDonChiTiet.getSanPhamChiTiet().getMauSac().getTenMauSac()).append(")")
                        .append("</td>");

                htmlContentBuilder.append("<td>").append(hoaDonChiTiet.getSoLuong()).append("</td>");
                htmlContentBuilder.append("<td>").append(fomatTienSanPham).append("</td>");
                htmlContentBuilder.append("</tr>");
            }
            htmlContentBuilder.append("</table>");

            // Thêm tổng tiền và các thông tin khác của hóa đơn nếu cần
            htmlContentBuilder.append("<p>Tổng giá trị đơn hàng: ").append(formattedTongTienHoaDon).append("</p>");
//            htmlContentBuilder.append("<p>Tiền giảm: ").append(formattedTienGiam).append("</p>");
            htmlContentBuilder.append("<p>Tiền giao hàng: ").append(formattedTienShip).append("</p>");
            htmlContentBuilder.append("<p>Tổng tiền thanh toán: ").append(formattedTongTienDonHang).append("</p>");

            htmlContentBuilder.append("<h3>Xin Cảm Ơn Quý Khách Đã Ủng Hộ SHOP CHÚNG TÔI!</h3>");
            htmlContentBuilder.append("</body></html>");

            // Gọi phương thức tạo file PDF từ nội dung HTML, sử dụng thư viện iText
            byte[] pdfBytes = createPdfFromHtml(htmlContentBuilder);

            // Lưu file PDF vào thư mục dự án
            String filePath = "C:\\Users\\Quang\\Documents\\GitHub\\Shop_Ban_Ghe\\backend\\hoa_don_" + hoaDonId + ".pdf";
            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                fileOutputStream.write(pdfBytes);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi nếu cần thiết
            }

            // Thiết lập thông tin phản hồi
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "hoa_don.pdf");

            // Trả về file PDF dưới dạng byte[]
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        }
        return ResponseEntity.notFound().build();
    }

    // Phương thức tạo file PDF từ nội dung HTML sử dụng thư viện iText
    private byte[] createPdfFromHtml(StringBuilder htmlContent) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlContent.toString(), outputStream, converterProperties);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
