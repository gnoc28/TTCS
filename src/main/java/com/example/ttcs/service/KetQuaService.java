package com.example.ttcs.service;

import com.example.ttcs.dto.request.CauTraLoiRequest;
import com.example.ttcs.dto.request.DiemRequest;
import com.example.ttcs.dto.response.KetQuaResponse;
import com.example.ttcs.entity.*;
import com.example.ttcs.enums.MucDo;
import com.example.ttcs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

@Service
public class KetQuaService {
    @Autowired
    private HocSinhRepository hocSinhRepository;
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

    public KetQuaResponse chamDiem(DiemRequest request) {
        // 1) Validate dữ liệu đầu vào chính (học sinh, đề)
        HocSinh hocSinh = hocSinhRepository.findById(request.getHocSinhId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh"));
        De de = deRepository.findById(request.getDeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));

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
        String nhanXetHeThong = buildNhanXetHeThong(dungTheoMucDo, tongTheoMucDo);
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

    // Rule nhận xét theo tỷ lệ đúng ở 4 mức NB/TH/VD/VDC
    private String buildNhanXetHeThong(Map<MucDo, Integer> dungTheoMucDo, Map<MucDo, Integer> tongTheoMucDo) {
        Double nb = tongTheoMucDo.getOrDefault(MucDo.NB, 0) > 0 ? tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.NB)
                : null;
        Double th = tongTheoMucDo.getOrDefault(MucDo.TH, 0) > 0 ? tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.TH)
                : null;
        Double vd = tongTheoMucDo.getOrDefault(MucDo.VD, 0) > 0 ? tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.VD)
                : null;
        Double vdc = tongTheoMucDo.getOrDefault(MucDo.VDC, 0) > 0
                ? tyLeDung(dungTheoMucDo, tongTheoMucDo, MucDo.VDC)
                : null;

        StringBuilder sb = new StringBuilder();

        if (nb != null && nb < 70) {
            sb.append("Nền tảng NB chưa vững, nên ôn lại kiến thức cốt lõi. ");
        } else if (nb != null && th != null && nb >= 80 && th < 60) {
            sb.append("NB tốt nhưng TH còn yếu, cần luyện nhóm câu hỏi thông hiểu. ");
        } else if (nb != null && th != null && nb >= 80 && th >= 70) {
            sb.append("Nền tảng NB/TH khá tốt. ");
        }

        if (vd != null && vd < 50) {
            sb.append("VD còn thấp, nên tăng bài tập phân tích nhiều bước. ");
        } else if (vd != null && vd >= 70) {
            sb.append("VD tốt, có thể tăng dần câu hỏi khó. ");
        }

        if (vdc != null && vdc < 40) {
            sb.append("VDC chưa ổn định, nên củng cố NB-TH-VD trước khi đẩy độ khó. ");
        } else if (vdc != null && vdc >= 60) {
            sb.append("VDC đạt mức tốt, có thể luyện đề nâng cao theo chuyên đề. ");
        }

        if (sb.length() == 0) {
            return "Kết quả ổn định, hãy duy trì nhịp luyện tập hiện tại.";
        }

        return sb.toString().trim();
    }

    // Hàm chỉ dùng khi mức độ đã có ít nhất 1 câu hỏi
    private double tyLeDung(Map<MucDo, Integer> dungTheoMucDo, Map<MucDo, Integer> tongTheoMucDo, MucDo mucDo) {
        int tong = tongTheoMucDo.getOrDefault(mucDo, 0);
        if (tong == 0) {
            return 0.0;
        }
        int dung = dungTheoMucDo.getOrDefault(mucDo, 0);
        return (dung * 100.0) / tong;
    }
}