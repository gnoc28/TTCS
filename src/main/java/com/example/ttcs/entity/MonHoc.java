package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mon_hoc")
public class MonHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten", nullable = false)
    private String ten;

    @ManyToOne
    @JoinColumn(name = "khoi_lop_id", nullable = false)
    private KhoiLop khoiLop;

    public MonHoc() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public KhoiLop getKhoiLop() {
        return khoiLop;
    }

    public void setKhoiLop(KhoiLop khoiLop) {
        this.khoiLop = khoiLop;
    }
}