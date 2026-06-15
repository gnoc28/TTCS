package com.example.ttcs.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "giao_vien")
public class GiaoVien {
    @Id
    private Integer id;

    @Column(name = "ma_GV", unique = true)
    private String maGV;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToOne
    @MapsId //id của giao_vien dùng chung với id của nguoi_dung
    @JoinColumn(name = "id") //giao_vien.id = nguoi_dung.id
    private NguoiDung nguoiDung;

    @ManyToOne  // them truong
    @JoinColumn(name = "truong_id")
    private Truong truong;
}


