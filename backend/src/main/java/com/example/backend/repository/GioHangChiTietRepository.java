package com.example.backend.repository;

import com.example.backend.entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Long> {
    @Query(value = "select * from gio_hang_chi_tiet where gio_hang_id = ? and is_deleted = false", nativeQuery = true)
    List<GioHangChiTiet> findByCartID(long id);

    @Query(value = "select * from gio_hang_chi_tiet where san_pham_chi_tiet_id = ?", nativeQuery = true)
    GioHangChiTiet findByProductDetailsID(long id);

    @Modifying
    @Query(value = "update gio_hang_chi_tiet set is_deleted = true where san_pham_chi_tiet_id = ?", nativeQuery = true)
    void deleteGioHangChiTiet(long san_pham_chi_tiet_id);

    @Query(value = "select * from gio_hang_chi_tiet where san_pham_chi_tiet_id = ?1 and gio_hang_id = ?2 and is_deleted = false", nativeQuery = true)
    Optional<GioHangChiTiet> checkGioHangChiTiet(long san_pham_chi_tiet_id, long gio_hang_id);

    @Query(value = "select * from gio_hang_chi_tiet where san_pham_chi_tiet_id = ? and is_deleted = false", nativeQuery = true)
    List<GioHangChiTiet> findCartBySPCTID(long san_pham_chi_tiet_id);

    @Query(value = "select * from gio_hang_chi_tiet where san_pham_chi_tiet_id = ? and is_deleted = true", nativeQuery = true)
    List<GioHangChiTiet> findCartBySPCTIDDeleteTrue(long san_pham_chi_tiet_id);
}
