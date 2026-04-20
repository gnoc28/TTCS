package com.example.ttcs.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamConfigResponse {

    private Integer id;
    private String maHash;
    private String tieuDe;
    private NguoiTaoDTO nguoiTao;
    private String phamViGiao;
    private Integer thoiGian;
    private LocalDateTime batDau;
    private LocalDateTime ketThuc;
    private Integer gioiHanNop;
    private Boolean daXuatBan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer khoiLopId;
    private Integer monHocId;

    // Danh sách lớp đã giao (dùng cho ExamAssignment)
    private List<GiaoChoLopDTO> giaoChoLop;

    // Danh sách id lớp đã giao (dùng để check nhanh ở FE)
    private List<Integer> cacLopDaGiao;

    @Data
    public static class NguoiTaoDTO {
        private Integer id;
        private String vaiTro;
    }

    @Data
    public static class GiaoChoLopDTO {
        private Integer id;
        private Integer deId;
        private Integer lopHocId;
        private LopHocRefDTO lop;
    }

    @Data
    public static class LopHocRefDTO {
        private Integer id;
        private String tenLop;
        private String maLop;
        private String namHoc;
    }
}