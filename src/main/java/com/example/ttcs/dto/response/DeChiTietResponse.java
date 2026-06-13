package com.example.ttcs.dto.response;

import com.example.ttcs.enums.PhamViGiao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DeChiTietResponse {
    private Integer deId;
    private String tieuDe;
    private PhamViGiao phamViGiao;
    private Integer thoiGian;
    private LocalDateTime batDau;
    private LocalDateTime ketThuc;
    private Integer gioiHanNop;
    private Boolean daXuatBan;
    private Integer soCauHoi;
    private BigDecimal tongDiem;
    private List<CauHoiDeChiTietResponse> cauHois;
    private String nguoiTaoTen;
    private LocalDateTime createdAt;
}