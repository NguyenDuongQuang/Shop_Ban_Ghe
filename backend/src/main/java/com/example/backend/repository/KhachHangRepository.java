package com.example.backend.repository;

import com.example.backend.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Long> {
    @Query(value = "select * from khach_hang where is_deleted = false ORDER BY id desc", nativeQuery = true)
    List<KhachHang> findAllCustomer();

    @Query(value = "select * from khach_hang where id = ? and is_deleted = false", nativeQuery = true)
    KhachHang findByID(Long id);

    @Query(value = "select * from khach_hang where ho_ten = ?", nativeQuery = true)
    Optional<KhachHang> findByName(String name);


    @Query(value = "select * from khach_hang where ho_ten = ?1 OR so_dien_thoai = ?1 OR email = ?1 OR dia_chi = ?1", nativeQuery = true)
    List<KhachHang> findCustomerAll(String input);

    @Query(value = "select * from khach_hang where is_deleted = false and date(ngay_sinh) = ?", nativeQuery = true)
    List<KhachHang> findCustomerDate(LocalDate ngaySinh);

    @Query(value = "select * from khach_hang where email = ?", nativeQuery = true)
    KhachHang findByEmail(String email);

    @Query(value = "select * from khach_hang where so_dien_thoai  = ?", nativeQuery = true)
    KhachHang findBySDT(String so_dien_thoai);
}
