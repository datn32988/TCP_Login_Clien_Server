package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChangePasswordDialog extends JDialog {
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnChange;
    private JButton btnCancel;
    private LoginClient client;
    private int userId;
    
    public ChangePasswordDialog(JFrame parent, LoginClient client, int userId) {
        super(parent, "Đổi mật khẩu", true);
        this.client = client;
        this.userId = userId;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(350, 250);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tiêu đề
        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 102, 204));
        
        // Panel form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel lblCurrentPassword = new JLabel("Mật khẩu hiện tại:");
        txtCurrentPassword = new JPasswordField();
        
        JLabel lblNewPassword = new JLabel("Mật khẩu mới:");
        txtNewPassword = new JPasswordField();
        
        JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
        txtConfirmPassword = new JPasswordField();
        
        formPanel.add(lblCurrentPassword);
        formPanel.add(txtCurrentPassword);
        formPanel.add(lblNewPassword);
        formPanel.add(txtNewPassword);
        formPanel.add(lblConfirmPassword);
        formPanel.add(txtConfirmPassword);
        
        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnChange = new JButton("Đổi mật khẩu");
        btnCancel = new JButton("Hủy");
        
        btnChange.setBackground(new Color(0, 153, 76));
        btnChange.setForeground(Color.black);
        
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.black);
        
        buttonPanel.add(btnChange);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Xử lý sự kiện
        btnChange.addActionListener(e -> performChangePassword());
        btnCancel.addActionListener(e -> dispose());
        
        txtConfirmPassword.addActionListener(e -> performChangePassword());
    }
    
    private void performChangePassword() {
        String currentPassword = new String(txtCurrentPassword.getPassword());
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        // Validate
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp");
            return;
        }
        
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự");
            return;
        }
        
        // Kết nối server
        if (!client.connectToServer("localhost", 12345)) {
            return;
        }
        
        // Thực hiện đổi mật khẩu
        String response = client.changePassword(userId, currentPassword, newPassword);
        
        switch (response) {
            case "PASSWORD_CHANGE_SUCCESS":
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                dispose();
                break;
            case "CURRENT_PASSWORD_INCORRECT":
                JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng");
                break;
            case "PASSWORD_CHANGE_FAIL":
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Lỗi kết nối server");
        }
    }
}