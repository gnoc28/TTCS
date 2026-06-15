package com.example.ttcs.dto;

import lombok.Data;
import java.util.Map;

@Data
public class CauHoiDto {
    private int thuTu;
    private String noiDung;
    private String mucDo; // NB | TH | VD | VDC
    private double diem;
    private int dong;
    private Map<String, LuaChonDto> luaChons;
}