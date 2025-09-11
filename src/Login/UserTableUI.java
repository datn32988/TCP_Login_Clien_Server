package Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class UserTableUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public UserTableUI() {
        setTitle("Danh sách người dùng");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240)); // A light gray background

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Add some padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Danh sách người dùng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Tên đăng nhập", "Mật khẩu"}, 0);
        table = new JTable(model);


        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(173, 216, 230));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(70, 130, 180)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        ((JTableHeader) table.getTableHeader()).setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, 40));


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        loadUsers();
    }

    private void loadUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();

        model.setRowCount(0); 
        for (User u : users) {
            model.addRow(new Object[]{u.getId(), u.getUsername(), u.getPasswordHash()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserTableUI().setVisible(true);
        });
    }
}