package com.example.backend.config;
import com.example.backend.config.JwtAuthenticationEntryPoint;
import com.example.backend.config.JwtAuthenticationFilter;
import com.example.backend.service.Impl.StaffDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private StaffDetailsServiceImpl staffDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.staffDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .disable()
                .authorizeRequests()

                .antMatchers("/login", "/payment/create", "/payment/return", "/khachHang/register","/sanPham/taoSanPham"
                        , "/customer/cart/addToCart", "/customer/sanPham/danhSach", "/customer/sanPham/loc/gia","/customer/sanPham/timKiemTheoTen/{search}", "/chatLieu/danhSach", "/loaiSanPham/danhSach",
                        "/nhaSanXuat/danhSach","/mauSac/danhSach", "/kichCo/danhSach","/sanPham/danhSach", "/customer/sanPham/loc/mau_sac", "/customer/sanPham/loc/kich_co"
                        , "/customer/sanPham/loc/gia", "/customer/sanPham/loc/loai_san_pham", "/customer/sanPham/loc/chat_lieu"
                        , "/customer/sanPham/loc/nha_san_xuat", "/customer/sanPham/getSanPham/id={id}", "/customer/sanPham/getAnhSanPham/{id}", "/customer/sanPham/api/getSize/{id}",
                        "/customer/sanPham/api/getColor/{id}", "/customer/sanPham/api/getSoLuong", "/api/banHang/online/checkOut",
                        "/api/banHang/online/getHoaDon/{id}", "/api/banHang/online/getHoaDonChiTiet/{id}", "/api/banHang/online/check-out",
                        "/api/banHang/online/add/khuyenMai", "/api/banHang/online/datHang", "/api/muaNgay/check-out",
                        "/api/muaNgay/getHoaDon/{id}", "/api/muaNgay/getHoaDonChiTiet/{id}",
                        "/api/muaNgay/check-out", "/api/muaNgay/add/khuyenMai",
                        "/api/muaNgay/datHang", "/payment/MuaNgay/create", "/payment/MuaNgay/return", "/customer/sanPham/getAnhMacDinhSanPham/{id}", "/customer/sanPham/getAnhByMauSac",
                        "/api/password/get-keys", "/api/password/check-keys", "/api/password/fogot-password",
                        "/customer/changePass", "/api/muaNgay/khuyenMai/list"
                ).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
