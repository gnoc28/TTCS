package com.example.ttcs.service;

import com.example.ttcs.dto.response.CauHoiChiTietKetQuaResponse;
import com.example.ttcs.dto.response.ChiTietKetQuaResponse;
import com.example.ttcs.dto.response.LuaChonItemResponse;
import com.example.ttcs.entity.CauHoi;
import com.example.ttcs.entity.ChiTietKetQua;
import com.example.ttcs.entity.De;
import com.example.ttcs.entity.KetQua;
import com.example.ttcs.entity.LuaChon;
import com.example.ttcs.enums.PhamViGiao;
import com.example.ttcs.repository.CauHoiRepository;
import com.example.ttcs.repository.ChiTietKetQuaRepository;
import com.example.ttcs.repository.KetQuaRepository;
import com.example.ttcs.repository.LuaChonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChiTietKetQuaService {
    @Autowired
    private CauHoiRepository cauHoiRepository;

    @Autowired
    private LuaChonRepository luaChonRepository;

    @Autowired
    private KetQuaRepository ketQuaRepository;

    @Autowired
    private ChiTietKetQuaRepository chiTietKetQuaRepository;

    public ChiTietKetQuaResponse getChiTietKetQua(Integer ketQuaId) {
        KetQua kq = ketQuaRepository.findById(ketQuaId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả"));

        List<ChiTietKetQua> ctList = chiTietKetQuaRepository.findByKetQuaId(ketQuaId);

        De deThi = kq.getDe();

        if (deThi != null) {
            // Giả sử deThi.getPhamViGiao() trả về kiểu Enum PhamViGiao
            if (deThi.getPhamViGiao() == PhamViGiao.LOP && deThi.getKetThuc() != null) {
                if (LocalDateTime.now().isBefore(deThi.getKetThuc())) {
                    throw new RuntimeException("Đề thi dành cho lớp học chưa kết thúc, bạn không được phép xem đáp án chi tiết vào lúc này!");
                }
            }
        }
        /*
         * List<Integer> cauHoiIds = new ArrayList<>();
         * for (ChiTietKetQua ct : ctList) {
         * cauHoiIds.add(ct.getCauHoi().getId());
         * }
         */
        List<Integer> cauHoiIds = ctList.stream()
                .map(ct -> ct.getCauHoi().getId())
                .toList();

        List<LuaChon> allLuaChon = luaChonRepository.findByCauHoiIds(cauHoiIds);

        Map<Integer, List<LuaChon>> mapLuaChon = allLuaChon.stream()
                .collect(Collectors.groupingBy(lc -> lc.getCauHoi().getId()));

        List<CauHoiChiTietKetQuaResponse> cauHoiList = new ArrayList<>();

        int soCauDung = 0;

        for (ChiTietKetQua ct : ctList) {

            CauHoi cauHoi = ct.getCauHoi();
            List<LuaChon> luaChons = mapLuaChon.getOrDefault(cauHoi.getId(), new ArrayList<>());

            CauHoiChiTietKetQuaResponse res = new CauHoiChiTietKetQuaResponse();
            res.setCauHoiId(cauHoi.getId());
            res.setNoiDung(cauHoi.getNoiDung());
            res.setThuTu(cauHoi.getThuTu());
            res.setDiem(ct.getDiemCau());
            res.setDapAnDaChon(ct.getLuaChonDaChon());

            List<LuaChonItemResponse> items = new ArrayList<>();
            String dapAnDung = null;

            for (LuaChon lc : luaChons) {
                LuaChonItemResponse item = new LuaChonItemResponse();
                item.setKyHieu(lc.getKyHieu());
                item.setNoiDung(lc.getNoiDung());
                item.setLaDapAn(lc.getLaDapAn());

                if (Boolean.TRUE.equals(lc.getLaDapAn())) {
                    dapAnDung = lc.getKyHieu();
                }

                items.add(item);
            }
            if (dapAnDung != null && dapAnDung.equals(ct.getLuaChonDaChon())) {
                soCauDung++;
            }

            res.setLuaChons(items);
            res.setDapAnDung(dapAnDung);

            cauHoiList.add(res);
        }

        cauHoiList.sort(Comparator.comparing(CauHoiChiTietKetQuaResponse::getThuTu));

        ChiTietKetQuaResponse Response = new ChiTietKetQuaResponse();
        Response.setKetQuaId(ketQuaId);
        Response.setTongDiem(kq.getDiemSo());
        Response.setCauHoiList(cauHoiList);
        return Response;
    }
}