package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AutoCloseable.class)
public abstract class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "created_by", updatable = false)
    private String createdby;

    @Column(name = "update_date", updatable = true)
    private Date updateDate;

    @Column(name = "update_by", updatable = true)
    private String updateBy;

    @Column(name = "isDeleted", columnDefinition = "Bit")
    private boolean isDeleted;
}
