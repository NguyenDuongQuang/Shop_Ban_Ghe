package com.example.backend.dto;

import com.example.backend.entity.SanPham;
import lombok.Data;

import java.util.Date;

@Data
public class SanPhamChiTietDTO {

    private Long id;

    private Integer soLuong;

    private boolean status;

    private Date createdDate;

    private String createdby;

    private Date updatedDate;

    private String updatedby;

    private SanPham sanPham;

    private long sanPham_id;
}
