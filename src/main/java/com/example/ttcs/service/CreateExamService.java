package com.example.ttcs.service;

import com.example.ttcs.dto.CauHoiDto;
import com.example.ttcs.dto.CreateExamRequest;
import com.example.ttcs.dto.LuaChonDto;
import com.example.ttcs.dto.PreviewExamDTO;
import com.example.ttcs.entity.CauHoi;
import com.example.ttcs.entity.De;
import com.example.ttcs.entity.LuaChon;
import com.example.ttcs.entity.NguoiDung;
import com.example.ttcs.enums.MucDo;
import com.example.ttcs.enums.PhamViGiao;
import com.example.ttcs.enums.VaiTro;
import com.example.ttcs.repository.CauHoiRepository;
import com.example.ttcs.repository.ChiTietKetQuaRepository;
import com.example.ttcs.repository.DeRepository;
import com.example.ttcs.repository.GiaoChoLopRepository;
import com.example.ttcs.repository.KetQuaRepository;
import com.example.ttcs.repository.LuaChonRepository;
import com.example.ttcs.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateExamService {

    private final DeRepository deRepository;
    private final CauHoiRepository cauHoiRepository;
    private final LuaChonRepository luaChonRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final ChiTietKetQuaRepository chiTietKetQuaRepository;
    private final KetQuaRepository ketQuaRepository;
    private final GiaoChoLopRepository giaoChoLopRepository;

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    // GV + HS đều được tạo đề
    @PreAuthorize("hasAnyRole('GV','HS')")
    @Transactional
    public Integer createExam(CreateExamRequest request, String username) {
        NguoiDung nguoiTao = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        PhamViGiao phamViGiao = nguoiTao.getVaiTro() == VaiTro.GV
                ? PhamViGiao.LOP
                : PhamViGiao.TU;

        String maHash = generateUniqueHash();

        De de = new De();
        de.setMaHash(maHash);
        de.setTieuDe(request.getTieuDe());
        de.setNguoiTao(nguoiTao);
        de.setPhamViGiao(phamViGiao);
        de.setDaXuatBan(false);

        De savedDe = deRepository.save(de);

        Map<String, CauHoiDto> cauHois = request.getNoiDungDe().getCauHois();
        for (CauHoiDto cauHoiDto : cauHois.values()) {
            saveCauHoi(cauHoiDto, savedDe);
        }

        return savedDe.getId();
    }

    private void saveCauHoi(CauHoiDto dto, De de) {
        CauHoi cauHoi = new CauHoi();
        cauHoi.setNoiDung(dto.getNoiDung());
        cauHoi.setDiem(BigDecimal.valueOf(dto.getDiem()));
        cauHoi.setMucDo(MucDo.valueOf(dto.getMucDo()));
        cauHoi.setThuTu(dto.getThuTu());
        cauHoi.setDe(de);

        CauHoi savedCauHoi = cauHoiRepository.save(cauHoi);

        if (dto.getLuaChons() != null) {
            for (LuaChonDto lcDto : dto.getLuaChons().values()) {
                saveLuaChon(lcDto, savedCauHoi);
            }
        }
    }

    private void saveLuaChon(LuaChonDto dto, CauHoi cauHoi) {
        LuaChon luaChon = new LuaChon();
        luaChon.setKyHieu(dto.getKyHieu());
        luaChon.setNoiDung(dto.getNoiDung());
        luaChon.setLaDapAn(dto.isLaDapAn());
        luaChon.setCauHoi(cauHoi);

        luaChonRepository.save(luaChon);
    }

    private String generateUniqueHash() {
        String hash;
        do {
            hash = generateHash(6);
        } while (deRepository.existsByMaHash(hash));
        return hash;
    }

    private String generateHash(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    // GV + HS đều xem danh sách đề của mình tạo
    @PreAuthorize("hasAnyRole('GV','HS')")
    public List<PreviewExamDTO> getPreviewsExamList(String username) {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username).orElseThrow();
        List<De> danhSachDe = deRepository
                .findByNguoiTaoIdOrderByUpdatedAtDesc(nguoiDung.getId());

        return danhSachDe.stream()
                .map(this::toPreviewDTO)
                .collect(Collectors.toList());
    }

    private PreviewExamDTO toPreviewDTO(De de) {
        PreviewExamDTO dto = new PreviewExamDTO();
        dto.setId(de.getId());
        dto.setMaHash(de.getMaHash());
        dto.setTieuDe(de.getTieuDe());
        dto.setPhamViGiao(de.getPhamViGiao() != null ? de.getPhamViGiao().name() : null);
        dto.setThoiGian(de.getThoiGian());
        dto.setBatDau(de.getBatDau());
        dto.setKetThuc(de.getKetThuc());
        dto.setGioiHanNop(de.getGioiHanNop());
        dto.setDaXuatBan(de.getDaXuatBan());
        dto.setCreatedAt(de.getCreatedAt());
        dto.setUpdatedAt(de.getUpdatedAt());

        PreviewExamDTO.NguoiTaoDTO nguoiTaoDTO = new PreviewExamDTO.NguoiTaoDTO();
        nguoiTaoDTO.setId(de.getNguoiTao().getId());
        nguoiTaoDTO.setVaiTro(de.getNguoiTao().getVaiTro().name());
        dto.setNguoiTao(nguoiTaoDTO);

        return dto;
    }

    // GV + HS đều có thể xóa NHƯNG phải là owner
    @PreAuthorize("hasAnyRole('GV','HS')")
    @Transactional
    public void deleteExam(Integer deId, String username) {
        De de = deRepository.findById(deId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề: " + deId));

        if (!de.getNguoiTao().getTenDangNhap().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xóa đề này");
        }

        // deRepository.delete(de);

        // 1. Xóa ChiTietKetQua (tham chiếu đến cả KetQua lẫn CauHoi)
        chiTietKetQuaRepository.deleteByCauHoiDeId(deId);

        // 2. Xóa KetQua
        ketQuaRepository.deleteByDeId(deId);

        // 3. Xóa GiaoChoLop
        giaoChoLopRepository.deleteByDeId(deId);

        // 4. Xóa LuaChon (con của CauHoi)
        luaChonRepository.deleteByCauHoiDeId(deId);

        // 5. Xóa CauHoi
        cauHoiRepository.deleteByDeId(deId);

        // 6. Xóa De
        deRepository.deleteById(deId);
    }
}