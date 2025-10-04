package Login.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Login.dao.UserDAO;
import Login.dao.LoginHistoryDAO;
import Login.dto.*;
import Login.model.User;

public class LoginServer {
    private static final int PORT = 12345;
    private ExecutorService threadPool;
    private UserDAO userDAO;
    private LoginHistoryDAO loginHistoryDAO;
    
    public LoginServer() {
        threadPool = Executors.newFixedThreadPool(10);
        userDAO = new UserDAO();
        loginHistoryDAO = new LoginHistoryDAO();
    }
    
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Login Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
                
                Object request = ois.readObject();
                
                if (request instanceof LoginRequest) {
                    handleLogin((LoginRequest) request, oos);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void handleLogin(LoginRequest request, ObjectOutputStream oos) throws IOException {
            User user = userDAO.authenticateUser(request.getUsername(), request.getPassword());
            
            if (user != null) {
                userDAO.updateLoginStatus(user.getId(), true);
                loginHistoryDAO.recordLogin(user.getId(), clientSocket.getInetAddress().getHostAddress());
                
                // Get role name
                String roleName = getRoleName(user.getRoleId());
                
                LoginResponse response = new LoginResponse(true, "Đăng nhập thành công!", user, roleName);
                oos.writeObject(response);
            } else {
                LoginResponse response = new LoginResponse(false, "Tên đăng nhập hoặc mật khẩu không đúng!");
                oos.writeObject(response);
            }
        }
        
        private String getRoleName(int roleId) {
            return roleId == 1 ? "ADMIN" : "USER";
        }
    }
    
    public static void main(String[] args) {
        new LoginServer().start();
    }
}