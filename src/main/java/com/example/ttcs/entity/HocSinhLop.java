package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "hoc_sinh_lop")
@Getter
@Setter
public class HocSinhLop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lop_hoc_id")
    private LopHoc lopHoc;

    @ManyToOne
    @JoinColumn(name = "hoc_sinh_id")
    private HocSinh hocSinh;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
