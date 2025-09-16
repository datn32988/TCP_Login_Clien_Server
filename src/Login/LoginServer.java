package Login;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.List;

public class LoginServer {
    private static final int PORT = 12345;
    private UserDAO userDAO;
    
    public LoginServer() {
        userDAO = new UserDAO();
    }
    
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy trên port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client kết nối: " + clientSocket.getInetAddress());
                
                new ClientHandler(clientSocket, userDAO).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new LoginServer().start();
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private UserDAO userDAO;
    
    public ClientHandler(Socket socket, UserDAO userDAO) {
        this.clientSocket = socket;
        this.userDAO = userDAO;
    }
    
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            
            while (true) {
                String request = (String) ois.readObject();
                
                if (request.equals("LOGIN")) {
                    // Xử lý đăng nhập
                    String username = (String) ois.readObject();
                    String password = (String) ois.readObject();
                    
                    System.out.println("Login attempt: " + username);
                    
                    User user = userDAO.authenticateUser(username, password);
                    
                    if (user != null) {
                        userDAO.updateLoginStatus(user.getId(), true);
                        oos.writeObject("SUCCESS");
                        oos.writeObject(user.getFullName());
                        oos.writeObject(user.getEmail());
                    } else {
                        oos.writeObject("FAIL");
                    }
                    oos.flush();
                    
                } else if (request.equals("REGISTER")) {
                    // Xử lý đăng ký
                    String username = (String) ois.readObject();
                    String password = (String) ois.readObject();
                    String email = (String) ois.readObject();
                    String fullName = (String) ois.readObject();
                    
                    System.out.println("Register attempt: " + username);
                    
                    if (userDAO.isEmailExists(username)) {
                        oos.writeObject("USERNAME_EXISTS");
                    } else if (userDAO.isEmailExists(email)) {
                        oos.writeObject("EMAIL_EXISTS");
                    } else if (userDAO.registerUser(username, password, email, fullName)) {
                        oos.writeObject("REGISTER_SUCCESS");
                    } else {
                        oos.writeObject("REGISTER_FAIL");
                    }
                    oos.flush();
                    
                } else if (request.equals("LOGOUT")) {
                	String username = (String) ois.readObject();
                    Integer userId = userDAO.getUserIdByUsername(username);
                    if (userId != null) {
                        userDAO.updateLoginStatus(userId, false);
                        oos.writeObject("LOGOUT_SUCCESS");
                    } else {
                        oos.writeObject("LOGOUT_FAIL");
                    }
                    oos.flush();
                    
                } else if (request.equals("GET_ALL_USERS")) {
                    // Lấy danh sách tất cả users (cho admin)
                    List<User> users = userDAO.getAllUsers();
                    oos.writeObject(users);
                    oos.flush();
                    
                } else if (request.equals("DELETE_USER")) {
                    int userId = (Integer) ois.readObject();
                    boolean result = userDAO.deleteUser(userId);
                    oos.writeObject(result ? "DELETE_SUCCESS" : "DELETE_FAIL");
                    oos.flush();
                    }
                  else if (request.equals("CHANGE_PASSWORD")) {
                        // Xử lý đổi mật khẩu
                        int userId = (Integer) ois.readObject();
                        String currentPassword = (String) ois.readObject();
                        String newPassword = (String) ois.readObject();
                        
                        // Kiểm tra mật khẩu hiện tại
                        User user = userDAO.getUserById(userId);
                        if (user != null && user.getPasswordHash().equals(currentPassword)) {
                            boolean result = userDAO.updatePassword(userId, newPassword);
                            oos.writeObject(result ? "PASSWORD_CHANGE_SUCCESS" : "PASSWORD_CHANGE_FAIL");
                        } else {
                            oos.writeObject("CURRENT_PASSWORD_INCORRECT");
                        }
                        oos.flush();
                        
                    } else if (request.equals("UPDATE_PROFILE")) {
                        // Xử lý cập nhật profile
                        int userId = (Integer) ois.readObject();
                        String email = (String) ois.readObject();
                        String fullName = (String) ois.readObject();
                        
                        // Kiểm tra email trùng (trừ chính user hiện tại)
                        User currentUser = userDAO.getUserById(userId);
                        User existingUser = userDAO.getUserByUsername(email);
                        
                        if (existingUser != null && existingUser.getId() != userId) {
                            oos.writeObject("EMAIL_EXISTS");
                        } else {
                            boolean result = userDAO.updateProfile(userId, email, fullName);
                            oos.writeObject(result ? "PROFILE_UPDATE_SUCCESS" : "PROFILE_UPDATE_FAIL");
                        }
                        oos.flush();
                        
                    } else if (request.equals("GET_USER_INFO")) {
                        // Lấy thông tin user
                        int userId = (Integer) ois.readObject();
                        User user = userDAO.getUserById(userId);
                        oos.writeObject(user);
                        oos.flush();
            
                }
                    else if (request.equals("GET_USER_BY_USERNAME")) {
                        // Lấy thông tin user bằng username
                        String username = (String) ois.readObject();
                        User user = userDAO.getUserByUsername(username);
                        oos.writeObject(user);
                        oos.flush();
                    }
                    else if (request.equals("EXIT")) {
                    break;
                }
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client ngắt kết nối: " + clientSocket.getInetAddress());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}