package Login.client;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Login.dto.*;
import Login.model.User;

public class ChangePasswordDialog extends JDialog {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    
    private User currentUser;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton changeButton;
    private JButton cancelButton;
    
    public ChangePasswordDialog(JFrame parent, User user) {
        super(parent, "Thay Đổi Mật Khẩu", true);
        this.currentUser = user;
        initializeGUI();
    }
    
    private void initializeGUI() {
        setSize(380, 420);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Main panel with blue gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226),
                    getWidth(), getHeight(), new Color(52, 152, 219)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Thay Đổi Mật Khẩu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setOpaque(false);
        
        // Current password
        JPanel currentPanel = createFieldPanel("Mật khẩu hiện tại:", currentPasswordField = createPasswordField());
        JPanel newPanel = createFieldPanel("Mật khẩu mới:", newPasswordField = createPasswordField());
        JPanel confirmPanel = createFieldPanel("Xác nhận mật khẩu:", confirmPasswordField = createPasswordField());
        
        formPanel.add(currentPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(newPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(confirmPanel);
        
        return formPanel;
    }
    
    private JPanel createFieldPanel(String labelText, JPasswordField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(300, 35));
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(300, 35));
        field.setMaximumSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        changeButton = new JButton("Thay Đổi");
        changeButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        changeButton.setBackground(new Color(46, 204, 113));
        changeButton.setForeground(Color.black);
        changeButton.setPreferredSize(new Dimension(120, 35));
        changeButton.setBorder(BorderFactory.createEmptyBorder());
        changeButton.setFocusPainted(false);
        changeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.black);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBorder(BorderFactory.createEmptyBorder());
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        changeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                changeButton.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                changeButton.setBackground(new Color(46, 204, 113));
            }
        });
        
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(90, 98, 104));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(108, 117, 125));
            }
        });
        
        changeButton.addActionListener(e -> changePassword());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(changeButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showErrorMessage("Mật khẩu mới không khớp!");
            return;
        }
        
        if (newPassword.length() < 6) {
            showErrorMessage("Mật khẩu mới phải có ít nhất 6 ký tự!");
            return;
        }
        
        if (currentPassword.equals(newPassword)) {
            showErrorMessage("Mật khẩu mới phải khác mật khẩu hiện tại!");
            return;
        }
        
        changeButton.setText("Đang thay đổi...");
        changeButton.setEnabled(false);
        
        SwingWorker<GenericResponse, Void> worker = new SwingWorker<GenericResponse, Void>() {
            @Override
            protected GenericResponse doInBackground() throws Exception {
                return sendChangePasswordRequest(currentPassword, newPassword);
            }
            
            @Override
            protected void done() {
                try {
                    GenericResponse response = get();
                    if (response.isSuccess()) {
                        showSuccessMessage("Thay đổi mật khẩu thành công!");
                        dispose();
                    } else {
                        showErrorMessage(response.getMessage());
                    }
                } catch (Exception e) {
                    showErrorMessage("Không thể kết nối đến server!");
                } finally {
                    changeButton.setText("Thay Đổi");
                    changeButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }
    
    private GenericResponse sendChangePasswordRequest(String oldPassword, String newPassword) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            
            ChangePasswordRequest request = new ChangePasswordRequest(currentUser.getId(), oldPassword, newPassword);
            oos.writeObject(request);
            
            return (GenericResponse) ois.readObject();
            
        } catch (Exception e) {
            return new GenericResponse(false, "Lỗi kết nối server!");
        }
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}