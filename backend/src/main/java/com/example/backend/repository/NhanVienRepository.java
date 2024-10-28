package com.example.backend.repository;

import com.example.backend.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien,Long> {
    @Query(value = "select * from nhan_vien where is_deleted  = false  order by id desc ", nativeQuery = true)
    List<NhanVien> findAllStaff();

    @Query(value = "select * from nhan_vien where id = ? and is_deleted = false", nativeQuery = true)
    NhanVien findByID(Long id);

    @Query(value = "select * from nhan_vien where ho_ten = ? and is_deleted = false ", nativeQuery = true)
    NhanVien findByName(String hoTen);

    @Query(value = "select * from nhan_vien where is_deleted = false and (so_dien_thoai LIKE %?1% OR ho_ten LIKE %?1% OR email LIKE %?1%);", nativeQuery = true)
    List<NhanVien> findStaffAll(String input);


    @Query(value =  "select * from nhan_vien where is_deleted = false and date(ngay_sinh) = ?", nativeQuery = true)
    List<NhanVien> findStaffDate(LocalDate ngaySinh);

    @Query(value = "select * from nhan_vien where email = ?", nativeQuery = true)
    NhanVien findByEmail(String username);

    @Query(value = "select * from nhan_vien where so_dien_thoai = ?", nativeQuery = true)
    NhanVien findBySdt(String sdt);
}
