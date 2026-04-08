package com.example.ttcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PublishExamRequest {

    @JsonProperty("tieuDe")
    private String tieuDe;

    @JsonProperty("thoiGian")
    private Integer thoiGian;

    @JsonProperty("batDau")
    private LocalDateTime batDau;

    @JsonProperty("ketThuc")
    private LocalDateTime ketThuc;

    @JsonProperty("gioiHanNop")
    private Integer gioiHanNop;

    // Danh sách id lớp GV muốn giao
    @JsonProperty("cacLopDaGiao")
    private List<Integer> cacLopDaGiao;
}