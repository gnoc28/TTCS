package com.example.ttcs.dto.response;

import com.example.ttcs.enums.MucDo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CauHoiDeChiTietResponse {
    private Integer cauHoiId;
    private String noiDung;
    private Integer thuTu;
    private BigDecimal diem;
    private MucDo mucDo;
    private List<LuaChonDeResponse> luaChons;
}