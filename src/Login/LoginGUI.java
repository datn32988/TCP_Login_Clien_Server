package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnExit;
    private LoginClient client;
    
    public LoginGUI() {
        client = new LoginClient();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204));
        
        // Panel form đăng nhập
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        txtUsername = new JTextField();
        
        JLabel lblPassword = new JLabel("Mật khẩu:");
        txtPassword = new JPasswordField();
        
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        
        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnLogin = new JButton("Đăng nhập");
        btnRegister = new JButton("Đăng ký");
        btnExit = new JButton("Thoát");
        
        // Style buttons
        btnLogin.setBackground(new Color(0, 153, 76));
        btnLogin.setForeground(Color.black);
        btnLogin.setPreferredSize(new Dimension(100, 35));
        
        btnRegister.setBackground(new Color(51, 153, 255));
        btnRegister.setForeground(Color.black);
        btnRegister.setPreferredSize(new Dimension(100, 35));
        
        btnExit.setBackground(new Color(204, 0, 0));
        btnExit.setForeground(Color.black);	
        btnExit.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnExit);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Xử lý sự kiện
        btnLogin.addActionListener(e -> performLogin());
        btnRegister.addActionListener(e -> showRegisterDialog());
        btnExit.addActionListener(e -> System.exit(0));
        
        // Enter để đăng nhập
        txtPassword.addActionListener(e -> performLogin());
    }
    
    
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        
        // Kết nối đến server
        if (!client.connectToServer("localhost", 12345)) {
            return;
        }
        
        // Thực hiện đăng nhập
        String response = client.login(username, password);
        
        if (response.equals("SUCCESS")) {
            // Lấy thông tin user đầy đủ bằng username
            User user = client.getUserByUsername(username);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                showMainScreen(user);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể lấy thông tin user");
            }
        } else if (response.equals("FAIL")) {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu");
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối server");
        }
    }
    
    private void showRegisterDialog() {
        RegisterGUI registerDialog = new RegisterGUI(this, client);
        registerDialog.setVisible(true);
    }
    
    private void showMainScreen(User user) {
        setVisible(false);
     // Trang chủ sau khi đăng nhập
        JFrame mainFrame = new JFrame("Trang chủ - " + user.getFullName());
        mainFrame.setSize(800, 550);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // Panel chính với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Label chào mừng
        JLabel welcomeLabel = new JLabel("Chào mừng, " + user.getFullName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Panel thông tin người dùng
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Thông tin tài khoản",
                0, 0, new Font("Segoe UI", Font.BOLD, 14)
        ));
        infoPanel.setBackground(Color.WHITE);

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 14);

        infoPanel.add(new JLabel("Tên đăng nhập:")).setFont(infoFont);
        infoPanel.add(new JLabel(user.getUsername())).setFont(infoFont);

        infoPanel.add(new JLabel("Email:")).setFont(infoFont);
        infoPanel.add(new JLabel(user.getEmail())).setFont(infoFont);

        infoPanel.add(new JLabel("Họ và tên:")).setFont(infoFont);
        infoPanel.add(new JLabel(user.getFullName())).setFont(infoFont);

        infoPanel.add(new JLabel("Trạng thái:")).setFont(infoFont);
        infoPanel.add(new JLabel(user.isLoggedIn() ? " Online" : " Offline")).setFont(infoFont);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // Panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Style chung cho nút
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);

        JButton btnChangePassword = new JButton("Đổi mật khẩu");
        btnChangePassword.setBackground(new Color(51, 153, 255));
        btnChangePassword.setForeground(Color.black);
        btnChangePassword.setFocusPainted(false);
        btnChangePassword.setFont(btnFont);

        JButton btnUpdateProfile = new JButton("Cập nhật Profile");
        btnUpdateProfile.setBackground(new Color(255, 153, 51));
        btnUpdateProfile.setForeground(Color.black);
        btnUpdateProfile.setFocusPainted(false);
        btnUpdateProfile.setFont(btnFont);

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setBackground(new Color(204, 0, 0));
        btnLogout.setForeground(Color.black);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(btnFont);

        // Thêm các nút vào panel
        buttonPanel.add(btnChangePassword);
        buttonPanel.add(btnUpdateProfile);
        buttonPanel.add(btnLogout);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Thêm panel chính vào frame
        mainFrame.add(mainPanel);

        // Xử lý sự kiện
        btnChangePassword.addActionListener(e -> {
            ChangePasswordDialog dialog = new ChangePasswordDialog(mainFrame, client, user.getId());
            dialog.setVisible(true);
        });

        btnUpdateProfile.addActionListener(e -> {
            ProfileDialog dialog = new ProfileDialog(mainFrame, client, user.getId(), user);
            dialog.setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            client.logout(user.getUsername());
            mainFrame.dispose();
            setVisible(true);
        });

        mainFrame.setVisible(true);

    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginGUI().setVisible(true);
        });
    }
}