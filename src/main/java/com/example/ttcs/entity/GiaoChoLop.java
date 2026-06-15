package com.example.ttcs.entity;

import jakarta.persistence.*;

@Entity
public class GiaoChoLop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "de_id")
    private De de;

    @ManyToOne
    @JoinColumn(name = "lop_hoc_id")
    private LopHoc lopHoc;

    public GiaoChoLop() {
    }

    // Getter và Setter cho DeService dùng
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public De getDe() {
        return de;
    }

    public void setDe(De de) {
        this.de = de;
    }

    public LopHoc getLopHoc() {
        return lopHoc;
    }

    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }
}