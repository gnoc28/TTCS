package com.example.ttcs.service;
import org.springframework.stereotype.Service;

@Service
public class FeedbackGenerationService {

    public String generateSystemFeedback(double thongHieuPercent, double vanDungPercent, double vanDungCaoPercent) {
        StringBuilder feedback = new StringBuilder();

        // 1. Nhận xét phần Thông hiểu
        feedback.append("📌 Thông hiểu: ");
        if (thongHieuPercent < 50) {
            feedback.append("Bạn đang hổng khá nhiều kiến thức cơ bản. Cần ưu tiên đọc lại sách giáo khoa và nắm chắc lý thuyết.\n");
        } else if (thongHieuPercent < 80) {
            feedback.append("Bạn nắm lý thuyết ở mức trung bình khá nhưng vẫn còn nhầm lẫn. Hãy ôn tập lại các định nghĩa.\n");
        } else {
            feedback.append("Tuyệt vời! Bạn nắm rất vững kiến thức nền tảng. Hãy duy trì phong độ này nhé.\n");
        }

        // 2. Nhận xét phần Vận dụng
        feedback.append("📌 Vận dụng: ");
        if (vanDungPercent < 50) {
            feedback.append("Bạn đang gặp khó khăn khi áp dụng lý thuyết vào giải bài tập. Hãy xem lại các ví dụ mẫu.\n");
        } else if (vanDungPercent < 80) {
            feedback.append("Kỹ năng giải bài tập khá ổn. Tuy nhiên, cần rèn luyện thêm nhiều dạng bài để làm nhanh hơn.\n");
        } else {
            feedback.append("Bạn có kỹ năng áp dụng công thức vào thực hành rất tốt. Cách giải quyết vấn đề mạch lạc.\n");
        }

        // 3. Nhận xét phần Vận dụng cao
        feedback.append("📌 Vận dụng cao: ");
        if (vanDungCaoPercent < 50) {
            feedback.append("Đây là nhóm câu hỏi khó, bạn không nên nản lòng, hãy tập trung hoàn thiện phần cơ bản trước.\n");
        } else if (vanDungCaoPercent < 80) {
            feedback.append("Bạn có tư duy tốt nhưng chưa quen với bài toán nhiều bước. Cần làm thêm đề thi nâng cao.\n");
        } else {
            feedback.append("Xuất sắc! Tư duy logic và xử lý tình huống khó của bạn rất đáng nể.\n");
        }

        return feedback.toString();
    }
}