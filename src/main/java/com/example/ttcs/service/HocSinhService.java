package com.example.ttcs.service; // Nhớ đổi com.example.ttcs cho khớp với project của ông

import com.example.ttcs.entity.HocSinh;
import com.example.ttcs.repository.HocSinhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HocSinhService {
    
    @Autowired
    private HocSinhRepository hocSinhRepository;

    // Lấy toàn bộ học sinh có trong Database
    public List<HocSinh> layTatCaHocSinh() {
        return hocSinhRepository.findAll();
    }
}