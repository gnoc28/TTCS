package com.example.ttcs.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExamContentResponse {

    private Integer id;
    private String tieuDe;
    private List<CauHoiDTO> cauHois;

    @Data
    public static class CauHoiDTO {
        private Integer id;
        private String noiDung;
        private Double diem;
        private Integer thuTu;
        private String mucDo;
        private Integer deId;
        private List<LuaChonDTO> luaChons;
    }

    @Data
    public static class LuaChonDTO {
        private Integer id;
        private String kyHieu;
        private String noiDung;
        private Boolean laDapAn;
        private Integer cauHoiId;
    }
}