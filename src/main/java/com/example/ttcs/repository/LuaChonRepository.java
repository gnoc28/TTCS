package com.example.ttcs.repository;

import com.example.ttcs.entity.CauHoi;
import com.example.ttcs.entity.LuaChon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LuaChonRepository extends JpaRepository<LuaChon, Integer> {
    List<LuaChon> findByCauHoiId(Integer cauHoiId);
    LuaChon findByCauHoiAndLaDapAnTrue(CauHoi cauHoi);
    @Query("SELECT lc FROM LuaChon lc WHERE lc.cauHoi.id IN :cauHoiIds")
    List<LuaChon> findByCauHoiIds(List<Integer> cauHoiIds);
    
    void deleteByCauHoiDeId(Integer deId);
}