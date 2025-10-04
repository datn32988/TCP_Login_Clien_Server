
package Login.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Login.DatabaseConnection;
import Login.model.LoginHistory;

public class LoginHistoryDAO {
    
    public boolean recordLogin(int userId, String ipAddress) {
        String sql = "INSERT INTO login_history (user_id, login_time, ip_address, status) VALUES (?, CURRENT_TIMESTAMP, ?, 'LOGIN')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, ipAddress);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean recordLogout(int userId) {
        String sql = "UPDATE login_history " +
                     "SET logout_time = CURRENT_TIMESTAMP, status = 'LOGOUT' " +
                     "WHERE id = (" +
                     "   SELECT id FROM login_history " +
                     "   WHERE user_id = ? AND logout_time IS NULL " +
                     "   ORDER BY login_time DESC LIMIT 1" +
                     ")";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public List<LoginHistory> getLoginHistory() {
        List<LoginHistory> history = new ArrayList<>();
        String sql = "SELECT lh.*, u.username FROM login_history lh " +
                    "JOIN users u ON lh.user_id = u.id " +
                    "ORDER BY lh.login_time DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LoginHistory lh = new LoginHistory();
                lh.setId(rs.getInt("id"));
                lh.setUserId(rs.getInt("user_id"));
                lh.setLoginTime(rs.getTimestamp("login_time"));
                lh.setLogoutTime(rs.getTimestamp("logout_time"));
                lh.setIpAddress(rs.getString("ip_address"));
                lh.setStatus(rs.getString("status"));
                history.add(lh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
}