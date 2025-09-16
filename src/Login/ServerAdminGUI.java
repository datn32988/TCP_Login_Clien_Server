package Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ServerAdminGUI extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JButton btnDelete;
    private JButton btnStartServer;
    private JButton btnStopServer;
    private JTextArea logArea;
    private LoginServer loginServer;
    private Thread serverThread;
    
    public ServerAdminGUI() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Quản lý Server - Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ SERVER ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        
        // Panel control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnStartServer = new JButton("Khởi động Server");
        btnStopServer = new JButton("Dừng Server");
        btnRefresh = new JButton("Làm mới");
        btnDelete = new JButton("Xóa User");
        
        // Style buttons
        btnStartServer.setBackground(new Color(0, 153, 76));
        btnStopServer.setBackground(new Color(204, 0, 0));
        btnRefresh.setBackground(new Color(51, 153, 255));
        btnDelete.setBackground(new Color(255, 102, 102));
        
        btnStartServer.setForeground(Color.black);
        btnStopServer.setForeground(Color.black);
        btnRefresh.setForeground(Color.black);
        btnDelete.setForeground(Color.black);
        
        controlPanel.add(btnStartServer);
        controlPanel.add(btnStopServer);
        controlPanel.add(btnRefresh);
        controlPanel.add(btnDelete);
        
        // Bảng users
        String[] columnNames = {"ID", "Username", "Email", "Họ tên", "Trạng thái", "Ngày tạo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        
        // Panel log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log Server"));
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logPanel.add(logScrollPane, BorderLayout.CENTER);
        
        // Layout
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(logPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Xử lý sự kiện
        btnStartServer.addActionListener(e -> startServer());
        btnStopServer.addActionListener(e -> stopServer());
        btnRefresh.addActionListener(e -> refreshUserList());
        btnDelete.addActionListener(e -> deleteSelectedUser());
        
        btnStopServer.setEnabled(false);
    }
    
    private void startServer() {
        loginServer = new LoginServer();
        serverThread = new Thread(() -> {
            log("Server đang khởi động...");
            loginServer.start();
        });
        serverThread.start();
        
        btnStartServer.setEnabled(false);
        btnStopServer.setEnabled(true);
        log("Server đã khởi động thành công trên port 12345");
        
        // Load danh sách users
        refreshUserList();
    }
    
    private void stopServer() {
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
            log("Server đã dừng");
        }
        
        btnStartServer.setEnabled(true);
        btnStopServer.setEnabled(false);
    }
    
    private void refreshUserList() {
        // Kết nối trực tiếp đến database để lấy danh sách users
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);
        
        // Thêm dữ liệu mới
        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.isLoggedIn(),
                user.getCreatedAt()
            });
        }
        
        log("Đã làm mới danh sách users. Tổng số: " + users.size());
    }
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn user để xóa");
            return;
        }
        
        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa user '" + username + "'?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            UserDAO userDAO = new UserDAO();
            if (userDAO.deleteUser(userId)) {
                log("Đã xóa user: " + username);
                refreshUserList();
            } else {
                log("Lỗi khi xóa user: " + username);
            }
        }
    }
    
    private void log(String message) {
        logArea.append("[" + java.time.LocalTime.now() + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ServerAdminGUI().setVisible(true);
        });
    }
}