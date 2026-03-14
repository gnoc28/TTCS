package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bai_giang_lop_hoc", uniqueConstraints = {
        @UniqueConstraint(name = "uq_bg_lop_hoc", columnNames = {"bai_giang_id", "lop_hoc_id"})
})
public class BaiGiangLopHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bai_giang_id", nullable = false)
    private BaiGiang baiGiang;

    @ManyToOne
    @JoinColumn(name = "lop_hoc_id", nullable = false)
    private LopHoc lopHoc;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

