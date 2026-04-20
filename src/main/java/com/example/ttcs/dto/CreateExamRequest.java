package com.example.ttcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class CreateExamRequest {

    @JsonProperty("tieuDe")
    private String tieuDe;

    // noiDungDe chứa toàn bộ JSON từ FE (cauHois)
    @JsonProperty("noiDungDe")
    private NoiDungDeDto noiDungDe;

    @Data
    public static class NoiDungDeDto {
        // key: "Câu 1", "Câu 2", ...
        private Map<String, CauHoiDto> cauHois;
    }
}