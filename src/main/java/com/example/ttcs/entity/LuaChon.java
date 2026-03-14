package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lua_chon")
public class LuaChon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ky_hieu", nullable = false, length = 1)
    private String kyHieu;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "la_dap_an", nullable = false)
    private Boolean laDapAn = false;

    @ManyToOne
    @JoinColumn(name = "cau_hoi_id", nullable = false)
    private CauHoi cauHoi;
}


