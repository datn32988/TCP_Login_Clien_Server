<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   Đăng nhập Client và Server
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

🎨 Giao Diện Người Dùng
- Java Swing: Thư viện GUI cho desktop applications
- JFrame: Container chính cho các cửa sổ
- JPanel: Quản lý layout và components
- Swing Components: JButton, JTextField, JPasswordField, JTable
- Event Handling: ActionListener, MouseListener

🌐 Truyền Thông Mạng
- TCP/IP Protocol: Giao thức truyền thông tin cậy
- Socket Programming: Java Socket và ServerSocket
- Port: 12345 cho kết nối client-server
- Object Streams: ObjectInputStream/ObjectOutputStream cho truyền dữ liệu

🗄️ Cơ Sở Dữ Liệu
- PostgreSQL 14+: Hệ quản trị cơ sở dữ liệu quan hệ
- JDBC Driver: postgresql-42.6.0.jar
- Database Schema: Quản lý users với các trường cần thiết
- SQL Operations: SELECT, INSERT, UPDATE, DELETE

🔄 Xử Lý Đa Luồng
- Java Multithreading: Xử lý nhiều client đồng thời
- Thread: Mỗi client kết nối được xử lý trong thread riêng
- Synchronization: Đảm bảo thread-safe khi truy cập database
## 🚀 3. Hình ảnh chức năng

<p align="center">
  <img src="doc/admin.png" alt="Ảnh 1" width="600"/>
</p>

<p align="center">
  <em>Hình 1: Giao diện Admin </em>
</p>

<p align="center">
  <img src="doc/dangky.png" alt="Ảnh 2" width="600"/>
</p>
<p align="center">
  <em> Hình 2: Giao diện Đăng ký</em>
</p>


<p align="center">
  <img src="doc/login.png" alt="Ảnh 3" width="600"/>
 
</p>
<p align="center">
  <em> Hình 3: Giao diện đăng nhập </em>
</p>

<p align="center">
    <img src="doc/profile.png" alt="Ảnh 4" width="600"/>
</p>
<p align="center">
  <em> Hình 4: Giao diện chính người dùng</em>
</p>

<p align="center">
  <img src="doc/password.png" alt="Ảnh 5" width="600"/>
</p>
<p align="center">
  <em> Hình 5: Giao diện đổi mật khẩu</em>
</p>

<p align="center">
  <img src="doc/updateprofile.png" alt="Ảnh 6" width="600"/>
</p>
<p align="center">
  <em> Hình 6: Giao diện thay đổi thông tin cá nhân</em>
</p>
 
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

📧 Email: datn32908@gmail.com
