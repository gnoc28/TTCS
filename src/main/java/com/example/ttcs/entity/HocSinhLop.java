package com.example.ttcs.entity;

import jakarta.persistence.*;

@Entity
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

    public HocSinhLop() {
    }

    // Getter và Setter cho LopHocService dùng
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LopHoc getLopHoc() {
        return lopHoc;
    }

    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }

    public HocSinh getHocSinh() {
        return hocSinh;
    }

    public void setHocSinh(HocSinh hocSinh) {
        this.hocSinh = hocSinh;
    }
}