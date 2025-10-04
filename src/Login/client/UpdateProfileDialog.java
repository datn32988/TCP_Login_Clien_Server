package Login.client;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Login.dto.*;
import Login.model.User;

public class UpdateProfileDialog extends JDialog {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    
    private User currentUser;
    private JTextField emailField;
    private JTextField fullNameField;
    private JButton updateButton;
    private JButton cancelButton;
    private Runnable onUpdateCallback;
    
    public UpdateProfileDialog(JFrame parent, User user, Runnable onUpdateCallback) {
        super(parent, "Cập Nhật Profile", true);
        this.currentUser = user;
        this.onUpdateCallback = onUpdateCallback;
        initializeGUI();
    }
    
    private void initializeGUI() {
        setSize(380, 320);
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
        JLabel titleLabel = new JLabel("Cập Nhật Profile", SwingConstants.CENTER);
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
        formPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        formPanel.setOpaque(false);
        
        // Email
        JPanel emailPanel = createFieldPanel("Email:", emailField = createTextField(currentUser.getEmail()));
        
        // Full Name
        JPanel namePanel = createFieldPanel("Họ và tên:", fullNameField = createTextField(currentUser.getFullName()));
        
        formPanel.add(emailPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(namePanel);
        
        return formPanel;
    }
    
    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(280, 35));
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(280, 35));
        field.setMaximumSize(new Dimension(280, 35));
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
        buttonPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        updateButton = new JButton("Cập Nhật");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        updateButton.setBackground(new Color(46, 204, 113));
        updateButton.setForeground(Color.black);
        updateButton.setPreferredSize(new Dimension(120, 35));
        updateButton.setBorder(BorderFactory.createEmptyBorder());
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.black);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBorder(BorderFactory.createEmptyBorder());
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(new Color(46, 204, 113));
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
        
        updateButton.addActionListener(e -> updateProfile());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
    private void updateProfile() {
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        
        if (email.isEmpty() || fullName.isEmpty()) {
            showErrorMessage("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        
        if (!isValidEmail(email)) {
            showErrorMessage("Email không hợp lệ!");
            return;
        }
        
        updateButton.setText("Đang cập nhật...");
        updateButton.setEnabled(false);
        
        SwingWorker<GenericResponse, Void> worker = new SwingWorker<GenericResponse, Void>() {
            @Override
            protected GenericResponse doInBackground() throws Exception {
                return sendUpdateProfileRequest(email, fullName);
            }
            
            @Override
            protected void done() {
                try {
                    GenericResponse response = get();
                    if (response.isSuccess()) {
                        showSuccessMessage("Cập nhật profile thành công!");
                        currentUser.setEmail(email);
                        currentUser.setFullName(fullName);
                        if (onUpdateCallback != null) {
                            onUpdateCallback.run();
                        }
                        dispose();
                    } else {
                        showErrorMessage(response.getMessage());
                    }
                } catch (Exception e) {
                    showErrorMessage("Không thể kết nối đến server!");
                } finally {
                    updateButton.setText("Cập Nhật");
                    updateButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }
    
    private GenericResponse sendUpdateProfileRequest(String email, String fullName) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            
            UpdateProfileRequest request = new UpdateProfileRequest(currentUser.getId(), email, fullName);
            oos.writeObject(request);
            
            return (GenericResponse) ois.readObject();
            
        } catch (Exception e) {
            return new GenericResponse(false, "Lỗi kết nối server!");
        }
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}