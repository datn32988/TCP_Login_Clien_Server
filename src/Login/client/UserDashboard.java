package Login.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import Login.model.User;
import Login.model.LoginHistory;
import Login.dao.LoginHistoryDAO;
import java.util.List;

public class UserDashboard extends JFrame {
    // Modern color palette - Blue theme (matching AdminDashboard)
    private static final Color PRIMARY_BLUE = new Color(33, 150, 243);
    private static final Color DARK_BLUE = new Color(25, 118, 210);
    private static final Color LIGHT_BLUE = new Color(227, 242, 253);
    private static final Color ACCENT_BLUE = new Color(13, 71, 161);
    private static final Color SUCCESS_GREEN = new Color(67, 160, 71);
    private static final Color WARNING_ORANGE = new Color(251, 140, 0);
    private static final Color DANGER_RED = new Color(244, 67, 54);
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    
    
    private User user;
    private JLabel welcomeLabel;
    private JPanel profileCard;
    private LoginHistoryDAO loginHistoryDAO;
    
    public UserDashboard(User user) {
        this.user = user;
        this.loginHistoryDAO = new LoginHistoryDAO();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("User Dashboard - " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Main panel with modern gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 248, 255),
                    0, getHeight(), new Color(225, 245, 254)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(0, 0));
        
        // Header
        JPanel headerPanel = createModernHeader();
        
        // Content
        JPanel contentPanel = createModernContent();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createModernHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(PRIMARY_BLUE);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Left side - Welcome text
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel("User Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        welcomeLabel = new JLabel("Xin chào, " + user.getFullName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(255, 255, 255, 200));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(welcomeLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(textPanel);
        
        // Right side - Action buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        JButton changePasswordBtn = createModernButton(" Đổi Mật Khẩu", ACCENT_BLUE);
        changePasswordBtn.setForeground(Color.BLACK);
        changePasswordBtn.addActionListener(e -> showChangePasswordDialog());
        
        JButton updateProfileBtn = createModernButton(" Sửa Profile", SUCCESS_GREEN);
        updateProfileBtn.addActionListener(e -> showUpdateProfileDialog());
        updateProfileBtn.setForeground(Color.BLACK);
        
        JButton logoutBtn = createModernButton(" Đăng Xuất", DANGER_RED);
        logoutBtn.addActionListener(e -> logout());
        logoutBtn.setForeground(Color.BLACK);
        rightPanel.add(changePasswordBtn);
        rightPanel.add(updateProfileBtn);
        rightPanel.add(logoutBtn);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private JPanel createModernContent() {
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Top section - Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        
        String accountStatus = user.isActive() ? "Hoạt động" : "Bị khóa";
        String onlineStatus = user.isLoggedIn() ? "Online" : "Offline";
        
        statsPanel.add(createStatCard(" Tài Khoản", accountStatus, 
            user.isActive() ? SUCCESS_GREEN : DANGER_RED));
        statsPanel.add(createStatCard(" Trạng Thái", onlineStatus, 
            user.isLoggedIn() ? SUCCESS_GREEN : TEXT_SECONDARY));
        statsPanel.add(createStatCard(" Vai Trò", "USER", PRIMARY_BLUE));
        statsPanel.add(createStatCard(" User ID", "#" + user.getId(), WARNING_ORANGE));
        
        // Middle section - Profile and Activity
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        middlePanel.setOpaque(false);
        
        profileCard = createProfileCard();
        JPanel activityCard = createActivityCard();
        
        middlePanel.add(profileCard);
        middlePanel.add(activityCard);
        
        // Bottom section - Features and Login History
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setOpaque(false);
        
        JPanel featuresCard = createFeaturesCard();
        JPanel historyCard = createLoginHistoryCard();
        
        bottomPanel.add(featuresCard);
        bottomPanel.add(historyCard);
        
        content.add(statsPanel, BorderLayout.NORTH);
        content.add(middlePanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        return content;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
            }
        };
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);
        
        // Color indicator
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(5, 0));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_SECONDARY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(valueLabel);
        
        card.add(colorBar, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createWhiteCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        card.add(titleLabel, BorderLayout.NORTH);
        
        return card;
    }
    
    private JPanel createProfileCard() {
        JPanel card = createWhiteCard(" Thông Tin Cá Nhân");
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        infoPanel.add(createInfoRow(" Họ và tên:", user.getFullName()));
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(createInfoRow(" Email:", user.getEmail()));
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(createInfoRow(" Username:", user.getUsername()));
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(createInfoRow(" User ID:", "#" + user.getId()));
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(createInfoRow(" Ngày tạo:", 
            user.getCreatedAt() != null ? sdf.format(user.getCreatedAt()) : "N/A"));
        
        card.add(infoPanel, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelLbl.setForeground(TEXT_SECONDARY);
        
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueLbl.setForeground(TEXT_PRIMARY);
        
        row.add(labelLbl, BorderLayout.WEST);
        row.add(valueLbl, BorderLayout.EAST);
        
        return row;
    }
    
    private JPanel createActivityCard() {
        JPanel card = createWhiteCard(" Hoạt Động Tài Khoản");
        
        JTextArea activityArea = new JTextArea(10, 30);
        activityArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        activityArea.setBackground(LIGHT_BLUE);
        activityArea.setForeground(TEXT_PRIMARY);
        activityArea.setEditable(false);
        activityArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder activity = new StringBuilder();
        activity.append("═══════════════════════════════════════════\n");
        activity.append("        THÔNG TIN HOẠT ĐỘNG\n");
        activity.append("═══════════════════════════════════════════\n\n");
        activity.append(" Thời gian hiện tại:\n");
        activity.append("   ").append(sdf.format(new Date())).append("\n\n");
        activity.append(" Trạng thái tài khoản:\n");
        activity.append("   • Trạng thái: ").append(user.isLoggedIn() ? " Online" : " Offline").append("\n");
        activity.append("   • Tình trạng: ").append(user.isActive() ? " Hoạt động" : " Bị khóa").append("\n\n");
        activity.append(" Thông tin thời gian:\n");
        activity.append("   • Tạo lúc: ").append(user.getCreatedAt() != null ? sdf.format(user.getCreatedAt()) : "N/A").append("\n");
        activity.append("   • Cập nhật: ").append(user.getUpdatedAt() != null ? sdf.format(user.getUpdatedAt()) : "N/A").append("\n\n");
        activity.append("═══════════════════════════════════════════\n");
        activity.append(" Hệ thống đang hoạt động bình thường\n");
        activity.append(" Tất cả dữ liệu được mã hóa bảo mật\n");
        
        activityArea.setText(activity.toString());
        
        JScrollPane scrollPane = new JScrollPane(activityArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createFeaturesCard() {
        JPanel card = createWhiteCard(" Tính Năng");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        
        JButton changePassBtn = createFeatureButton(" Thay Đổi Mật Khẩu", ACCENT_BLUE);
        changePassBtn.setForeground(Color.BLACK);
        JButton updateProfileBtn = createFeatureButton(" Cập Nhật Profile", SUCCESS_GREEN);
        updateProfileBtn.setForeground(Color.BLACK);
        JButton viewHistoryBtn = createFeatureButton(" Lịch Sử Đăng Nhập", PRIMARY_BLUE);
        viewHistoryBtn.setForeground(Color.BLACK);
        JButton helpBtn = createFeatureButton(" Trợ Giúp & Hỗ Trợ", WARNING_ORANGE);
        helpBtn.setForeground(Color.BLACK);
        
        changePassBtn.addActionListener(e -> showChangePasswordDialog());
        updateProfileBtn.addActionListener(e -> showUpdateProfileDialog());
        viewHistoryBtn.addActionListener(e -> showLoginHistoryDialog());
        helpBtn.addActionListener(e -> showHelpDialog());
        
        buttonPanel.add(changePassBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(updateProfileBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(viewHistoryBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(helpBtn);
        
        card.add(buttonPanel, BorderLayout.CENTER);
        return card;
    }
    
    private JButton createFeatureButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private JPanel createLoginHistoryCard() {
        JPanel card = createWhiteCard(" Lịch Sử Đăng Nhập Gần Đây");
        
        String[] columns = {"Thời gian", "IP Address", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(LIGHT_BLUE);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.black);
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        // Status column renderer
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if ("LOGIN".equals(value)) {
                    setText(" LOGIN");
                    setForeground(SUCCESS_GREEN);
                    setFont(new Font("Segoe UI", Font.BOLD, 10));
                } else if ("LOGOUT".equals(value)) {
                    setText(" LOGOUT");
                    setForeground(DANGER_RED);
                    setFont(new Font("Segoe UI", Font.BOLD, 10));
                }
                return this;
            }
        });
        
        // Load user's login history
        loadUserLoginHistory(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }
    
    private void loadUserLoginHistory(DefaultTableModel model) {
        model.setRowCount(0);
        List<LoginHistory> allHistory = loginHistoryDAO.getLoginHistory();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        int count = 0;
        
        // Filter history for current user and show last 5 entries
        for (LoginHistory lh : allHistory) {
            if (lh.getUserId() == user.getId() && count < 5) {
                String loginTime = lh.getLoginTime() != null ? sdf.format(lh.getLoginTime()) : "";
                model.addRow(new Object[]{
                    loginTime,
                    lh.getIpAddress() != null ? lh.getIpAddress() : "N/A",
                    lh.getStatus()
                });
                count++;
            }
        }
        
        // If no history found, show placeholder
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"Chưa có dữ liệu", "—", "—"});
        }
    }
    
    private void showChangePasswordDialog() {
        ChangePasswordDialog dialog = new ChangePasswordDialog(this, user);
        dialog.setVisible(true);
    }
    
    private void showUpdateProfileDialog() {
        UpdateProfileDialog dialog = new UpdateProfileDialog(this, user, () -> {
            refreshProfileCard();
        });
        dialog.setVisible(true);
    }
    
    private void showLoginHistoryDialog() {
        JDialog historyDialog = new JDialog(this, "Lịch Sử Đăng Nhập Đầy Đủ", true);
        historyDialog.setSize(800, 500);
        historyDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(" Lịch Sử Đăng Nhập Của Bạn");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_BLUE);
        
        String[] columns = {"ID", "Thời gian đăng nhập", "Thời gian đăng xuất", "IP Address", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setBackground(PRIMARY_BLUE);
        table.getTableHeader().setForeground(Color.black);
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Load all user history
        List<LoginHistory> allHistory = loginHistoryDAO.getLoginHistory();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        for (LoginHistory lh : allHistory) {
            if (lh.getUserId() == user.getId()) {
                String loginTime = lh.getLoginTime() != null ? sdf.format(lh.getLoginTime()) : "";
                String logoutTime = lh.getLogoutTime() != null ? sdf.format(lh.getLogoutTime()) : "—";
                
                model.addRow(new Object[]{
                    lh.getId(),
                    loginTime,
                    logoutTime,
                    lh.getIpAddress() != null ? lh.getIpAddress() : "N/A",
                    lh.getStatus()
                });
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton closeBtn = createModernButton("Đóng", PRIMARY_BLUE);
        closeBtn.setForeground(Color.BLACK);
        closeBtn.addActionListener(e -> historyDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeBtn);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        historyDialog.add(panel);
        historyDialog.setVisible(true);
    }
    
    private void showHelpDialog() {
        JDialog helpDialog = new JDialog(this, "Trợ Giúp & Hỗ Trợ", true);
        helpDialog.setSize(600, 450);
        helpDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("❓ Trợ Giúp & Hỗ Trợ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_BLUE);
        
        JTextArea helpText = new JTextArea();
        helpText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        StringBuilder help = new StringBuilder();
        help.append("═══════════════════════════════════════════════\n");
        help.append("              THÔNG TIN HỖ TRỢ\n");
        help.append("═══════════════════════════════════════════════\n\n");
        help.append(" LIÊN HỆ:\n");
        help.append("   • Email: support@company.com\n");
        help.append("   • Hotline: 1900-xxxx (8:00 - 22:00)\n");
        help.append("   • Website: https://help.company.com\n\n");
        help.append(" CÁC TÍNH NĂNG:\n");
        help.append("   • Đổi mật khẩu: Bảo mật tài khoản của bạn\n");
        help.append("   • Cập nhật profile: Thay đổi thông tin cá nhân\n");
        help.append("   • Xem lịch sử: Theo dõi hoạt động đăng nhập\n\n");
        help.append(" BẢO MẬT:\n");
        help.append("   • Không chia sẻ mật khẩu với người khác\n");
        help.append("   • Sử dụng mật khẩu mạnh (>8 ký tự)\n");
        help.append("   • Đăng xuất khi không sử dụng\n\n");
        help.append(" CÂU HỎI THƯỜNG GẶP:\n");
        help.append("   1. Làm sao để đổi mật khẩu?\n");
        help.append("      → Nhấn nút 'Đổi Mật Khẩu' ở góc trên\n\n");
        help.append("   2. Làm sao để cập nhật thông tin?\n");
        help.append("      → Nhấn nút 'Sửa Profile'\n\n");
        help.append("   3. Tôi quên mật khẩu?\n");
        help.append("      → Liên hệ support@company.com\n\n");
        help.append(" BÁO LỖI:\n");
        help.append("   • Email: bugs@company.com\n");
        help.append("   • Mô tả chi tiết lỗi gặp phải\n");
        help.append("   • Gửi kèm screenshot nếu có\n\n");
        help.append("═══════════════════════════════════════════════\n");
        help.append("Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!\n");
        
        helpText.setText(help.toString());
        
        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        JButton closeBtn = createModernButton("Đóng", PRIMARY_BLUE);
        closeBtn.addActionListener(e -> helpDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeBtn);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        helpDialog.add(panel);
        helpDialog.setVisible(true);
    }
    
    private void refreshProfileCard() {
        // Update welcome label
        welcomeLabel.setText("Xin chào, " + user.getFullName());
        
        // Update profile card content
        Container parent = profileCard.getParent();
        int index = -1;
        
        // Find the index of current profile card
        for (int i = 0; i < parent.getComponentCount(); i++) {
            if (parent.getComponent(i) == profileCard) {
                index = i;
                break;
            }
        }
        
        parent.remove(profileCard);
        profileCard = createProfileCard();
        
        if (index >= 0) {
            parent.add(profileCard, index);
        } else {
            parent.add(profileCard);
        }
        
        parent.revalidate();
        parent.repaint();
        
        showModernSuccess("Profile đã được cập nhật thành công!");
    }
    
    private void logout() {
    	int choice = JOptionPane.showConfirmDialog(this, 
    	        "Bạn có chắc chắn muốn đăng xuất?", 
    	        "Xác nhận Đăng Xuất", 
    	        JOptionPane.YES_NO_OPTION,
    	        JOptionPane.QUESTION_MESSAGE);
    	    
    	    if (choice == JOptionPane.YES_OPTION) {
    	        // Cập nhật trạng thái đăng nhập của user
    	        new Login.dao.UserDAO().updateLoginStatus(user.getId(), false);

    	        // Ghi log logout
    	        loginHistoryDAO.recordLogout(user.getId());

    	        // Đóng dashboard, quay về màn hình login
    	        dispose();
    	        new LoginClient().setVisible(true);
    	    }
    }
    
    private void showModernSuccess(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Thành công");
        dialog.setVisible(true);
    }
}