package com.example.ttcs.entity;

import com.example.ttcs.enums.LoaiFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "file_bai_giang")
public class FileBaiGiang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tieu_de")
    private String tieuDe;

    @Column(name = "duong_dan", nullable = false, length = 1000)
    private String duongDan;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_file", nullable = false)
    private LoaiFile loaiFile = LoaiFile.url;

    @ManyToOne
    @JoinColumn(name = "bai_giang_id", nullable = false)
    private BaiGiang baiGiang;
}


