package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "giao_cho_lop", uniqueConstraints = {
        @UniqueConstraint(name = "uq_gcl_bt_lh", columnNames = {"de_id", "lop_hoc_id"})
})
public class GiaoChoLop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "de_id", nullable = false)
    private De de;

    @ManyToOne
    @JoinColumn(name = "lop_hoc_id", nullable = false)
    private LopHoc lopHoc;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}


