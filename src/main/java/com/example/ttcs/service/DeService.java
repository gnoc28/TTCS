package com.example.ttcs.service;

import com.example.ttcs.dto.GiaoBaiRequest;
import com.example.ttcs.dto.response.CauHoiDeChiTietResponse;
import com.example.ttcs.dto.response.DeChiTietResponse;
import com.example.ttcs.dto.response.LuaChonDeResponse;
import com.example.ttcs.entity.CauHoi;
import com.example.ttcs.entity.De;
import com.example.ttcs.entity.GiaoChoLop;
import com.example.ttcs.entity.LopHoc;
import com.example.ttcs.entity.LuaChon;
import com.example.ttcs.repository.CauHoiRepository;
import com.example.ttcs.repository.DeRepository;
import com.example.ttcs.repository.GiaoChoLopRepository;
import com.example.ttcs.repository.LopHocRepository;
import com.example.ttcs.repository.LuaChonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeService {

    @Autowired
    private DeRepository deRepository;

    @Autowired
    private LopHocRepository lopHocRepository;

    @Autowired
    private GiaoChoLopRepository giaoChoLopRepository;

    @Autowired
    private CauHoiRepository cauHoiRepository;

    @Autowired
    private LuaChonRepository luaChonRepository;

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

    // 3. Lấy chi tiết đề: thông tin đề + danh sách câu hỏi + lựa chọn
    public DeChiTietResponse layChiTietDe(Integer deId) {
        De de = deRepository.findById(deId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề thi!"));

        List<CauHoi> cauHois = cauHoiRepository.findByDeIdOrderByThuTuAsc(deId);
        List<Integer> cauHoiIds = cauHois.stream().map(CauHoi::getId).toList();

        List<LuaChon> allLuaChon = cauHoiIds.isEmpty()
                ? new ArrayList<>()
                : luaChonRepository.findByCauHoiIds(cauHoiIds);

        Map<Integer, List<LuaChon>> mapLuaChon = allLuaChon.stream()
                .collect(Collectors.groupingBy(lc -> lc.getCauHoi().getId()));

        List<CauHoiDeChiTietResponse> cauHoiResponses = new ArrayList<>();
        BigDecimal tongDiem = BigDecimal.ZERO;

        for (CauHoi cauHoi : cauHois) {
            CauHoiDeChiTietResponse cauHoiRes = new CauHoiDeChiTietResponse();
            cauHoiRes.setCauHoiId(cauHoi.getId());
            cauHoiRes.setNoiDung(cauHoi.getNoiDung());
            cauHoiRes.setThuTu(cauHoi.getThuTu());
            cauHoiRes.setDiem(cauHoi.getDiem());
            cauHoiRes.setMucDo(cauHoi.getMucDo());

            List<LuaChonDeResponse> luaChonDeResponses = mapLuaChon
                    .getOrDefault(cauHoi.getId(), new ArrayList<>())
                    .stream()
                    .map(lc -> {
                        LuaChonDeResponse res = new LuaChonDeResponse();
                        res.setKyHieu(lc.getKyHieu());
                        res.setNoiDung(lc.getNoiDung());
                        return res;
                    })
                    .toList();

            cauHoiRes.setLuaChons(luaChonDeResponses);
            cauHoiResponses.add(cauHoiRes);

            if (cauHoi.getDiem() != null) {
                tongDiem = tongDiem.add(cauHoi.getDiem());
            }
        }

        DeChiTietResponse response = new DeChiTietResponse();
        response.setDeId(de.getId());
        response.setTieuDe(de.getTieuDe());
        response.setPhamViGiao(de.getPhamViGiao());
        response.setThoiGian(de.getThoiGian());
        response.setBatDau(de.getBatDau());
        response.setKetThuc(de.getKetThuc());
        response.setGioiHanNop(de.getGioiHanNop());
        response.setDaXuatBan(de.getDaXuatBan());
        response.setSoCauHoi(cauHoiResponses.size());
        response.setTongDiem(tongDiem);
        response.setCauHois(cauHoiResponses);
        response.setNguoiTaoTen(de.getNguoiTao().getTen());
        response.setCreatedAt(de.getCreatedAt());
        return response;
    }

    // Lấy danh sách đề đã giao của giáo viên
    public List<GiaoChoLop> layDanhSachDeGiaoTheoGiaoVien(Integer giaoVienId) {
        // Lấy tất cả GiaoChoLop có de.nguoiTao.id = giaoVienId
        return giaoChoLopRepository.findAll().stream()
                .filter(gcl -> gcl.getDe() != null && gcl.getDe().getNguoiTao() != null &&
                        gcl.getDe().getNguoiTao().getId().equals(giaoVienId))
                .toList();
    }
}