package com.example.backend.repository;

import com.example.backend.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang,Long> {
    @Query(value = "select * from gio_hang where khach_hang_id = ?", nativeQuery = true)
    GioHang findbyCustomerID(long khach_hang_id);
}
