package com.example.ttcs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreviewExamDTO {
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

    // TODO: Uncomment khi làm phần giao cho lớp
    // private List<GiaoChoLopDTO> giaoChoLop;

    @Data
    public static class NguoiTaoDTO {
        private Integer id;
        private String vaiTro;
    }

    // TODO: Uncomment khi làm phần giao cho lớp
    // @Data
    // public static class GiaoChoLopDTO {
    // private Integer id;
    // private Integer de;
    // private Integer lopHoc;
    // private LopHocDTO lop;
    // private LocalDateTime createdAt;
    // }

    // @Data
    // public static class LopHocDTO {
    // private Integer id;
    // private String tenLop;
    // private String maLop;
    // private String namHoc;
    // }
}