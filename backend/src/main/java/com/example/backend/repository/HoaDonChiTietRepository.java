package com.example.backend.repository;

import com.example.backend.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Long> {  @Query(value = "SELECT * FROM hoa_don_chi_tiet WHERE is_deleted = false  ORDER BY id DESC", nativeQuery = true)
      List<HoaDonChiTiet> findAllDetailedInvoice();

    @Query(value = "select * from hoa_don_chi_tiet where hoa_don_id = ? and is_deleted = false", nativeQuery = true)
    List<HoaDonChiTiet> findByIDBill(Long id);

    @Query(value = "select * from hoa_don_chi_tiet where hoa_don_id = ? and san_pham_chi_tiet_id=? and is_deleted = false", nativeQuery = true)
    Optional<HoaDonChiTiet> checkHDCT(long hoa_don_id, long san_pham_chi_tiet_id);

    @Query(value = "SELECT hoa_don_chi_tiet.*\n" +
            "FROM hoa_don_chi_tiet\n" +
            "         JOIN hoa_don ON hoa_don_chi_tiet.hoa_don_id = hoa_don.id\n" +
            "WHERE hoa_don_chi_tiet.san_pham_chi_tiet_id = ?\n" +
            "  AND hoa_don_chi_tiet.is_deleted = false\n" +
            "  AND hoa_don.trang_thai_id = 6;", nativeQuery = true)
    List<HoaDonChiTiet> findBySPCTID(long san_pham_chi_tiet_id);

    @Query(value = "SELECT hoa_don_chi_tiet.*\n" +
            "FROM hoa_don_chi_tiet\n" +
            "         JOIN hoa_don ON hoa_don_chi_tiet.hoa_don_id = hoa_don.id\n" +
            "WHERE hoa_don_chi_tiet.san_pham_chi_tiet_id = ?\n" +
            "  AND hoa_don_chi_tiet.is_deleted = true\n" +
            "  AND hoa_don.trang_thai_id = 6;", nativeQuery = true)
    List<HoaDonChiTiet> findBySPCTIDDeleteTrue(long san_pham_chi_tiet_id);

    @Modifying
    @Query(value = "DELETE FROM hoa_don_chi_tiet WHERE hoa_don_chi_tiet.hoa_don_id IN (SELECT id FROM hoa_don WHERE trang_thai_id = ?)", nativeQuery = true)
    void deleteByID(long trang_thai_id);
}
