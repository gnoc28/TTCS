package com.example.ttcs.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiemRequest {
    private Integer hocSinhId;
    private Integer deId;
    private LocalDateTime thoiGianBatDau;
    private List<CauTraLoiRequest> cauTraLoi;
}