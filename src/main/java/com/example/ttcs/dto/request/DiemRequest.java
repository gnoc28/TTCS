package com.example.ttcs.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiemRequest {
    private Integer hocSinhId;
    private Integer deId;
    private Integer lopId; // Thêm trường này để xác định lớp khi nộp bài
    private LocalDateTime thoiGianBatDau;
    private List<CauTraLoiRequest> cauTraLoi;
}