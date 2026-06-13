package com.example.ttcs.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CauHoiChiTietKetQuaResponse {
    private Integer cauHoiId;
    private String noiDung;
    private Integer thuTu;
    private BigDecimal diem;

    private String dapAnDaChon;
    private String dapAnDung;

    private List<LuaChonItemResponse> luaChons;
}