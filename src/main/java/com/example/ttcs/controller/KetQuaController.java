package com.example.ttcs.controller;

import com.example.ttcs.dto.request.DiemRequest;
import com.example.ttcs.dto.response.ChiTietKetQuaResponse;
import com.example.ttcs.dto.response.KetQuaResponse;
import com.example.ttcs.service.ChiTietKetQuaService;
import com.example.ttcs.service.KetQuaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ket-qua")
public class KetQuaController {
    @Autowired
    private KetQuaService ketQuaService;

    @Autowired
    private ChiTietKetQuaService chiTietKetQuaService;

    @PostMapping("/submit")
    public ResponseEntity<KetQuaResponse> chamDiem(@RequestBody DiemRequest request) {
        return ResponseEntity.ok(ketQuaService.chamDiem(request));
    }

    @GetMapping("/{id}/chi-tiet")
    public ResponseEntity<ChiTietKetQuaResponse> chiTiet(@PathVariable Integer id) {
        return ResponseEntity.ok(chiTietKetQuaService.getChiTietKetQua(id));
    }
}