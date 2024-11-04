package com.example.backend.repository;

import com.example.backend.entity.TrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrangThaiRepository extends JpaRepository<TrangThai,Long> {
    @Query(value = "select * from trang_thai where id = ?", nativeQuery = true)
    TrangThai findByID(long id);
}
