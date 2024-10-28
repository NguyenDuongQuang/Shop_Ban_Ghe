package com.example.backend.repository;

import com.example.backend.entity.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham,Long> {
    @Query(value = "SELECT * FROM loai_san_pham WHERE is_deleted = false ORDER BY id DESC", nativeQuery = true)
    List<LoaiSanPham> findAll();

    @Query(value = "select * from loai_san_pham where id = ? and is_deleted = false", nativeQuery = true)
    LoaiSanPham findByID(Long id);

    @Query(value = "SELECT * FROM loai_san_pham WHERE is_deleted = false AND (name LIKE %?1% OR maximumvalue LIKE %?1% OR percent_discount LIKE %?1%)", nativeQuery = true)
    List<LoaiSanPham> findByAll(String input);

    @Query(value = "SELECT * FROM loai_san_pham WHERE is_deleted = false AND DATE(started_date) = ?", nativeQuery = true)
    List<LoaiSanPham> findByDate(LocalDate ngayTao);

    @Query(value = "select * from loai_san_pham where is_deleted = false and loai_san_pham = ?;", nativeQuery = true)
    LoaiSanPham findByName(String name);
}
