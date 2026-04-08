package com.example.ttcs.repository;

import com.example.ttcs.entity.HocSinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // 🚀 Phải import thêm cái này

@Repository
public interface HocSinhRepository extends JpaRepository<HocSinh, Integer> {
    
    // 🚀 THÊM HÀM NÀY: Để tìm ID học sinh từ cái chữ "HS005"
    Optional<HocSinh> findByMaHS(String maHS); 
    
    /* ⚠️ LƯU Ý NHỎ: 
       Nếu trong file Entity HocSinh.java của ông, cái biến chứa mã học sinh 
       ông đặt tên là "maHocSinh" (thay vì "maHs"), thì ông phải đổi tên hàm 
       trên kia thành: Optional<HocSinh> findByMaHocSinh(String maHocSinh); 
       Spring Boot nó tự động map theo đúng tên biến ông đặt nhé! 
    */
}