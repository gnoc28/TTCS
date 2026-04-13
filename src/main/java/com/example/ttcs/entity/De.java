package com.example.ttcs.entity;

import com.example.ttcs.enums.PhamViGiao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "de")
public class De {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_hash", nullable = false, unique = true, length = 8)
    private String maHash;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @ManyToOne
    @JoinColumn(name = "nguoi_tao_id", nullable = false)
    private NguoiDung nguoiTao;

    @Enumerated(EnumType.STRING)
    @Column(name = "pham_vi_giao")
    private PhamViGiao phamViGiao;

    @Column(name = "thoi_gian")
    private Integer thoiGian;

    @Column(name = "bat_dau")
    private LocalDateTime batDau;

    @Column(name = "ket_thuc")
    private LocalDateTime ketThuc;

    @Column(name = "gioi_han_nop")
    private Integer gioiHanNop;

    @Column(name = "da_xuat_ban", nullable = false)
    private Boolean daXuatBan = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //them moi
    @ManyToOne
    @JoinColumn(name = "khoi_lop_id")
    private KhoiLop khoiLop;

    //them moi
    @ManyToOne
    @JoinColumn(name = "mon_hoc_id")
    private MonHoc monHoc;
}


