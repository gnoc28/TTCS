package com.example.ttcs.entity;

import com.example.ttcs.enums.GioiTinh;
import com.example.ttcs.enums.VaiTro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_dang_nhap", unique = true, nullable = false)
    private String tenDangNhap;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    private String ho;

    @Column(name = "ten_dem")
    private String tenDem;

    @Column(nullable = false)
    private String ten;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Enumerated(EnumType.STRING)
    @Column(name = "gioi_tinh")
    private GioiTinh gioiTinh;

    @Column(name = "anh_dai_dien")
    private String anhDaiDien;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro")
    private VaiTro vaiTro;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "nguoiDung")
    private GiaoVien giaoVien;

    @OneToOne(mappedBy = "nguoiDung")
    private HocSinh hocSinh;
}


