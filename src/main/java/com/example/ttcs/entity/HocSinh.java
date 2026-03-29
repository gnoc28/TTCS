package com.example.ttcs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hoc_sinh")
public class HocSinh {
    @Id
    private Integer id;

    @Column(name = "ma_HS", unique = true)
    private String maHS;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private NguoiDung nguoiDung;
}


