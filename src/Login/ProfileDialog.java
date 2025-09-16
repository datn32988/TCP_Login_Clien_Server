package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProfileDialog extends JDialog {
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JButton btnSave;
    private JButton btnCancel;
    private LoginClient client;
    private int userId;
    
    public ProfileDialog(JFrame parent, LoginClient client, int userId, User user) {
        super(parent, "Cập nhật thông tin", true);
        this.client = client;
        this.userId = userId;
        initializeUI();
        loadUserData(user);
    }
    
    private void initializeUI() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tiêu đề
        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 102, 204));
        
        // Panel form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        txtUsername = new JTextField();
        txtUsername.setEditable(false);
        
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField();
        
        JLabel lblFullName = new JLabel("Họ và tên:");
        txtFullName = new JTextField();
        
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblFullName);
        formPanel.add(txtFullName);
        
        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnSave = new JButton("Lưu thay đổi");
        btnCancel = new JButton("Hủy");
        
        btnSave.setBackground(new Color(0, 153, 76));
        btnSave.setForeground(Color.WHITE);
        
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Xử lý sự kiện
        btnSave.addActionListener(e -> performUpdateProfile());
        btnCancel.addActionListener(e -> dispose());
        
        txtFullName.addActionListener(e -> performUpdateProfile());
    }
    
    private void loadUserData(User user) {
        if (user != null) {
            txtUsername.setText(user.getUsername());
            txtEmail.setText(user.getEmail());
            txtFullName.setText(user.getFullName());
        }
    }
    
    private void performUpdateProfile() {
        String email = txtEmail.getText().trim();
        String fullName = txtFullName.getText().trim();
        
        // Validate
        if (email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin");
            return;
        }
        
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ");
            return;
        }
        
        // Kết nối server
        if (!client.connectToServer("localhost", 12345)) {
            return;
        }
        
        // Thực hiện cập nhật
        String response = client.updateProfile(userId, email, fullName);
        
        switch (response) {
            case "PROFILE_UPDATE_SUCCESS":
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                dispose();
                break;
            case "EMAIL_EXISTS":
                JOptionPane.showMessageDialog(this, "Email đã được sử dụng");
                break;
            case "PROFILE_UPDATE_FAIL":
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Lỗi kết nối server");
        }
    }
}