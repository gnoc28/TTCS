package com.example.ttcs.dto;

public class LopHocRequest {
    private String tenLop;
    private String maLop;
    private String namHoc;
    private Integer khoiLopId;
    private Integer monHocId;
    private Integer giaoVienId;

    public LopHocRequest() {
    }

    // Getter và Setter cho LopHocService dùng
    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public Integer getGiaoVienId() {
        return giaoVienId;
    }

    public void setGiaoVienId(Integer giaoVienId) {
        this.giaoVienId = giaoVienId;
    }

    public Integer getKhoiLopId() {
        return khoiLopId;
    }

    public void setKhoiLopId(Integer khoiLopId) {
        this.khoiLopId = khoiLopId;
    }

    public Integer getMonHocId() {
        return monHocId;
    }

    public void setMonHocId(Integer monHocId) {
        this.monHocId = monHocId;
    }
}