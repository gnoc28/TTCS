package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GiaoVienKhoiLop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "khoi_lop_id", nullable = false)
    private KhoiLop khoiLop;

    @ManyToOne
    @JoinColumn(name = "giao_vien_id", nullable = false)
    private GiaoVien giaoVien;
}
