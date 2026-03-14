package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_ket_qua", uniqueConstraints = {
        @UniqueConstraint(name = "uq_ctkq_kq_ch", columnNames = {"ket_qua_id", "cau_hoi_id"})
})
public class ChiTietKetQua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lua_chon_da_chon", length = 1)
    private String luaChonDaChon;

    @Column(name = "diem_cau", nullable = false)
    private BigDecimal diemCau = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "ket_qua_id", nullable = false)
    private KetQua ketQua;

    @ManyToOne
    @JoinColumn(name = "cau_hoi_id", nullable = false)
    private CauHoi cauHoi;
}

