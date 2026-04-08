package com.example.ttcs.dto;

import lombok.Data;
import java.util.List;

@Data
public class BaiGiangRequest {
    private String tieuDe;
    private String noiDung;
    private Integer idGiaoVien; 
    // Đã sửa thành Integer cho khớp với bảng LopHoc của bạn
    private List<Integer> danhSachIdLopHoc; 
    private List<FileDto> danhSachFile;

    @Data
    public static class FileDto {
        private String tenFile;
        private String urlFile;
        private String loaiFile;
    }
}