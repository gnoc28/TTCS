package com.example.ttcs.entity;

import com.example.ttcs.enums.MucDo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cau_hoi")
public class CauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(nullable = false)
    private BigDecimal diem = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "mucdo", nullable = false)
    private MucDo mucDo = MucDo.NB;

    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;

    @ManyToOne
    @JoinColumn(name = "de_id", nullable = false)
    private De de;
}

