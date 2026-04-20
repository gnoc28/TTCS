package com.example.ttcs.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exam_results")
@Data // Dùng Lombok để tự tạo Getter/Setter
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "total_score")
    private Double totalScore;

    // Lưu đoạn văn bản nhận xét tự động của hệ thống
    @Column(name = "system_feedback", columnDefinition = "TEXT")
    private String systemFeedback;

    // Cho phép null, lưu lời nhận xét riêng của giáo viên
    @Column(name = "teacher_feedback", columnDefinition = "TEXT")
    private String teacherFeedback;
}