package com.example.ttcs.service;

import com.example.ttcs.dto.GiaoBaiRequest;
import com.example.ttcs.entity.De;
import com.example.ttcs.entity.GiaoChoLop;
import com.example.ttcs.entity.LopHoc;
import com.example.ttcs.repository.DeRepository;
import com.example.ttcs.repository.GiaoChoLopRepository;
import com.example.ttcs.repository.LopHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeService {

    @Autowired
    private DeRepository deRepository;

    @Autowired
    private LopHocRepository lopHocRepository;

    @Autowired
    private GiaoChoLopRepository giaoChoLopRepository;

    // 1. Logic cho GIÁO VIÊN: Giao bài cho lớp
    @Transactional
    public String giaoDeChoLop(GiaoBaiRequest request) {
        De de = deRepository.findById(request.getDeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề thi!"));

        LopHoc lop = lopHocRepository.findById(request.getLopHocId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học!"));

        GiaoChoLop giaoChoLop = new GiaoChoLop();
        giaoChoLop.setDe(de);
        giaoChoLop.setLopHoc(lop);
        // Giả sử Entity GiaoChoLop của bạn có trường thoiGianKetThuc
        // giaoChoLop.setThoiGianKetThuc(request.getHanNop());

        giaoChoLopRepository.save(giaoChoLop);
        return "Đã giao bài tập thành công cho lớp " + lop.getTenLop();
    }

    // 2. Logic cho HỌC SINH: Lấy danh sách bài tập của lớp mình
    public List<De> layDanhSachBaiTapCuaLop(Long lopHocId) {
        // Tìm tất cả các record giao bài cho lớp này
        List<GiaoChoLop> danhSachGiao = giaoChoLopRepository.findByLopHoc_Id(lopHocId);

        // Trích xuất ra danh sách Đề thi tương ứng
        return danhSachGiao.stream()
                .map(GiaoChoLop::getDe)
                .collect(Collectors.toList());
    }
}