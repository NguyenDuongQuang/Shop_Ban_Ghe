package com.example.backend.repository;

import com.example.backend.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi,Long> {
    @Query(value = "select * from dia_chi where khach_hang_id = ?", nativeQuery = true)
    List<DiaChi> findByCustomerID(long id);

    @Query(value = "select * from dia_chi where khach_hang_id = ? and id = ?", nativeQuery = true)
    DiaChi findbyCustomerAndID(long khachHang_id, long id);

    @Query(value = "select * from dia_chi where khach_hang_id = ? and dia_chi_mac_dinh = true", nativeQuery = true)
    DiaChi getDiaChi(long khach_hang_id);
}
