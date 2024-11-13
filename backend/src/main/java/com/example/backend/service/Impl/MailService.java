package com.example.backend.service.Impl;

import com.example.backend.entity.HoaDon;
import com.example.backend.entity.HoaDonChiTiet;
import com.example.backend.repository.HoaDonChiTietRepository;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    NhanVienRepository nhanVienRepository;

    public void sendOrderConfirmationEmail(String recipientEmail,
                                           HoaDon hoaDon) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(hoaDon.getEmailNguoiNhan());
        helper.setTo(recipientEmail);
        helper.setSubject("Đơn hàng của bạn đã được đặt thành công");

        long idHoaDon = hoaDon.getId();

        // Lấy danh sách ảnh chính của tất cả sản phẩm và lưu vào List
        List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findByIDBill(idHoaDon);
        List<String> tenAnhChinhList = new ArrayList<>();

        // Tạo context và thêm thông tin đơn hàng vào mẫu email
        Context context = new Context();
        context.setVariable("id", hoaDon.getId());
        context.setVariable("nguoiNhan", hoaDon.getNguoiNhan());
        context.setVariable("tongTien", hoaDon.getTongTienHoaDon());
        context.setVariable("hoaDonChiTiet", hoaDonChiTiet);
        context.setVariable("hoaDon", hoaDon);
        context.setVariable("trangThai", hoaDon.getTrangThai().getName());
        context.setVariable("tenAnhChinhList", tenAnhChinhList);
        // Thêm các thông tin khác của đơn hàng vào context nếu cần

        String emailContent = templateEngine.process("mail/GuiMail", context);

        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }
    public void guiMailKhiThaoTac(String recipientEmail,
                                  HoaDon hoaDon) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(hoaDon.getEmailNguoiNhan());
        helper.setTo(recipientEmail);
        helper.setSubject("Đơn hàng của bạn đã được nhân viên của chúng tôi xử lý");

        long idHoaDon = hoaDon.getId();

        // Lấy danh sách ảnh chính của tất cả sản phẩm và lưu vào List
        List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findByIDBill(idHoaDon);
        List<String> tenAnhChinhList = new ArrayList<>();

        // Tạo context và thêm thông tin đơn hàng vào mẫu email
        Context context = new Context();
        context.setVariable("id", hoaDon.getId());
        context.setVariable("nguoiNhan", hoaDon.getNguoiNhan());
        context.setVariable("tongTien", hoaDon.getTongTienHoaDon());
        context.setVariable("hoaDonChiTiet", hoaDonChiTiet);
        context.setVariable("hoaDon", hoaDon);
        context.setVariable("trangThai", hoaDon.getTrangThai().getName());
        context.setVariable("tenAnhChinhList", tenAnhChinhList);
        // Thêm các thông tin khác của đơn hàng vào context nếu cần

        String emailContent = templateEngine.process("mail/GuiMailKhiThaoTac", context);

        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }
    public SimpleMailMessage sendMailRanDom(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        return message;
    }
}
