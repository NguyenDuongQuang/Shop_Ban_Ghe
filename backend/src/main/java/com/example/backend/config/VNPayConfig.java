package com.example.backend.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VNPayConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_Returnurl = "http://127.0.0.1:8080/payment/return?";
    public static String vnp_ReturnurlMuaNgay = "http://127.0.0.1:8080/payment/MuaNgay/return?";
    public static String vnp_TmnCode = "6UT99E3K";
    public static String vnp_HashSecret = "7ZXMZG06LA5NGTW2CEZK1F99XH4EGKQF";
    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String get_Return_url (long amonut, String id_bill){
        vnp_Returnurl = vnp_Returnurl + "amout=" + amonut + "&id_user=" + id_bill;
        return vnp_Returnurl;
    }

    public static String get_Return_urlMuaNgay (long amonut, String id_bill){
        vnp_ReturnurlMuaNgay = vnp_ReturnurlMuaNgay + "amout=" + amonut + "&id_user=" + id_bill;
        return vnp_ReturnurlMuaNgay;
    }

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static int orderReturn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }

        if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
            return 1;
        } else {
            return 0;
        }
    }
}

