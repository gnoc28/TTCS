package com.example.ttcs.service;

import com.example.ttcs.dto.ExamConfigResponse;
import com.example.ttcs.dto.ExamContentResponse;
import com.example.ttcs.dto.LopHocRef;
import com.example.ttcs.dto.PublishExamRequest;
import com.example.ttcs.entity.CauHoi;
import com.example.ttcs.entity.De;
import com.example.ttcs.entity.GiaoChoLop;
import com.example.ttcs.entity.LopHoc;
import com.example.ttcs.entity.LuaChon;
import com.example.ttcs.repository.CauHoiRepository;
import com.example.ttcs.repository.DeRepository;
import com.example.ttcs.repository.GiaoChoLopRepository;
import com.example.ttcs.repository.LuaChonRepository;
import com.example.ttcs.repository.LopHocRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamInforService {

    private final DeRepository deRepository;
    private final GiaoChoLopRepository giaoChoLopRepository;
    private final CauHoiRepository cauHoiRepository;
    private final LuaChonRepository luaChonRepository;
    private final LopHocRepository lopHocRepository;

    // ------------------------------------------------------------------ //
    // GET /create_exam/{id}/config
    // ------------------------------------------------------------------ //
    public ExamConfigResponse getConfig(Integer deId, String username) {

        De de = deRepository.findById(deId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề: " + deId));
        // kiem tra quyen xem de
        if (!de.getNguoiTao().getTenDangNhap().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xem đề này");
        }
        List<GiaoChoLop> giaoChoLops = giaoChoLopRepository.findByDeId(deId);

        ExamConfigResponse response = new ExamConfigResponse();
        response.setId(de.getId());
        response.setMaHash(de.getMaHash());
        response.setTieuDe(de.getTieuDe());
        response.setPhamViGiao(de.getPhamViGiao() != null ? de.getPhamViGiao().name() : null);
        response.setThoiGian(de.getThoiGian());
        response.setBatDau(de.getBatDau());
        response.setKetThuc(de.getKetThuc());
        response.setGioiHanNop(de.getGioiHanNop());
        response.setDaXuatBan(de.getDaXuatBan());
        response.setCreatedAt(de.getCreatedAt());
        response.setUpdatedAt(de.getUpdatedAt());

        // Map nguoiTao
        ExamConfigResponse.NguoiTaoDTO nguoiTaoDTO = new ExamConfigResponse.NguoiTaoDTO();
        nguoiTaoDTO.setId(de.getNguoiTao().getId());
        nguoiTaoDTO.setVaiTro(de.getNguoiTao().getVaiTro().name());
        response.setNguoiTao(nguoiTaoDTO);

        // Map giaoChoLop
        List<ExamConfigResponse.GiaoChoLopDTO> giaoChoLopDTOs = giaoChoLops.stream()
                .map(gcl -> {
                    ExamConfigResponse.GiaoChoLopDTO dto = new ExamConfigResponse.GiaoChoLopDTO();
                    dto.setId(gcl.getId());
                    dto.setDeId(de.getId());
                    dto.setLopHocId(gcl.getLopHoc().getId());

                    // Map thông tin lớp học kèm theo
                    ExamConfigResponse.LopHocRefDTO lopRef = new ExamConfigResponse.LopHocRefDTO();
                    lopRef.setId(gcl.getLopHoc().getId());
                    lopRef.setTenLop(gcl.getLopHoc().getTenLop());
                    lopRef.setMaLop(gcl.getLopHoc().getMaLop());
                    lopRef.setNamHoc(gcl.getLopHoc().getNamHoc());
                    dto.setLop(lopRef);

                    return dto;
                })
                .collect(Collectors.toList());

        response.setGiaoChoLop(giaoChoLopDTOs);

        // Danh sách id lớp để FE check nhanh
        List<Integer> cacLopDaGiao = giaoChoLops.stream()
                .map(gcl -> gcl.getLopHoc().getId())
                .collect(Collectors.toList());
        response.setCacLopDaGiao(cacLopDaGiao);

        return response;
    }

    // GET /create_exam/{id}/content
    public ExamContentResponse getContent(Integer deId, String username) {

        De de = deRepository.findById(deId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề: " + deId));
        // kiem tra quyen xem de
        if (!de.getNguoiTao().getTenDangNhap().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xem đề này");
        }
        List<CauHoi> cauHois = cauHoiRepository.findByDeIdOrderByThuTuAsc(deId);

        ExamContentResponse response = new ExamContentResponse();
        response.setId(de.getId());
        response.setTieuDe(de.getTieuDe());

        List<ExamContentResponse.CauHoiDTO> cauHoiDTOs = cauHois.stream()
                .map(cauHoi -> {
                    ExamContentResponse.CauHoiDTO chDto = new ExamContentResponse.CauHoiDTO();
                    chDto.setId(cauHoi.getId());
                    chDto.setNoiDung(cauHoi.getNoiDung());
                    chDto.setDiem(cauHoi.getDiem().doubleValue());
                    chDto.setThuTu(cauHoi.getThuTu());
                    chDto.setMucDo(cauHoi.getMucDo().name());
                    chDto.setDeId(de.getId());

                    // Map lựa chọn
                    List<LuaChon> luaChons = luaChonRepository.findByCauHoiId(cauHoi.getId());
                    List<ExamContentResponse.LuaChonDTO> luaChonDTOs = luaChons.stream()
                            .map(lc -> {
                                ExamContentResponse.LuaChonDTO lcDto = new ExamContentResponse.LuaChonDTO();
                                lcDto.setId(lc.getId());
                                lcDto.setKyHieu(lc.getKyHieu());
                                lcDto.setNoiDung(lc.getNoiDung());
                                lcDto.setLaDapAn(lc.getLaDapAn());
                                lcDto.setCauHoiId(cauHoi.getId());
                                return lcDto;
                            })
                            .collect(Collectors.toList());

                    chDto.setLuaChons(luaChonDTOs);
                    return chDto;
                })
                .collect(Collectors.toList());

        response.setCauHois(cauHoiDTOs);
        return response;
    }

    // POST /create_exam/{id}/publish
    @Transactional
    public ExamConfigResponse publish(Integer deId, String username, PublishExamRequest request) {

        De de = deRepository.findById(deId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề: " + deId));

        // Kiểm tra quyền
        if (!de.getNguoiTao().getTenDangNhap().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xuất bản đề này");
        }

        // Cập nhật thông tin cấu hình
        if (request.getTieuDe() != null)
            de.setTieuDe(request.getTieuDe());
        if (request.getThoiGian() != null)
            de.setThoiGian(request.getThoiGian());
        de.setBatDau(request.getBatDau());
        de.setKetThuc(request.getKetThuc());
        de.setGioiHanNop(request.getGioiHanNop());
        de.setDaXuatBan(true);

        deRepository.save(de);

        // Đồng bộ danh sách lớp được giao
        // Xóa hết lớp cũ
        List<GiaoChoLop> lopCu = giaoChoLopRepository.findByDeId(deId);
        giaoChoLopRepository.deleteAll(lopCu);

        // Thêm lại theo danh sách mới
        List<Integer> lopMoiIds = request.getCacLopDaGiao();
        if (lopMoiIds != null) {
            for (Integer lopId : lopMoiIds) {
                LopHoc lop = lopHocRepository.findById(lopId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp: " + lopId));
                GiaoChoLop gcl = new GiaoChoLop();
                gcl.setDe(de);
                gcl.setLopHoc(lop);
                giaoChoLopRepository.save(gcl);
            }
        }

        // Trả về config mới nhất (tái dụng getConfig)
        return getConfig(deId, username);
    }

    public List<LopHocRef> getTatCaLopCuaGV(String username) {
        List<LopHoc> danhSachLop = lopHocRepository
                .findByGiaoVien_NguoiDung_TenDangNhap(username);

        return danhSachLop.stream()
                .map(lop -> {
                    LopHocRef ref = new LopHocRef();
                    ref.setId(lop.getId());
                    ref.setTenLop(lop.getTenLop());
                    ref.setMaLop(lop.getMaLop());
                    ref.setNamHoc(lop.getNamHoc());
                    return ref;
                })
                .collect(Collectors.toList());
    }
}