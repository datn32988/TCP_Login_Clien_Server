package Login.client;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Login.dto.RegisterRequest;
import Login.dto.GenericResponse;

public class RegisterClient extends JFrame {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JButton registerButton;
    private JButton loginButton;
    
    public RegisterClient() {
        initializeRegisterGUI();
    }
    
    private void initializeRegisterGUI() {
        setTitle("Đăng Ký Tài Khoản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600); // Giảm chiều cao từ 700 xuống 600
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create gradient background panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226),
                    0, getHeight(), new Color(143, 148, 251)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title Panel
        JPanel titlePanel = createTitlePanel();
        
        // Register Form Panel
        JPanel formPanel = createRegisterFormPanel();
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20)); // Giảm padding
        
        JLabel titleLabel = new JLabel("TẠO TÀI KHOẢN MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Giảm kích thước font
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Điền thông tin để đăng ký");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Giảm kích thước font
        subtitleLabel.setForeground(new Color(230, 230, 230));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        
        return titlePanel;
    }
    
    private JPanel createRegisterFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(10, 40, 10, 40)); // Giảm padding
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Create white rounded panel for form
        JPanel formContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Giảm border radius
            }
        };
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(new EmptyBorder(25, 30, 25, 30)); // Giảm padding
        formContainer.setOpaque(false);
        
        // Form fields
        usernameField = createTextField("Tên đăng nhập:");
        passwordField = (JPasswordField) createTextField("Mật khẩu:", true);
        confirmPasswordField = (JPasswordField) createTextField("Xác nhận mật khẩu:", true);
        emailField = createTextField("Email:");
        fullNameField = createTextField("Họ và tên:");
        
        // Register button (primary)
        registerButton = new JButton("Đăng Ký");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Giảm kích thước font
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.black);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Giảm chiều cao
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Giảm padding
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Login button (secondary)
        loginButton = new JButton("Đã có tài khoản? Đăng nhập ngay");
        loginButton.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Giảm kích thước font
        loginButton.setBackground(Color.black);
        loginButton.setForeground(new Color(52, 152, 219));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // Giảm chiều cao
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 1)); // Giảm border width
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Button hover effects
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(39, 174, 96));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(46, 204, 113));
            }
        });
        
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(236, 245, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Color.WHITE);
            }
        });
        
        registerButton.addActionListener(e -> performRegister());
        loginButton.addActionListener(e -> showLoginForm());
        
        // Add Enter key support
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performRegister();
                }
            }
        };
        
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        confirmPasswordField.addKeyListener(enterKeyListener);
        emailField.addKeyListener(enterKeyListener);
        fullNameField.addKeyListener(enterKeyListener);
        
        // Add fields to container với khoảng cách hợp lý
        formContainer.add(createFieldContainer("Tên đăng nhập:", usernameField));
        formContainer.add(Box.createVerticalStrut(12)); // Giảm khoảng cách
        formContainer.add(createFieldContainer("Mật khẩu:", passwordField));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldContainer("Xác nhận mật khẩu:", confirmPasswordField));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldContainer("Email:", emailField));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldContainer("Họ và tên:", fullNameField));
        formContainer.add(Box.createVerticalStrut(20)); // Giảm khoảng cách
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createVerticalStrut(8)); // Giảm khoảng cách
        buttonPanel.add(loginButton);
        
        formContainer.add(buttonPanel);
        
        formPanel.add(formContainer);
        return formPanel;
    }
    
    private JTextField createTextField(String labelText) {
        return createTextField(labelText, false);
    }
    
    private JTextField createTextField(String labelText, boolean isPassword) {
        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 13)); // Giảm kích thước font
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); // Giảm chiều cao
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10) // Giảm padding
        ));
        return field;
    }
    
    private JPanel createFieldContainer(String labelText, JTextField field) {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13)); // Giảm kích thước font
        label.setForeground(new Color(60, 60, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); // Giảm chiều cao
        
        container.add(label);
        container.add(Box.createVerticalStrut(4)); // Giảm khoảng cách
        container.add(field);
        
        return container;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(5, 20, 10, 20)); // Giảm padding
        
        JLabel footerLabel = new JLabel("© 2025 Login System");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11)); // Giảm kích thước font
        footerLabel.setForeground(new Color(240, 240, 240));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(footerLabel);
        return footerPanel;
    }
    
    private void performRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        
        // Validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            showErrorMessage("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showErrorMessage("Mật khẩu xác nhận không khớp!");
            return;
        }
        
        if (password.length() < 6) {
            showErrorMessage("Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }
        
        if (!isValidEmail(email)) {
            showErrorMessage("Email không hợp lệ!");
            return;
        }
        
        if (username.length() < 3) {
            showErrorMessage("Tên đăng nhập phải có ít nhất 3 ký tự!");
            return;
        }
        
        registerButton.setText("Đang đăng ký...");
        registerButton.setEnabled(false);
        
        SwingWorker<GenericResponse, Void> worker = new SwingWorker<GenericResponse, Void>() {
            @Override
            protected GenericResponse doInBackground() throws Exception {
                return sendRegisterRequest(username, password, email, fullName);
            }
            
            @Override
            protected void done() {
                try {
                    GenericResponse response = get();
                    if (response.isSuccess()) {
                        showSuccessMessage("Đăng ký thành công! Vui lòng đăng nhập.");
                        showLoginForm();
                    } else {
                        showErrorMessage(response.getMessage());
                    }
                } catch (Exception e) {
                    showErrorMessage("Không thể kết nối đến server!");
                    e.printStackTrace();
                } finally {
                    registerButton.setText("Đăng Ký");
                    registerButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }
    
    private GenericResponse sendRegisterRequest(String username, String password, String email, String fullName) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            
            RegisterRequest request = new RegisterRequest(username, password, email, fullName);
            oos.writeObject(request);
            
            return (GenericResponse) ois.readObject();
            
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponse(false, "Lỗi kết nối server!");
        }
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    
    private void showLoginForm() {
        dispose();
        new LoginClient().setVisible(true);
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new RegisterClient().setVisible(true);
        });
    }
}