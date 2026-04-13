// package com.example.ttcs.dto;

// public class JwtResponse {
//     private String token;
//     private String type = "Bearer";

//     public JwtResponse(String token) {
//         this.token = token;
//     }

//     // Các hàm Getter/Setter cực kỳ quan trọng để gửi dữ liệu đi
//     public String getToken() {
//         return token;
//     }

//     public void setToken(String token) {
//         this.token = token;
//     }

//     public String getType() {
//         return type;
//     }

//     public void setType(String type) {
//         this.type = type;
//     }
// }

package com.example.ttcs.dto;

public class JwtResponse {
    private String accessToken; // Đổi tên thành accessToken cho chuẩn với React
    private String type = "Bearer";
    private UserInfo user; // Đóng gói thông tin người dùng vào đây

    // Constructor mới: Nhận cả Token lẫn Thông tin User
    public JwtResponse(String accessToken, Integer id, String tenDangNhap, String vaiTro) {
        this.accessToken = accessToken;
        this.user = new UserInfo(id, tenDangNhap, vaiTro);
    }

    // --- Getters / Setters cho JwtResponse ---
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    // LỚP : Tạo ra cấu trúc "user": { ... }
    public static class UserInfo {
        private Integer id;
        private String tenDangNhap;
        private String vaiTro;

        public UserInfo(Integer id, String tenDangNhap, String vaiTro) {
            this.id = id;
            this.tenDangNhap = tenDangNhap;
            this.vaiTro = vaiTro;
        }

        // --- Getters / Setters cho UserInfo ---
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTenDangNhap() {
            return tenDangNhap;
        }

        public void setTenDangNhap(String tenDangNhap) {
            this.tenDangNhap = tenDangNhap;
        }

        public String getVaiTro() {
            return vaiTro;
        }

        public void setVaiTro(String vaiTro) {
            this.vaiTro = vaiTro;
        }
    }
}