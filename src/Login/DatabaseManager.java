package Login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	 private static final String DB_URL = "jdbc:postgresql://localhost:5432/login_db";
	 private static final String DB_USER = "postgres";
	 private static final String DB_PASSWORD = "123";

	    // Hàm lấy kết nối
	  public static Connection getConnection() throws SQLException {
	      return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	  }
}
