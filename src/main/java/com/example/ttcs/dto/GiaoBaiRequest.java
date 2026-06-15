package com.example.ttcs.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GiaoBaiRequest {
    private Integer deId;
    private Integer lopHocId;
    private LocalDateTime hanNop;

    // 1. Constructor không tham số (BẮT BUỘC phải có để Spring nhận JSON)
    public GiaoBaiRequest() {
    }

    // 2. Constructor có tham số (Cái bạn vừa viết)
    public GiaoBaiRequest(Integer deId, Integer lopHocId, LocalDateTime hanNop) {
        this.deId = deId;
        this.lopHocId = lopHocId;
        this.hanNop = hanNop;
    }

    // 3. Tự viết Getter/Setter (Vì máy bạn đang không nhận @Data)
    public Integer getDeId() {
        return deId;
    }

    public void setDeId(Integer deId) {
        this.deId = deId;
    }

    public Integer getLopHocId() {
        return lopHocId;
    }

    public void setLopHocId(Integer lopHocId) {
        this.lopHocId = lopHocId;
    }

    public LocalDateTime getHanNop() {
        return hanNop;
    }

    public void setHanNop(LocalDateTime hanNop) {
        this.hanNop = hanNop;
    }
}