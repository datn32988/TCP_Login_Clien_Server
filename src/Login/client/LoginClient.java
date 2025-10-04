package Login.client;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Login.dto.LoginRequest;
import Login.dto.LoginResponse;
import Login.model.User;

public class LoginClient extends JFrame {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private User currentUser;
    private String currentRole;

    public LoginClient() {
        initializeLoginGUI();
    }

    private void initializeLoginGUI() {
        setTitle("Hệ Thống Đăng Nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Gradient background
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

        JPanel titlePanel = createTitlePanel();
        JPanel formPanel = createLoginFormPanel();
        JPanel footerPanel = createFooterPanel();

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel titleLabel = new JLabel("WELCOME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Đăng nhập để tiếp tục");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(230, 230, 230));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);

        return titlePanel;
    }

    private JPanel createLoginFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // White rounded background
        JPanel formContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(new EmptyBorder(40, 40, 40, 40));
        formContainer.setOpaque(false);

        // Username components
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(60, 60, 60));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Sửa từ preferredSize
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Password components
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(60, 60, 60));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Sửa từ preferredSize
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Buttons
        loginButton = new JButton("Đăng Nhập");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(74, 144, 226));
        loginButton.setForeground(Color.black); // Đổi thành màu trắng cho dễ đọc
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Sửa từ preferredSize
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton = new JButton("Tạo Tài Khoản");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.black); // Đổi thành màu trắng cho dễ đọc
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Sửa từ preferredSize
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái
        registerButton.setBorder(BorderFactory.createEmptyBorder());
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(54, 124, 206));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(74, 144, 226));
            }
        });

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

        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> showRegisterForm());

        // Enter key support
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // Button Panel (2 nút ngang nhau)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(15)); // Khoảng cách giữa 2 nút
        buttonPanel.add(registerButton);

        // Add components với các thành phần giữ khoảng cách
        formContainer.add(usernameLabel);
        formContainer.add(Box.createVerticalStrut(8));
        formContainer.add(usernameField);
        formContainer.add(Box.createVerticalStrut(20));
        formContainer.add(passwordLabel);
        formContainer.add(Box.createVerticalStrut(8));
        formContainer.add(passwordField);
        formContainer.add(Box.createVerticalStrut(30));
        formContainer.add(buttonPanel);

        formPanel.add(formContainer);
        return formPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel footerLabel = new JLabel("© 2025 Login System - Designed with Love");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(240, 240, 240));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        loginButton.setText("Đang đăng nhập...");
        loginButton.setEnabled(false);

        SwingWorker<LoginResponse, Void> worker = new SwingWorker<LoginResponse, Void>() {
            @Override
            protected LoginResponse doInBackground() throws Exception {
                return sendLoginRequest(username, password);
            }

            @Override
            protected void done() {
                try {
                    LoginResponse response = get();
                    if (response.isSuccess()) {
                        currentUser = response.getUser();
                        currentRole = response.getRoleName();
                        showDashboard();
                    } else {
                        showErrorMessage(response.getMessage());
                    }
                } catch (Exception e) {
                    showErrorMessage("Không thể kết nối đến server!");
                    e.printStackTrace();
                } finally {
                    loginButton.setText("Đăng Nhập");
                    loginButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private LoginResponse sendLoginRequest(String username, String password) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            LoginRequest request = new LoginRequest(username, password);
            oos.writeObject(request);

            return (LoginResponse) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(false, "Lỗi kết nối server!");
        }
    }

    private void showDashboard() {
        dispose();
        if ("ADMIN".equals(currentRole)) {
            new AdminDashboard(currentUser).setVisible(true);
        } else {
            new UserDashboard(currentUser).setVisible(true);
        }
    }

    private void showRegisterForm() {
        dispose();
        new RegisterClient().setVisible(true);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginClient().setVisible(true);
        });
    }
}