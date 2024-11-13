package com.example.backend.repository;

import com.example.backend.entity.Anh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Anh,Long> {
    @Query(value = "select * from hinh_anh where id_product = ? and anh_mac_dinh = true limit 1;", nativeQuery = true)
    Anh getFirstAnhByIdSanPham(@Param("idsp")Long idsp);

    @Query(value = "SELECT * FROM hinh_anh WHERE is_deleted = false ORDER BY id DESC", nativeQuery = true)
    List<Anh> findAll();

    @Query(value = "select * from hinh_anh where id = ? and is_deleted = false", nativeQuery = true)
    Anh findByID(Long id);

    @Query(value = "SELECT * FROM hinh_anh WHERE is_deleted = false AND DATE(started_date) = ?", nativeQuery = true)
    List<Anh> findByDate(LocalDate ngayTao);

    @Query(value = "select * from hinh_anh where name = ?", nativeQuery = true)
    Optional<Anh> findByName(String name);

    @Query(value = "select * from hinh_anh where id_product = ? and is_deleted = false", nativeQuery = true)
    List<Anh> findByIDProduct(long id_sanPham);

    @Query(value = "select * from hinh_anh where id_product = ?1 and  anh_mac_dinh = true;", nativeQuery = true)
    Anh findAnhMacDinh(long id_sp);

    @Modifying
    @Query(value = "delete from hinh_anh where id = ?", nativeQuery = true)
    void xoaAnh(long id);

    @Query(value = "select name from hinh_anh where id_product = ? and anh_mac_dinh = true limit 1", nativeQuery = true)
    String getTenAnhSanPham_HienThiDanhSach(long sanPham_id);

    @Query(value = "select name from hinh_anh where id_product = ? and anh_mac_dinh = true limit 1", nativeQuery = true)
    String getAnhMacDinh(long san_pham_id);

    @Query(value = "select name from hinh_anh where id_product = ? and anh_mac_dinh = true", nativeQuery = true)
    String getAnhSPByMauSacAndSPID(long sanPham_id);

    @Query(value = "select * from hinh_anh where id_product = ?", nativeQuery = true)
    List<Anh> getHinhAnhByProductID(long sanPham_id);

    @Query(value = "select * from hinh_anh where id_product = ?1", nativeQuery = true)
    List<Anh> getHinhAnhs(long sanPham_id);
}
