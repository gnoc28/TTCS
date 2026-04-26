package com.example.ttcs.dto.response;

import java.util.Map;

public class ThongKeResponse {
    public String tieuDe;
    public Map<String, Object> thongKe;

    public ThongKeResponse(Map<String, Object> thongKe) {
        this.thongKe = thongKe;
    }
}