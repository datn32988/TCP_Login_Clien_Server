package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterGUI extends JDialog {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JButton btnRegister;
    private JButton btnCancel;
    private LoginClient client;
    
    public RegisterGUI(JFrame parent, LoginClient client) {
        super(parent, "Đăng ký tài khoản", true);
        this.client = client;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 102, 204));
        
        // Panel form đăng ký
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        txtUsername = new JTextField();
        
        JLabel lblPassword = new JLabel("Mật khẩu:");
        txtPassword = new JPasswordField();
        
        JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
        txtConfirmPassword = new JPasswordField();
        
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField();
        
        JLabel lblFullName = new JLabel("Họ và tên:");
        txtFullName = new JTextField();
        
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(lblConfirmPassword);
        formPanel.add(txtConfirmPassword);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblFullName);
        formPanel.add(txtFullName);
        
        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnRegister = new JButton("Đăng ký");
        btnCancel = new JButton("Hủy");
        
        btnRegister.setBackground(new Color(0, 153, 76));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setPreferredSize(new Dimension(100, 30));
        
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Xử lý sự kiện
        btnRegister.addActionListener(e -> performRegister());
        btnCancel.addActionListener(e -> dispose());
        
        // Enter để đăng ký
        txtFullName.addActionListener(e -> performRegister());
    }
    
    private void performRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText().trim();
        String fullName = txtFullName.getText().trim();
        
        // Validate dữ liệu
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp");
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }
        
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ");
            return;
        }
        
        // Kết nối đến server
        if (!client.connectToServer("localhost", 12345)) {
            return;
        }
        
        // Thực hiện đăng ký
        String response = client.register(username, password, email, fullName);
        
        switch (response) {
            case "REGISTER_SUCCESS":
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                dispose();
                break;
            case "USERNAME_EXISTS":
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại");
                break;
            case "EMAIL_EXISTS":
                JOptionPane.showMessageDialog(this, "Email đã được sử dụng");
                break;
            case "REGISTER_FAIL":
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Lỗi kết nối server");
        }
    }
}