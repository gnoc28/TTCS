package com.example.ttcs.dto;

public class LopHocHocSinhResponse {
    private Integer id;
    private String tenLop;
    private String maLop;
    private String namHoc;
    private Integer soLuongHS;

    private KhoiLopDTO khoiLop;
    private MonHocDTO monHoc;
    private GiaoVienDTO giaoVien;

    public LopHocHocSinhResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getSoLuongHS() {
        return soLuongHS;
    }

    public void setSoLuongHS(Integer soLuongHS) {
        this.soLuongHS = soLuongHS;
    }

    public KhoiLopDTO getKhoiLop() {
        return khoiLop;
    }

    public void setKhoiLop(KhoiLopDTO khoiLop) {
        this.khoiLop = khoiLop;
    }

    public MonHocDTO getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(MonHocDTO monHoc) {
        this.monHoc = monHoc;
    }

    public GiaoVienDTO getGiaoVien() {
        return giaoVien;
    }

    public void setGiaoVien(GiaoVienDTO giaoVien) {
        this.giaoVien = giaoVien;
    }

    public static class KhoiLopDTO {
        private Integer id;
        private String ten;

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
    }

    public static class MonHocDTO {
        private Integer id;
        private String ten;

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
    }

    public static class GiaoVienDTO {
        private Integer id;
        private String hoTen;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }
    }
}