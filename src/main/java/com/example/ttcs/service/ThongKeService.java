package com.example.ttcs.service;

import com.example.ttcs.dto.request.ThongKeRequest;
import com.example.ttcs.dto.response.ThongKeResponse;
import com.example.ttcs.entity.*;
import com.example.ttcs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThongKeService {
    @Autowired
    private HocSinhLopRepository hocSinhLopRepository;
    @Autowired
    private KetQuaRepository ketQuaRepository;
    @Autowired
    private ChiTietKetQuaRepository chiTietKetQuaRepository;
    @Autowired
    private LuaChonRepository luaChonRepository;
    @Autowired
    private DeRepository deRepository;

    /**
     * Thống kê kết quả bài làm theo đề và lớp hoặc theo hocSinhLopId
     * 
     * @param req ThongKeRequest chứa deId, lopId, hocSinhLopId
     * @return ThongKeResponse (Map<String, Object> thongKe)
     */
    public ThongKeResponse thongKeKetQuaTheoDeVaLopOrHocSinhLop(ThongKeRequest req) {
        // Xác định lớp cần thống kê (ưu tiên theo hocSinhLopId nếu có)
        final Integer lopId;
        if (req.hocSinhLopId != null) {
            Optional<HocSinhLop> hslopt = hocSinhLopRepository.findById(req.hocSinhLopId);
            if (hslopt.isPresent()) {
                lopId = hslopt.get().getLopHoc().getId();
            } else {
                lopId = req.lopId;
            }
        } else {
            lopId = req.lopId;
        }
        // Lọc danh sách kết quả theo đề và lớp, chỉ lấy kết quả cao nhất của mỗi học
        // sinh
        List<KetQua> result = ketQuaRepository.findAll().stream()
                .filter(kq -> kq.getDe() != null && kq.getDe().getId().equals(req.deId)
                        && kq.getHocSinhLop() != null && kq.getHocSinhLop().getLopHoc() != null
                        && kq.getHocSinhLop().getLopHoc().getId().equals(lopId))
                .collect(Collectors.groupingBy(
                        kq -> kq.getHocSinh().getId(),
                        Collectors.maxBy(Comparator
                                .comparing(kq -> kq.getDiemSo() != null ? kq.getDiemSo().doubleValue() : 0.0))))
                .values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // Tính tổng số bài nộp, điểm TB, điểm cao nhất, tổng số học sinh
        int totalSubmissions = result.size();
        double avgScore = totalSubmissions > 0
                ? result.stream().mapToDouble(kq -> kq.getDiemSo() != null ? kq.getDiemSo().doubleValue() : 0).average()
                        .orElse(0)
                : 0;
        double maxScore = result.stream().mapToDouble(kq -> kq.getDiemSo() != null ? kq.getDiemSo().doubleValue() : 0)
                .max().orElse(0);
        int totalStudents = 1;
        if (!result.isEmpty() && result.get(0).getHocSinhLop() != null
                && result.get(0).getHocSinhLop().getLopHoc() != null) {
            totalStudents = hocSinhLopRepository.countByLopHocId(lopId);
        }
        // Tính tỷ lệ hoàn thành bài (số học sinh đã nộp / tổng số học sinh)
        java.util.Set<Integer> hocSinhLopIdsDaNop = result.stream()
                .filter(kq -> kq.getHocSinhLop() != null)
                .map(kq -> kq.getHocSinhLop().getId())
                .collect(java.util.stream.Collectors.toSet());
        int soHocSinhHoanThanh = hocSinhLopIdsDaNop.size();
        double completionRate = (totalStudents > 0) ? ((double) soHocSinhHoanThanh / totalStudents) : 1.0;
        // Phân phối điểm số thành các khoảng (0-2, 2-4, ...)
        int[] scoreDistribution = new int[5];
        for (KetQua kq : result) {
            double score = kq.getDiemSo() != null ? kq.getDiemSo().doubleValue() : 0;
            if (score <= 2)
                scoreDistribution[0]++;
            else if (score <= 4)
                scoreDistribution[1]++;
            else if (score <= 6)
                scoreDistribution[2]++;
            else if (score <= 8)
                scoreDistribution[3]++;
            else
                scoreDistribution[4]++;
        }
        // Tạo danh sách học sinh đã nộp bài với thông tin chi tiết
        var students = result.stream().map(kq -> new java.util.HashMap<String, Object>() {
            {
                put("id", kq.getId());
                put("name",
                        kq.getHocSinh() != null ? kq.getHocSinh().getNguoiDung().getTen()
                                : (kq.getHocSinhLop() != null && kq.getHocSinhLop().getHocSinh() != null
                                        ? kq.getHocSinhLop().getHocSinh().getNguoiDung().getTen()
                                        : ""));
                put("attempt", kq.getLanThu());
                put("score", kq.getDiemSo());
                if (kq.getThoiGianBatDau() != null && kq.getThoiGianNop() != null) {
                    long seconds = java.time.Duration.between(kq.getThoiGianBatDau(), kq.getThoiGianNop()).getSeconds();
                    put("time", seconds);
                } else {
                    put("time", 0);
                }
            }
        }).toList();
        // Xác định top 5 câu hỏi sai nhiều nhất và các đáp án của chúng
        Map<Integer, int[]> wrongCountMap = new HashMap<>();
        Map<Integer, String> questionContentMap = new HashMap<>();
        List<ChiTietKetQua> allDetails = new ArrayList<>();
        for (KetQua kq : result) {
            List<ChiTietKetQua> details = chiTietKetQuaRepository.findByKetQuaId(kq.getId());
            allDetails.addAll(details);
        }

        //nhan xet he thong cho giao vien
        int tongNB = 0, dungNB = 0;
        int tongTH = 0, dungTH = 0;
        int tongVD = 0, dungVD = 0;
        int tongVDC = 0, dungVDC = 0;

        for (ChiTietKetQua ct : allDetails) {

            boolean dung = ct.getDiemCau() != null
                    && ct.getDiemCau().doubleValue() > 0;

            switch (ct.getCauHoi().getMucDo()) {

                case NB:
                    tongNB++;
                    if (dung)
                        dungNB++;
                    break;

                case TH:
                    tongTH++;
                    if (dung)
                        dungTH++;
                    break;

                case VD:
                    tongVD++;
                    if (dung)
                        dungVD++;
                    break;

                case VDC:
                    tongVDC++;
                    if (dung)
                        dungVDC++;
                    break;
            }
        }

        double avgNB = tongNB > 0 ? dungNB * 100.0 / tongNB : -1;
        double avgTH = tongTH > 0 ? dungTH * 100.0 / tongTH : -1;
        double avgVD = tongVD > 0 ? dungVD * 100.0 / tongVD : -1;
        double avgVDC = tongVDC > 0 ? dungVDC * 100.0 / tongVDC : -1;
        // nhan xet he thong cho giao vien

        for (ChiTietKetQua ct : allDetails) {
            Integer cauHoiId = ct.getCauHoi().getId();
            String noiDung = ct.getCauHoi().getNoiDung();
            int thuTu = ct.getCauHoi().getThuTu() != null ? ct.getCauHoi().getThuTu() : 0;
            if (ct.getDiemCau() == null || ct.getDiemCau().doubleValue() == 0) {
                wrongCountMap.putIfAbsent(cauHoiId, new int[] { 0, thuTu });
                wrongCountMap.get(cauHoiId)[0]++;
                questionContentMap.putIfAbsent(cauHoiId, noiDung);
            }
        }
        List<Map<String, Object>> hardestQuestions = wrongCountMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue()[0], a.getValue()[0]))
                .limit(5)
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", e.getKey());
                    m.put("content", questionContentMap.get(e.getKey()));
                    m.put("wrongCount", e.getValue()[0]);
                    m.put("order", e.getValue()[1]);
                    List<LuaChon> luaChons = luaChonRepository.findByCauHoiId(e.getKey());
                    List<Map<String, Object>> answers = luaChons.stream().map(lc -> {
                        Map<String, Object> ans = new HashMap<>();
                        ans.put("kyHieu", lc.getKyHieu());
                        ans.put("noiDung", lc.getNoiDung());
                        ans.put("laDapAn", lc.getLaDapAn());
                        return ans;
                    }).collect(Collectors.toList());
                    m.put("answers", answers);
                    return m;
                })
                .collect(Collectors.toList());
        // Tạo object thống kê trả về cho FE
        java.util.Map<String, Object> thongKe = new java.util.HashMap<>();
        thongKe.put("totalSubmissions", totalSubmissions);
        thongKe.put("avgScore", avgScore);
        thongKe.put("completionRate", completionRate);
        thongKe.put("maxScore", maxScore);
        thongKe.put("scoreDistribution", scoreDistribution);
        thongKe.put("hardestQuestions", hardestQuestions);
        thongKe.put("students", students);

        String tieuDe = null;
        if (req.deId != null) {
            Optional<De> deOpt = deRepository.findById(req.deId);
            if (deOpt.isPresent()) {
                tieuDe = deOpt.get().getTieuDe();
            }
        }
        thongKe.put("avgNB", avgNB);
        thongKe.put("avgTH", avgTH);
        thongKe.put("avgVD", avgVD);
        thongKe.put("avgVDC", avgVDC);
        ThongKeResponse response = new ThongKeResponse(thongKe);
        response.tieuDe = tieuDe;
        return response;
    }
}