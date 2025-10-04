<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   ĐĂNG NHẬP CLIENT VÀ SERVER
</h2>
<div align="center">
    <p align="center">
        <img src="doc/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="doc/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="doc/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 📖 1. Giới thiệu
<b>Hệ thống đăng nhập client-server </b>là một giải pháp phần mềm hiện đại được thiết kế để quản lý xác thực người dùng thông qua giao thức mạng TCP. Hệ thống này cung cấp một nền tảng bảo mật và ổn định cho việc đăng ký, đăng nhập và quản lý tài khoản người dùng.

Đề tài tập trung vào việc xây dựng một kiến trúc phân tán theo mô hình client-server, nơi máy chủ đóng vai trò trung tâm trong việc xử lý logic nghiệp vụ, quản lý cơ sở dữ liệu và đảm bảo tính bảo mật, trong khi client cung cấp giao diện người dùng trực quan và thân thiện.
📊 Mục Tiêu Đề Tài
 - Xây dựng hệ thống đăng nhập phân tán sử dụng mô hình client-server

- Triển khai giao thức TCP cho truyền thông mạng đáng tin cậy

- Phát triển giao diện đồ họa với Java Swing

- Kết nối cơ sở dữ liệu PostgreSQL để lưu trữ thông tin người dùng

- Đảm bảo tính bảo mật trong quá trình xác thực

Cung cấp công cụ quản lý cho administrator
## 🔧 2. Ngôn ngữ lập trình sử dụng: [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
🌐 Ngôn Ngữ Lập Trình
- Java SE 17+: Ngôn ngữ lập trình chính
- Tính năng: Hướng đối tượng, đa luồng, networking
- Ứng dụng: Viết toàn bộ client, server và business logic

🗄️ Cơ Sở Dữ Liệu
- PostgreSQL 14+: Hệ quản trị cơ sở dữ liệu quan hệ
- JDBC Driver: postgresql-42.6.0.jar
- Database Schema: Quản lý users với các trường cần thiết
- SQL Operations: SELECT, INSERT, UPDATE, DELETE
## 🚀 3. Hình ảnh chức năng
<h2 align="center">📸 Giao diện hệ thống</h2>

<table align="center">
  <tr>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204000.png" width="400"/><br/>
      <em>Hình 1: Chức năng Đăng nhập</em>
    </td>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204439.png" width="400"/><br/>
      <em>Hình 2: Giao diện User</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204447.png" width="400"/><br/>
      <em>Hình 3: Chức năng đổi mật khẩu</em>
    </td>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204456.png" width="400"/><br/>
      <em>Hình 4: Chức năng cập nhập profile</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204506.png" width="400"/><br/>
      <em>Hình 5: Chức năng lịch sử đăng nhập của bạn</em>
    </td>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204545.png" width="400"/><br/>
      <em>Hình 6: Chức năng đăng ký </em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204610.png" width="400"/><br/>
      <em>Hình 7: Giao diện Admin</em>
    </td>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204620.png" width="400"/><br/>
      <em>Hình 8: Chức năng quản lý người dùng</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204628.png" width="400"/><br/>
      <em>Hình 9: Chức năng quản lý đăng nhập</em>
    </td>
    <td align="center">
      <img src="doc/Screenshot 2025-10-04 204636.png" width="400"/><br/>
      <em>Hình 10: Chức năng thêm User mới</em>
    </td>
  </tr>
  
</table>

 
## 📝 4. Các bước cài đặt 

### 🔹 Bước 1: Cài đặt phần mềm cần thiết
- **Java Development Kit (JDK) 8+)**  
  - Tải từ https://www.oracle.com/java/technologies/javase-downloads.html hoặc https://jdk.java.net/  
  - Kiểm tra cài đặt:  
    ```bash
    java -version
    javac -version
    ```
- **PostgreSQL 12+**  
  - Tải từ https://www.postgresql.org/download/  
  - Cài đặt với cấu hình mặc định  
- **PostgreSQL JDBC Driver**  
  - Phiên bản khuyến nghị: **postgresql-42.6.0.jar**  
  - Tải tại https://jdbc.postgresql.org/download/  
  - Đặt file JAR cùng thư mục với source code  

---

### 🔹 Bước 2: Biên dịch source code

#### 🖥️ Trên Windows
```bash
javac -cp .;postgresql-42.6.0.jar Login/*.java
```
💻 Trên Linux/Mac
```bash
javac -cp .:postgresql-42.6.0.jar Login/*.java
```
🔹 Bước 3: Khởi động Server Admin
🖥️ Trên Windows
```bash
java -cp .;postgresql-42.6.0.jar Login.ServerAdminGUI
```
💻 Trên Linux/Mac
```bash
java -cp .:postgresql-42.6.0.jar Login.ServerAdminGUI
```
🔹 Bước 4: Khởi động Client
🖥️ Trên Windows
```bash
java -cp .;postgresql-42.6.0.jar Login.LoginGUI
```
💻 Trên Linux/Mac
```bash
java -cp .:postgresql-42.6.0.jar Login.LoginGUI
```

## 5. Liên hệ 
Nếu có bất kỳ thắc mắc hoặc cần hỗ trợ, vui lòng liên hệ:
<b>
- Họ và Tên: Nguyễn Duy Đạt
- Lớp: CNTT 16-03
- Email: datn32908@gmail.com
</b>