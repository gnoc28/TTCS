package com.example.ttcs.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class KetQuaResponse {
    private Integer ketQuaId;
    private Integer lanThu;
    private BigDecimal diemSo;
    private int soCauDung;
    private int tongSoCau;
    private Long thoiGianLamGiay;
    private String nhanXetHeThong;

    public KetQuaResponse(Integer ketQuaId, Integer lanThu, BigDecimal diem, int soCauDung, int tongSoCau,
            Long thoiGianLamGiay, String nhanXetHeThong) {
        this.ketQuaId = ketQuaId;
        this.lanThu = lanThu;
        this.diemSo = diem;
        this.soCauDung = soCauDung;
        this.tongSoCau = tongSoCau;
        this.thoiGianLamGiay = thoiGianLamGiay;
        this.nhanXetHeThong = nhanXetHeThong;
    }
}