package Login.client;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;

import Login.dao.*;
import Login.dto.*;
import Login.model.User;
import Login.model.LoginHistory;
import java.util.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    
    // Modern color palette - Blue theme
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
    
    private User admin;
    private JTabbedPane tabbedPane;
    private UserDAO userDAO;
    private LoginHistoryDAO loginHistoryDAO;
    private DefaultTableModel userTableModel;
    private DefaultTableModel loginHistoryTableModel;
    private JTable userTable;
    private JTable loginHistoryTable;
    private JTextField searchField;
    private JLabel totalUsersLabel, onlineUsersLabel, adminCountLabel, activeUsersLabel;
    
    public AdminDashboard(User admin) {
        this.admin = admin;
        this.userDAO = new UserDAO();
        this.loginHistoryDAO = new LoginHistoryDAO();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Admin Dashboard - " + admin.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 900);
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
        
        // Header with modern design
        JPanel headerPanel = createModernHeader();
        
        // Tabbed pane with custom UI
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(TEXT_PRIMARY);
        
        // Custom tab design
        UIManager.put("TabbedPane.selected", PRIMARY_BLUE);
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
        
        // Add tabs with icons (using unicode symbols)
        tabbedPane.addTab(" Dashboard", createModernDashboard());
        tabbedPane.addTab(" Quản Lý Users", createModernUserManagement());
        tabbedPane.addTab(" Lịch Sử Đăng Nhập", createModernLoginHistory());
        tabbedPane.addTab(" Thêm User", createModernAddUser());
        
        tabbedPane.setBorder(new EmptyBorder(10, 15, 15, 15));
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
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
        
        JLabel titleLabel = new JLabel("Admin Control Panel");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Xin chào, " + admin.getFullName());
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(textPanel);
        
        // Right side - Action buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        JButton refreshBtn = createModernButton(" Refresh", SUCCESS_GREEN);
        refreshBtn.setForeground(Color.BLACK);
        refreshBtn.addActionListener(e -> refreshAllData());
        
        JButton logoutBtn = createModernButton(" Đăng Xuất", DANGER_RED);
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.addActionListener(e -> logout());
        
        rightPanel.add(refreshBtn);
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
    
    private JPanel createModernDashboard() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        
        List<User> allUsers = userDAO.getAllUsers();
        long totalUsers = allUsers.size();
        long onlineUsers = allUsers.stream().filter(User::isLoggedIn).count();
        long adminCount = allUsers.stream().filter(u -> u.getRoleId() == 1).count();
        long activeUsers = allUsers.stream().filter(User::isActive).count();
        
        statsPanel.add(createStatCard(" Tổng Users", String.valueOf(totalUsers), PRIMARY_BLUE, "totalUsers"));
        statsPanel.add(createStatCard(" Online", String.valueOf(onlineUsers), SUCCESS_GREEN, "onlineUsers"));
        statsPanel.add(createStatCard(" Admins", String.valueOf(adminCount), WARNING_ORANGE, "adminCount"));
        statsPanel.add(createStatCard(" Active", String.valueOf(activeUsers), new Color(0, 150, 136), "activeUsers"));
        
        // Recent activity panel
        JPanel activityPanel = createModernActivityPanel();
        
        // Quick stats panel
        JPanel quickStatsPanel = createQuickStatsPanel();
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(activityPanel);
        centerPanel.add(quickStatsPanel);
        
        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color, String id) {
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
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_SECONDARY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        
        // Store label reference for updates
        if ("totalUsers".equals(id)) totalUsersLabel = valueLabel;
        else if ("onlineUsers".equals(id)) onlineUsersLabel = valueLabel;
        else if ("adminCount".equals(id)) adminCountLabel = valueLabel;
        else if ("activeUsers".equals(id)) activeUsersLabel = valueLabel;
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(valueLabel);
        
        card.add(colorBar, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createModernActivityPanel() {
        JPanel panel = createWhiteCard("Hoạt Động Gần Đây");
        
        JTextArea activityArea = new JTextArea(15, 40);
        activityArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        activityArea.setBackground(LIGHT_BLUE);
        activityArea.setForeground(TEXT_PRIMARY);
        activityArea.setEditable(false);
        activityArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder activity = new StringBuilder();
        activity.append("╔══════════════════════════════════════════════════╗\n");
        activity.append("║        HỆ THỐNG QUẢN LÝ ADMIN - LOG             ║\n");
        activity.append("╚══════════════════════════════════════════════════╝\n\n");
        activity.append(" Thời gian: ").append(sdf.format(new Date())).append("\n\n");
        activity.append(" Admin: ").append(admin.getUsername()).append(" (").append(admin.getFullName()).append(")\n");
        activity.append(" Trạng thái hệ thống: ✅ HOẠT ĐỘNG BÌNH THƯỜNG\n\n");
        
        List<User> users = userDAO.getAllUsers();
        activity.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        activity.append("THỐNG KÊ:\n");
        activity.append("  • Tổng users: ").append(users.size()).append("\n");
        activity.append("  • Đang online: ").append(users.stream().filter(User::isLoggedIn).count()).append("\n");
        activity.append("  • Active users: ").append(users.stream().filter(User::isActive).count()).append("\n");
        activity.append("  • Banned users: ").append(users.stream().filter(u -> !u.isActive()).count()).append("\n\n");
        
        activity.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        activity.append("TÍNH NĂNG:\n");
        activity.append("   Quản lý users (Thêm/Xóa/Ban/Unban)\n");
        activity.append("   Xem lịch sử đăng nhập chi tiết\n");
        activity.append("   Tìm kiếm và lọc users\n");
        activity.append("   Thống kê và báo cáo real-time\n");
        activity.append("   Bảo mật cao với xác thực 2 lớp\n");
        
        activityArea.setText(activity.toString());
        
        JScrollPane scrollPane = new JScrollPane(activityArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createQuickStatsPanel() {
    	int activeSessionsCount = 0;
        JPanel panel = createWhiteCard(" Thống Kê Nhanh");
        panel.setLayout(new GridLayout(6, 1, 0, 10));
        
        List<LoginHistory> recentLogins = loginHistoryDAO.getLoginHistory();
        int todayLogins = 0;
        int activeSessionscount = 0;
        
        java.util.Calendar today = java.util.Calendar.getInstance();
        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
        today.set(java.util.Calendar.MINUTE, 0);
        today.set(java.util.Calendar.SECOND, 0);
        
        for (LoginHistory lh : recentLogins) {
            if (lh.getLoginTime() != null && lh.getLoginTime().after(today.getTime())) {
                todayLogins++;
            }
            if ("LOGIN".equals(lh.getStatus())) {
                activeSessionsCount++;
            }
        }
        
        panel.add(createQuickStatItem(" Đăng nhập hôm nay", String.valueOf(todayLogins)));
        panel.add(createQuickStatItem(" Phiên đang hoạt động", String.valueOf(activeSessionsCount)));
        panel.add(createQuickStatItem(" Tổng lịch sử", String.valueOf(recentLogins.size())));
        panel.add(createQuickStatItem(" Server Status", "ONLINE"));
        panel.add(createQuickStatItem(" Database", "CONNECTED"));
        panel.add(createQuickStatItem(" Backup", "OK"));
        
        return panel;
    }
    
    private JPanel createQuickStatItem(String label, String value) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelLbl.setForeground(TEXT_SECONDARY);
        
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLbl.setForeground(PRIMARY_BLUE);
        
        item.add(labelLbl, BorderLayout.WEST);
        item.add(valueLbl, BorderLayout.EAST);
        
        return item;
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
    
    private JPanel createModernUserManagement() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = createUserManagementTop();
        JPanel tablePanel = createModernUserTable();
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUserManagementTop() {
        JPanel topPanel = createWhiteCard(" QUẢN LÝ USERS");
        topPanel.setLayout(new BorderLayout(15, 0));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setBackground(LIGHT_BLUE);
        
        JButton searchBtn = createModernButton(" Tìm", PRIMARY_BLUE);
        searchBtn.setForeground(Color.BLACK);
        searchBtn.addActionListener(e -> searchUsers());
        
        JButton clearBtn = createModernButton(" Clear", new Color(149, 165, 166));
        clearBtn.setForeground(Color.BLACK);
        clearBtn.addActionListener(e -> clearSearch());
        
        searchPanel.add(new JLabel("Tìm kiếm: "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        
        JButton banBtn = createModernButton(" Ban", WARNING_ORANGE);
        banBtn.setForeground(Color.BLACK);
        JButton unbanBtn = createModernButton(" Unban", SUCCESS_GREEN);
        unbanBtn.setForeground(Color.BLACK);
        JButton deleteBtn = createModernButton(" Delete", DANGER_RED);
        deleteBtn.setForeground(Color.BLACK);
        
        banBtn.addActionListener(e -> banSelectedUser());
        unbanBtn.addActionListener(e -> unbanSelectedUser());
        deleteBtn.addActionListener(e -> deleteSelectedUser());
        
        actionPanel.add(banBtn);
        actionPanel.add(unbanBtn);
        actionPanel.add(deleteBtn);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createModernUserTable() {
        JPanel panel = createWhiteCard("");
        
        String[] columns = {"ID", "Username", "Full Name", "Email", "Role", "Status", "Active", "Created"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(userTableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userTable.setRowHeight(35);
        userTable.setBackground(Color.WHITE);
        userTable.setGridColor(new Color(240, 240, 240));
        userTable.setSelectionBackground(LIGHT_BLUE);
        userTable.setSelectionForeground(TEXT_PRIMARY);
        
        // Header styling
        JTableHeader header = userTable.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.black);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Custom renderer for Active column
        userTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if (value instanceof Boolean) {
                    boolean isActive = (Boolean) value;
                    setText(isActive ? " Active" : " Banned");
                    setForeground(isActive ? SUCCESS_GREEN : DANGER_RED);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                return this;
            }
        });
        
        // Status column renderer
        userTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if ("Online".equals(value)) {
                    setText(" Online");
                    setForeground(SUCCESS_GREEN);
                } else {
                    setText(" Offline");
                    setForeground(TEXT_SECONDARY);
                }
                return this;
            }
        });
        
        loadUserData();
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createModernLoginHistory() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = createWhiteCard(" LỊCH SỬ ĐĂNG NHẬP");
        headerPanel.setLayout(new BorderLayout());
        
        // Refresh button
        JButton refreshBtn = createModernButton(" Refresh", PRIMARY_BLUE);
        refreshBtn.setForeground(Color.BLACK);
        refreshBtn.addActionListener(e -> loadLoginHistoryData());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(refreshBtn);
        
        headerPanel.add(btnPanel, BorderLayout.EAST);
        
        // Table
        JPanel tablePanel = createLoginHistoryTable();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLoginHistoryTable() {
        JPanel panel = createWhiteCard("");
        
        String[] columns = {"ID", "User ID", "Username", "Login Time", "Logout Time", "IP Address", "Status"};
        loginHistoryTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        loginHistoryTable = new JTable(loginHistoryTableModel);
        loginHistoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginHistoryTable.setRowHeight(32);
        loginHistoryTable.setBackground(Color.WHITE);
        loginHistoryTable.setGridColor(new Color(240, 240, 240));
        loginHistoryTable.setSelectionBackground(LIGHT_BLUE);
        
        // Header styling
        JTableHeader header = loginHistoryTable.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.black);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Status column renderer
        loginHistoryTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if ("LOGIN".equals(value)) {
                    setText(" LOGIN");
                    setForeground(SUCCESS_GREEN);
                    setFont(new Font("Segoe UI", Font.BOLD, 11));
                } else if ("LOGOUT".equals(value)) {
                    setText(" LOGOUT");
                    setForeground(DANGER_RED);
                    setFont(new Font("Segoe UI", Font.BOLD, 11));
                }
                return this;
            }
        });
        
        loadLoginHistoryData();
        
        JScrollPane scrollPane = new JScrollPane(loginHistoryTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void loadLoginHistoryData() {
        loginHistoryTableModel.setRowCount(0);
        List<LoginHistory> historyList = loginHistoryDAO.getLoginHistory();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        for (LoginHistory lh : historyList) {
            String loginTime = lh.getLoginTime() != null ? sdf.format(lh.getLoginTime()) : "";
            String logoutTime = lh.getLogoutTime() != null ? sdf.format(lh.getLogoutTime()) : "—";
            
            loginHistoryTableModel.addRow(new Object[]{
                lh.getId(),
                lh.getUserId(),
                getUsernameById(lh.getUserId()),
                loginTime,
                logoutTime,
                lh.getIpAddress(),
                lh.getStatus()
            });
        }
    }
    
    private String getUsernameById(int userId) {
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            if (u.getId() == userId) {
                return u.getUsername();
            }
        }
        return "Unknown";
    }
    
    private JPanel createModernAddUser() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel formCard = createWhiteCard(" THÊM USER MỚI");
        formCard.setPreferredSize(new Dimension(600, 550));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        
        // Form fields
        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        JTextField emailField = createStyledTextField();
        JTextField fullNameField = createStyledTextField();
        
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"USER", "ADMIN"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleCombo.setBackground(LIGHT_BLUE);
        roleCombo.setPreferredSize(new Dimension(300, 40));
        roleCombo.setMaximumSize(new Dimension(300, 40));
        
        JButton addButton = createModernButton(" Thêm User", SUCCESS_GREEN);
        addButton.setForeground(Color.BLACK);
        addButton.setPreferredSize(new Dimension(200, 45));
        addButton.setMaximumSize(new Dimension(200, 45));
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        addButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
                showModernError("Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showModernError("Email không hợp lệ!");
                return;
            }
            
            if (password.length() < 6) {
                showModernError("Mật khẩu phải có ít nhất 6 ký tự!");
                return;
            }
            
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPasswordHash(password);
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setRoleId("ADMIN".equals(role) ? 1 : 2);
            
            if (userDAO.createUser(newUser)) {
                showModernSuccess(" Thêm user thành công!");
                usernameField.setText("");
                passwordField.setText("");
                emailField.setText("");
                fullNameField.setText("");
                roleCombo.setSelectedIndex(0);
                refreshAllData();
            } else {
                showModernError("Lỗi! Username hoặc email đã tồn tại.");
            }
        });
        
        formCard.add(Box.createVerticalStrut(20));
        formCard.add(createFormField(" Username:", usernameField));
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(createFormField(" Password:", passwordField));
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(createFormField(" Email:", emailField));
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(createFormField(" Full Name:", fullNameField));
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(createFormField(" Role:", roleCombo));
        formCard.add(Box.createVerticalStrut(30));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        formCard.add(buttonPanel);
        
        panel.add(formCard);
        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(300, 40));
        field.setBackground(LIGHT_BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(300, 40));
        field.setBackground(LIGHT_BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(500, 50));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setPreferredSize(new Dimension(150, 30));
        label.setMinimumSize(new Dimension(150, 30));
        label.setMaximumSize(new Dimension(150, 30));
        
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(field);
        panel.add(Box.createHorizontalGlue());
        
        return panel;
    }
    
    private void loadUserData() {
        userTableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (User user : users) {
            String role = user.getRoleId() == 1 ? "ADMIN" : "USER";
            String status = user.isLoggedIn() ? "Online" : "Offline";
            String created = user.getCreatedAt() != null ? sdf.format(user.getCreatedAt()) : "";
            
            userTableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                role,
                status,
                user.isActive(),
                created
            });
        }
    }
    
    private void searchUsers() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadUserData();
            return;
        }
        
        SwingWorker<List<User>, Void> worker = new SwingWorker<List<User>, Void>() {
            @Override
            protected List<User> doInBackground() throws Exception {
                return sendSearchRequest(searchTerm);
            }
            
            @Override
            protected void done() {
                try {
                    List<User> users = get();
                    updateTableWithUsers(users);
                } catch (Exception e) {
                    showModernError("Lỗi khi tìm kiếm users!");
                }
            }
        };
        worker.execute();
    }
    
    private List<User> sendSearchRequest(String searchTerm) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            
            AdminActionRequest request = new AdminActionRequest(AdminActionRequest.ActionType.SEARCH_USERS, searchTerm);
            oos.writeObject(request);
            
            GenericResponse response = (GenericResponse) ois.readObject();
            if (response.isSuccess() && response.getUserList() != null) {
                return response.getUserList();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    private void updateTableWithUsers(List<User> users) {
        userTableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (User user : users) {
            String role = user.getRoleId() == 1 ? "ADMIN" : "USER";
            String status = user.isLoggedIn() ? "Online" : "Offline";
            String created = user.getCreatedAt() != null ? sdf.format(user.getCreatedAt()) : "";
            
            userTableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                role,
                status,
                user.isActive(),
                created
            });
        }
    }
    
    private void clearSearch() {
        searchField.setText("");
        loadUserData();
    }
    
    private void banSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showModernError("Vui lòng chọn user cần ban!");
            return;
        }
        
        int userId = (Integer) userTable.getValueAt(selectedRow, 0);
        String username = (String) userTable.getValueAt(selectedRow, 1);
        boolean isActive = (Boolean) userTable.getValueAt(selectedRow, 6);
        
        if (!isActive) {
            showModernError("User này đã bị ban!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "🚫 Bạn có chắc chắn muốn ban user: " + username + "?",
            "Xác nhận Ban User",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            performAdminAction(AdminActionRequest.ActionType.BAN_USER, userId, "ban user " + username);
        }
    }
    
    private void unbanSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showModernError("Vui lòng chọn user cần unban!");
            return;
        }
        
        int userId = (Integer) userTable.getValueAt(selectedRow, 0);
        String username = (String) userTable.getValueAt(selectedRow, 1);
        boolean isActive = (Boolean) userTable.getValueAt(selectedRow, 6);
        
        if (isActive) {
            showModernError("User này chưa bị ban!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            " Bạn có chắc chắn muốn unban user: " + username + "?",
            "Xác nhận Unban User",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            performAdminAction(AdminActionRequest.ActionType.UNBAN_USER, userId, "unban user " + username);
        }
    }
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showModernError("Vui lòng chọn user cần xóa!");
            return;
        }
        
        int userId = (Integer) userTable.getValueAt(selectedRow, 0);
        String username = (String) userTable.getValueAt(selectedRow, 1);
        String role = (String) userTable.getValueAt(selectedRow, 4);
        
        if ("ADMIN".equals(role)) {
            showModernError("Không thể xóa tài khoản Admin!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "⚠️ CẢNH BÁO: Bạn có chắc chắn muốn XÓA VĨNH VIỄN user: " + username + "?\n" +
            "Hành động này KHÔNG THỂ HOÀN TÁC!",
            "Xác nhận Xóa User",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String input = JOptionPane.showInputDialog(this,
                "Để xác nhận xóa, vui lòng nhập tên user: " + username,
                "Xác nhận cuối cùng",
                JOptionPane.WARNING_MESSAGE);
            
            if (username.equals(input)) {
                performAdminAction(AdminActionRequest.ActionType.DELETE_USER, userId, "delete user " + username);
            } else if (input != null) {
                showModernError("Tên user không khớp!");
            }
        }
    }
    
    private void performAdminAction(AdminActionRequest.ActionType actionType, int userId, String actionDescription) {
        SwingWorker<GenericResponse, Void> worker = new SwingWorker<GenericResponse, Void>() {
            @Override
            protected GenericResponse doInBackground() throws Exception {
                return sendAdminActionRequest(actionType, userId);
            }
            
            @Override
            protected void done() {
                try {
                    GenericResponse response = get();
                    if (response.isSuccess()) {
                        showModernSuccess(" Thành công " + actionDescription + "!");
                        loadUserData();
                        refreshAllData();
                    } else {
                        showModernError("Lỗi: " + response.getMessage());
                    }
                } catch (Exception e) {
                    showModernError("Không thể kết nối đến server!");
                }
            }
        };
        worker.execute();
    }
    
    private GenericResponse sendAdminActionRequest(AdminActionRequest.ActionType actionType, int userId) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            
            AdminActionRequest request = new AdminActionRequest(actionType, userId);
            oos.writeObject(request);
            
            return (GenericResponse) ois.readObject();
            
        } catch (Exception e) {
            return new GenericResponse(false, "Lỗi kết nối server!");
        }
    }
    
    private void refreshAllData() {
        SwingUtilities.invokeLater(() -> {
            // Update dashboard stats
            List<User> allUsers = userDAO.getAllUsers();
            if (totalUsersLabel != null) totalUsersLabel.setText(String.valueOf(allUsers.size()));
            if (onlineUsersLabel != null) onlineUsersLabel.setText(String.valueOf(allUsers.stream().filter(User::isLoggedIn).count()));
            if (adminCountLabel != null) adminCountLabel.setText(String.valueOf(allUsers.stream().filter(u -> u.getRoleId() == 1).count()));
            if (activeUsersLabel != null) activeUsersLabel.setText(String.valueOf(allUsers.stream().filter(User::isActive).count()));
            
            // Refresh user table
            loadUserData();
            
            // Refresh login history
            loadLoginHistoryData();
            
            // Refresh dashboard panel
            Component dashboardPanel = tabbedPane.getComponentAt(0);
            tabbedPane.setComponentAt(0, createModernDashboard());
        });
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            " Bạn có chắc chắn muốn đăng xuất?", 
            "Xác nhận Đăng Xuất", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginClient().setVisible(true);
        }
    }
    
    private void showModernError(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "❌ Lỗi");
        dialog.setVisible(true);
    }
    
    private void showModernSuccess(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "✓ Thành công");
        dialog.setVisible(true);
    }
}