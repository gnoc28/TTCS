package com.example.ttcs.service;

import org.springframework.stereotype.Service;

@Service
public class FeedbackGenerationService {

    // 1. DÀNH CHO HỌC SINH (Khi xem kết quả cá nhân)
    public String generateSystemFeedback(double percentNB, double percentTH, double percentVD, double percentVDC) {
        StringBuilder feedback = new StringBuilder();

        // --- NHẬN XÉT CHI TIẾT ---
        if (percentNB != -1) {
            feedback.append("- Nhận biết: ");
            if (percentNB < 50)
                feedback.append("Hổng kiến thức cơ bản. Cần học lại lý thuyết ngay.\n");
            else if (percentNB < 80)
                feedback.append("Nhớ lý thuyết nhưng chưa chắc chắn, dễ nhầm lẫn.\n");
            else
                feedback.append("Nắm vững kiến thức nền tảng.\n");
        }

        if (percentTH != -1) {
            feedback.append("- Thông hiểu: ");
            if (percentTH < 50)
                feedback.append("Chưa hiểu bản chất, đang học vẹt.\n");
            else if (percentTH < 80)
                feedback.append("Hiểu bài khá, cần phân tích cẩn thận hơn để tránh bị lừa.\n");
            else
                feedback.append("Hiểu rất rõ bản chất vấn đề.\n");
        }

        if (percentVD != -1) {
            feedback.append("- Vận dụng: ");
            if (percentVD < 50)
                feedback.append("Yếu kỹ năng giải bài tập. Cần xem lại các ví dụ mẫu.\n");
            else if (percentVD < 80)
                feedback.append("Áp dụng công thức khá tốt, cần tăng tốc độ làm bài.\n");
            else
                feedback.append("Kỹ năng thực hành thành thạo, chính xác.\n");
        }

        if (percentVDC != -1) {
            feedback.append("- Vận dụng cao: ");
            if (percentVDC < 50)
                feedback.append("Chưa làm được câu phân loại. Hãy ưu tiên ăn chắc điểm dễ trước.\n");
            else if (percentVDC < 80)
                feedback.append("Tư duy tốt nhưng thiếu kinh nghiệm xử lý bài toán phức tạp.\n");
            else
                feedback.append("Tư duy logic xuất sắc, giải quyết vấn đề linh hoạt.\n");
        }

        // --- CHIẾN THUẬT TỔNG QUAN (GIAO THOA) ---
        feedback.append("\n💡 CHIẾN THUẬT ÔN TẬP:\n");

        double nb = percentNB == -1 ? 100 : percentNB;
        double th = percentTH == -1 ? 100 : percentTH;
        double vd = percentVD == -1 ? 100 : percentVD;
        double vdc = percentVDC == -1 ? 100 : percentVDC;

        if (nb < 50 && th < 50 && vd < 50) {
            feedback.append(
                    "=> Báo động đỏ: Kiến thức hổng toàn diện. Dừng ngay việc giải đề, nhờ giáo viên giảng lại lý thuyết từ đầu.");
        } else if (nb < 50 && (vd >= 50 || vdc >= 50)) {
            feedback.append(
                    "=> Lỗi học vẹt: Làm được bài tập nhờ nhớ mẹo nhưng sai lý thuyết cơ bản. Cần đọc lại SGK để không mất điểm oan.");
        } else if (nb >= 80 && th >= 80 && vd < 50) {
            feedback.append(
                    "=> Thiếu thực hành: Nắm chắc lý thuyết nhưng chưa biết áp dụng. Cần làm lại thật nhiều các bài tập mẫu.");
        } else if (nb >= 80 && th >= 80 && vd >= 50 && vdc < 50) {
            feedback.append(
                    "=> Khá giỏi nhưng thiếu cọ xát: Nền tảng rất chắc, để đạt điểm tuyệt đối cần luyện thêm các dạng bài toán khó.");
        } else if (nb >= 80 && th >= 80 && vd >= 80) {
            feedback.append(
                    "=> Phong độ xuất sắc: Nắm chắc cả lý thuyết lẫn thực hành. Tiếp tục duy trì cách học này!");
        } else {
            feedback.append(
                    "=> Đang ở mức an toàn: Lực học tương đối đều nhưng chưa bứt phá. Cần rèn luyện thêm để khắc phục các lỗi sai sót lặt vặt.");
        }

        return feedback.toString();
    }
    
    // 2. DÀNH CHO GIÁO VIÊN (Phân tích lớp học - Có Giao Thoa Móc Nối)
    public String generateTeacherAdvice(double avgNB, double avgTH, double avgVD, double avgVDC) {
        StringBuilder advice = new StringBuilder();

        // --- ĐÁNH GIÁ CHUNG TỪNG PHẦN ---
        // --- ĐÁNH GIÁ CHUNG TỪNG PHẦN ---
        advice.append("📊 THỐNG KÊ NHANH TÌNH HÌNH LỚP:\n");
        
        if (avgNB != -1) {
            advice.append("- Nhận biết: ");
            if (avgNB < 50) advice.append("Tỷ lệ sai kiến thức cơ bản còn cao.\n");
            else if (avgNB < 80) advice.append("Đa số nắm được cơ bản nhưng đôi chỗ chưa thực sự chắc chắn.\n");
            else advice.append("Rất tốt, lớp nắm cực kỳ vững kiến thức nền tảng.\n");
        }

        if (avgTH != -1) {
            advice.append("- Thông hiểu: ");
            if (avgTH < 50) advice.append("Nhiều em gặp khó khăn trong việc hiểu bản chất.\n");
            else if (avgTH < 80) advice.append("Khả năng phân tích và hiểu câu hỏi ở mức khá.\n");
            else advice.append("Lớp hiểu sâu và phân tích vấn đề rất mạch lạc.\n");
        }

        if (avgVD != -1) {
            advice.append("- Vận dụng: ");
            if (avgVD < 50) advice.append("Kỹ năng xử lý bài tập thực hành chưa đạt kỳ vọng.\n");
            else if (avgVD < 80) advice.append("Đã biết cách áp dụng công thức vào giải bài tập.\n");
            else advice.append("Kỹ năng giải bài tập của tập thể rất đồng đều và chính xác.\n");
        }

        if (avgVDC != -1) {
            advice.append("- Vận dụng cao: ");
            if (avgVDC < 30) advice.append("Nhóm học sinh có khả năng giải câu khó còn mỏng.\n");
            else if (avgVDC < 60) advice.append("Một bộ phận học sinh đã bắt đầu làm được các câu phân loại.\n");
            else advice.append("Tuyệt vời, nhiều em có tư duy đột phá, giải tốt câu hỏi khó.\n");
        }

        // --- TƯ VẤN SƯ PHẠM TỔNG QUAN (GIAO THOA) ---
        advice.append("\n💡 TƯ VẤN CHIẾN THUẬT GIẢNG DẠY:\n");

        // Xử lý trường hợp đề không có mức độ nào đó
        double nb = avgNB == -1 ? 100 : avgNB;
        double th = avgTH == -1 ? 100 : avgTH;
        double vd = avgVD == -1 ? 100 : avgVD;
        double vdc = avgVDC == -1 ? 100 : avgVDC;

        if (nb < 50 && th < 50 && vd < 50) {
            // Kịch bản 1: Lớp hổng toàn diện
            advice.append(
                    "=> Báo động đỏ: Chất lượng làm bài của lớp ở mức rất thấp. Thầy/Cô nên cân nhắc tạm dừng việc dạy bài mới để ôn tập lại toàn bộ kiến thức bài này bằng sơ đồ tư duy (Mindmap) hoặc ví dụ trực quan hơn.");
        } else if (nb < 50 && (vd >= 50 || vdc >= 50)) {
            // Kịch bản 2: Lớp có hiện tượng học vẹt công thức
            advice.append(
                    "=> Lỗ hổng gốc: Lớp đang giải bài tập dựa trên việc nhớ vẹt công thức/mẹo mà quên sạch định nghĩa cơ bản. Thầy/Cô cần dành 15 phút đầu giờ kiểm tra miệng lý thuyết để uốn nắn ngay.");
        } else if (nb >= 70 && th >= 70 && vd < 50) {
            // Kịch bản 3: Tốt lý thuyết nhưng yếu bài tập
            advice.append(
                    "=> Thiếu kỹ năng thực hành: Lớp thuộc lý thuyết nhưng lúng túng khi bắt tay vào làm bài. Đề xuất: Thầy/Cô nên chữa chậm lại các bài tập mẫu trên bảng và yêu cầu lớp làm bài tập tương tự ngay tại lớp để sửa lỗi trực tiếp.");
        } else if (nb >= 70 && th >= 70 && vd >= 60 && vdc < 40) {
            // Kịch bản 4: Phân hóa bình thường, đại trà tốt nhưng thiếu mũi nhọn
            advice.append(
                    "=> Cần phân hóa: Kiến thức đại trà của lớp rất ổn, nhưng phần lớn 'bó tay' trước câu khó. Đề xuất: Phân tách nhóm khá giỏi để giao thêm bài nâng cao riêng, nhóm còn lại tiếp tục ôn chắc cơ bản để tránh áp lực.");
        } else if (nb >= 80 && th >= 80 && vd >= 70) {
            // Kịch bản 5: Lớp xuất sắc
            advice.append(
                    "=> Lớp học chất lượng cao: Mặt bằng chung cực kỳ đồng đều và xuất sắc. Thầy/Cô hoàn toàn có thể tăng tốc độ giảng dạy và đẩy mạnh tỷ lệ câu hỏi tư duy (Vận dụng cao) trong các bài kiểm tra sắp tới.");
        } else {
            // Kịch bản 6: Lực học làng nhàng
            advice.append(
                    "=> Nhịp độ ổn định: Tình hình học tập của lớp đang ở mức an toàn nhưng chưa có điểm nhấn. Thầy/Cô cứ duy trì nhịp giảng dạy hiện tại, kết hợp gọi các em điểm thấp lên bảng làm bài để kéo mặt bằng chung.");
        }

        return advice.toString();
    }
}