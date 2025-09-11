<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   TCP_Login_Client_Server
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
Đề tài "TCP_Login_Client_Server" tập trung vào việc xây dựng một hệ thống đăng nhập cơ bản, sử dụng giao thức TCP (Transmission Control Protocol). Giao thức này được chọn bởi tính chất hướng kết nối và đảm bảo độ tin cậy cao, đảm bảo các gói tin được truyền đến đích một cách tuần tự và đầy đủ, rất phù hợp cho các tác vụ nhạy cảm như truyền thông tin tài khoản và mật khẩu.

Mục tiêu chính của đề tài là:

Phát triển một ứng dụng Client: Cho phép người dùng nhập thông tin đăng nhập (tên người dùng và mật khẩu) và gửi yêu cầu này đến máy chủ.

Xây dựng một Server: Lắng nghe các kết nối từ client, tiếp nhận yêu cầu đăng nhập, xử lý xác thực bằng cách so sánh với dữ liệu đã lưu trữ, và phản hồi kết quả về cho client.

Áp dụng giao thức TCP: Hiểu và sử dụng các cơ chế của TCP để thiết lập, duy trì và đóng kết nối một cách an toàn, đảm bảo dữ liệu không bị thất lạc trong quá trình truyền.

Thông qua việc triển khai đề tài này, người thực hiện sẽ có cái nhìn sâu sắc về cách thức hoạt động của mô hình Client-Server, nắm vững quy trình giao tiếp mạng cơ bản và củng cố kiến thức về giao thức TCP trong việc xây dựng các ứng dụng mạng tin cậy. Đây là nền tảng quan trọng để phát triển các hệ thống phân tán và ứng dụng web phức tạp hơn trong tương lai
## 🔧 2. Ngôn ngữ lập trình sử dụng: [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)

## 🚀 3. Công nghệ sử dụng
1. Ngôn ngữ lập trình: Java ☕
Java là ngôn ngữ lập trình đa nền tảng, mạnh mẽ và phổ biến. Nó được sử dụng để phát triển cả ứng dụng Client và Server.

Ứng dụng Client (Frontend): Có thể sử dụng Java Swing hoặc JavaFX để xây dựng giao diện người dùng (GUI) cho phép người dùng nhập thông tin đăng nhập.

Ứng dụng Server (Backend): Xử lý các yêu cầu từ Client, thực hiện logic nghiệp vụ như xác thực người dùng, và tương tác với cơ sở dữ liệu.

2. Cơ sở dữ liệu: PostgreSQL 🐘
PostgreSQL là một hệ quản trị cơ sở dữ liệu quan hệ (RDBMS) mã nguồn mở, nổi tiếng về độ tin cậy, tính toàn vẹn dữ liệu và hiệu suất cao. Nó sẽ được sử dụng để lưu trữ thông tin người dùng, bao gồm tên đăng nhập và mật khẩu (thường được lưu dưới dạng băm).

3. Công nghệ kết nối cơ sở dữ liệu: JDBC 🔗
Để ứng dụng Java giao tiếp với cơ sở dữ liệu PostgreSQL, bạn cần sử dụng JDBC (Java Database Connectivity). Đây là một API tiêu chuẩn của Java, cung cấp các lớp và giao diện để kết nối đến cơ sở dữ liệu, gửi các câu lệnh SQL và xử lý kết quả trả về.

Các thành phần chính của JDBC cần sử dụng:

JDBC Driver (Trình điều khiển): Một thư viện (file .jar) cụ thể cho PostgreSQL, cho phép ứng dụng Java hiểu và giao tiếp được với cơ sở dữ liệu này. Ví dụ: postgresql-42.2.x.jar.

Connection: Một đối tượng để thiết lập và duy trì kết nối vật lý đến cơ sở dữ liệu.

Statement / PreparedStatement: Các đối tượng dùng để thực thi các câu lệnh SQL. PreparedStatement được ưu tiên sử dụng để ngăn chặn các cuộc tấn công SQL Injection.

ResultSet: Một đối tượng lưu trữ kết quả trả về từ một câu lệnh truy vấn (SELECT).
## 📝 4. Hình ảnh chức năng

## 5. Các bước cài đặt
### 1. Bước 1:
### 2. Bước 2:
### 3. Bước 3:
