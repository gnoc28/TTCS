package com.example.ttcs.service;

import com.example.ttcs.dto.request.CauTraLoiRequest;
import com.example.ttcs.dto.request.DiemRequest;
import com.example.ttcs.dto.request.ThongKeRequest;
import com.example.ttcs.dto.response.KetQuaResponse;
import com.example.ttcs.dto.response.ThongKeResponse;
import com.example.ttcs.entity.*;
import com.example.ttcs.enums.MucDo;
import com.example.ttcs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KetQuaService {
    @Autowired
    private HocSinhRepository hocSinhRepository;

    @Autowired
    private HocSinhLopRepository hocSinhLopRepository;

    @Autowired
    private DeRepository deRepository;

    @Autowired
    private CauHoiRepository cauHoiRepository;

    @Autowired
    private LuaChonRepository luaChonRepository;

    @Autowired
    private KetQuaRepository ketQuaRepository;

    @Autowired
    private ChiTietKetQuaRepository chiTietKetQuaRepository;

    @Autowired
    private FeedbackGenerationService feedbackGenerationService;
    

    public KetQuaResponse chamDiem(DiemRequest request) {
        // 1) Validate dữ liệu đầu vào chính (học sinh, đề)
        HocSinh hocSinh = hocSinhRepository.findById(request.getHocSinhId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh"));
        De de = deRepository.findById(request.getDeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));

        HocSinhLop hocSinhLop = null;
        if (request.getLopId() != null) {
            hocSinhLop = hocSinhLopRepository
                    .findByLopHocIdAndHocSinhId(request.getLopId(), request.getHocSinhId())
                    .orElse(null);
        }

        // 2) Chốt mốc thời gian bắt đầu/nộp để lưu lịch sử làm bài
        LocalDateTime thoiGianNop = LocalDateTime.now();
        LocalDateTime thoiGianBatDau = request.getThoiGianBatDau() != null
                ? request.getThoiGianBatDau()
                : thoiGianNop;
        if (thoiGianBatDau.isAfter(thoiGianNop)) {
            throw new RuntimeException("Thời gian bắt đầu không được lớn hơn thời gian nộp");
        }

        KetQua kq = new KetQua();
        kq.setHocSinh(hocSinh);
        kq.setDe(de);
        kq.setThoiGianBatDau(thoiGianBatDau);
        kq.setThoiGianNop(thoiGianNop);
        
        if (hocSinhLop != null) {
            kq.setHocSinhLop(hocSinhLop);
        }

        // đếm số lần làm
        Integer lanThuMoi = ketQuaRepository.findTopByHocSinhIdAndDeIdOrderByLanThuDesc(hocSinh.getId(), de.getId())
                .map(kqCu -> kqCu.getLanThu() + 1)
                .orElse(1);
        kq.setLanThu(lanThuMoi);

        ketQuaRepository.save(kq);

        // 3) Chấm điểm và gom thống kê theo mức độ câu hỏi để sinh nhận xét hệ thống
        BigDecimal totalScore = BigDecimal.ZERO;
        int soCauDung = 0;
        Map<MucDo, Integer> tongTheoMucDo = new EnumMap<>(MucDo.class);
        Map<MucDo, Integer> dungTheoMucDo = new EnumMap<>(MucDo.class);

        for (MucDo mucDo : MucDo.values()) {
            tongTheoMucDo.put(mucDo, 0);
            dungTheoMucDo.put(mucDo, 0);
        }

        for (CauTraLoiRequest ans : request.getCauTraLoi()) {
            CauHoi cauHoi = cauHoiRepository.findById(ans.getCauHoiId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi"));
            LuaChon correct = luaChonRepository.findByCauHoiAndLaDapAnTrue(cauHoi);

            ChiTietKetQua ct = new ChiTietKetQua();
            ct.setKetQua(kq);
            ct.setCauHoi(cauHoi);
            ct.setLuaChonDaChon(ans.getLuaChon());

            MucDo mucDo = cauHoi.getMucDo() == null ? MucDo.NB : cauHoi.getMucDo();
            tongTheoMucDo.put(mucDo, tongTheoMucDo.get(mucDo) + 1);

            if (correct != null && correct.getKyHieu().equals(ans.getLuaChon())) {
                ct.setDiemCau(cauHoi.getDiem());
                totalScore = totalScore.add(cauHoi.getDiem());
                soCauDung += 1;
                dungTheoMucDo.put(mucDo, dungTheoMucDo.get(mucDo) + 1);
            } else {
                ct.setDiemCau(BigDecimal.ZERO);
            }
            chiTietKetQuaRepository.save(ct);
        }
        kq.setDiemSo(totalScore);

        // 4) Sinh nhận xét tự động và lưu cùng kết quả
        long thoiGianLamGiay = Duration.between(thoiGianBatDau, thoiGianNop).getSeconds();
        
        // BỔ SUNG: Tính % của cả 4 mức độ (Nếu mức độ nào không có câu hỏi, sẽ trả về -1)
        double nhanBietPercent = tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.NB);
        double thongHieuPercent = tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.TH);
        double vanDungPercent = tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.VD);
        double vanDungCaoPercent = tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.VDC);

        // Truyền 4 tham số vào để "bắt bệnh"
        String nhanXetHeThong = feedbackGenerationService.generateSystemFeedback(
            nhanBietPercent, 
            thongHieuPercent, 
            vanDungPercent, 
            vanDungCaoPercent
        );
        
        kq.setNhanXetHeThong(nhanXetHeThong);
        ketQuaRepository.save(kq);

        return new KetQuaResponse(
                kq.getId(),
                kq.getLanThu(),
                totalScore,
                soCauDung,
                request.getCauTraLoi().size(),
                thoiGianLamGiay,
                nhanXetHeThong);
    }

    // ĐÃ SỬA: Quy ước trả về -1 nếu đề thi không có câu hỏi ở mức độ này
    private double tyLeDung(Map<MucDo, Integer> dungTheoMucDo, Map<MucDo, Integer> tongTheoMucDo, MucDo mucDo) {
        int tong = tongTheoMucDo.getOrDefault(mucDo, 0);
        if (tong == 0) {
            return -1.0; 
        }
        int dung = dungTheoMucDo.getOrDefault(mucDo, 0);
        return (dung * 100.0) / tong;
    }
    // API dành riêng cho Giáo viên: Lưu nhận xét thủ công
    public void luuNhanXetGiaoVien(Integer ketQuaId, String nhanXet) {
        KetQua kq = ketQuaRepository.findById(ketQuaId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả bài làm!"));
        
        kq.setNhanXetGiaoVien(nhanXet);
        ketQuaRepository.save(kq);
    }
}