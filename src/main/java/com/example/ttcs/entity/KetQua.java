package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ket_qua")
public class KetQua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "thoi_gian_bat_dau", nullable = false)
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_nop", nullable = false)
    private LocalDateTime thoiGianNop;

    @Column(name = "diem_so")
    private BigDecimal diemSo;

    @Column(name = "nhan_xet_giao_vien", columnDefinition = "TEXT")
    private String nhanXetGiaoVien;

    @Column(name = "nhan_xet_he_thong", columnDefinition = "TEXT")
    private String nhanXetHeThong;

    @ManyToOne
    @JoinColumn(name = "hoc_sinh_id", nullable = false)
    private HocSinh hocSinh;

    @ManyToOne
    @JoinColumn(name = "hoc_sinh_lop_id")
    private HocSinhLop hocSinhLop;

    @Column(name = "lan_thu", nullable = false)
    private Integer lanThu = 1;

    @ManyToOne
    @JoinColumn(name = "de_id", nullable = false)
    private De de;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}


