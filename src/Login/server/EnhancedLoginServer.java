package Login.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Login.dao.UserDAO;
import Login.dao.LoginHistoryDAO;
import Login.dto.*;
import Login.model.User;

public class EnhancedLoginServer {
    private static final int PORT = 12345;
    private ExecutorService threadPool;
    private UserDAO userDAO;
    private LoginHistoryDAO loginHistoryDAO;

    public EnhancedLoginServer() {
        threadPool = Executors.newFixedThreadPool(20); // Increased thread pool
        userDAO = new UserDAO();
        loginHistoryDAO = new LoginHistoryDAO();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Enhanced Login Server started on port " + PORT);
            System.out.println("Server ready to handle:");
            System.out.println("- Login/Logout");
            System.out.println("- User Registration");
            System.out.println("- Password Change");
            System.out.println("- Profile Updates");
            System.out.println("- Admin Actions (Ban/Unban/Delete)");
            System.out.println("- User Search");
            System.out.println("=".repeat(50));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
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
                String clientIP = clientSocket.getInetAddress().getHostAddress();

                System.out.println("Received request from " + clientIP + ": " + request.getClass().getSimpleName());

                if (request instanceof LoginRequest) {
                    handleLogin((LoginRequest) request, oos, clientIP);
                } else if (request instanceof RegisterRequest) {
                    handleRegister((RegisterRequest) request, oos);
                } else if (request instanceof ChangePasswordRequest) {
                    handleChangePassword((ChangePasswordRequest) request, oos);
                } else if (request instanceof UpdateProfileRequest) {
                    handleUpdateProfile((UpdateProfileRequest) request, oos);
                } else if (request instanceof AdminActionRequest) {
                    handleAdminAction((AdminActionRequest) request, oos);
                } else {
                    // Unknown request type
                    GenericResponse errorResponse = new GenericResponse(false, "Unknown request type");
                    oos.writeObject(errorResponse);
                }

            } catch (Exception e) {
                System.err.println("Error handling client request: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private void handleLogin(LoginRequest request, ObjectOutputStream oos, String clientIP) throws IOException {
            System.out.println("Processing login for user: " + request.getUsername());
            
            User user = userDAO.authenticateUser(request.getUsername(), request.getPassword());

            if (user != null) {
                if (!user.isActive()) {
                    LoginResponse response = new LoginResponse(false, "Tài khoản của bạn đã bị khóa!");
                    oos.writeObject(response);
                    System.out.println("Login failed for " + request.getUsername() + ": Account banned");
                    return;
                }

                // Update login status and record login history
                userDAO.updateLoginStatus(user.getId(), true);
                loginHistoryDAO.recordLogin(user.getId(), clientIP);

                // Get role name
                String roleName = getRoleName(user.getRoleId());

                LoginResponse response = new LoginResponse(true, "Đăng nhập thành công!", user, roleName);
                oos.writeObject(response);
                System.out.println("Login successful for " + request.getUsername() + " (Role: " + roleName + ")");
            } else {
                LoginResponse response = new LoginResponse(false, "Tên đăng nhập hoặc mật khẩu không đúng!");
                oos.writeObject(response);
                System.out.println("Login failed for " + request.getUsername() + ": Invalid credentials");
            }
        }

        private void handleRegister(RegisterRequest request, ObjectOutputStream oos) throws IOException {
            System.out.println("Processing registration for user: " + request.getUsername());
            
            // Validate input
            if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty() ||
                request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                request.getFullName() == null || request.getFullName().trim().isEmpty()) {
                
                GenericResponse response = new GenericResponse(false, "Vui lòng điền đầy đủ thông tin!");
                oos.writeObject(response);
                return;
            }

            // Check if username or email already exists
            if (userDAO.isUsernameExists(request.getUsername())) {
                GenericResponse response = new GenericResponse(false, "Tên đăng nhập đã tồn tại!");
                oos.writeObject(response);
                System.out.println("Registration failed for " + request.getUsername() + ": Username exists");
                return;
            }

            if (userDAO.isEmailExists(request.getEmail())) {
                GenericResponse response = new GenericResponse(false, "Email đã được sử dụng!");
                oos.writeObject(response);
                System.out.println("Registration failed for " + request.getUsername() + ": Email exists");
                return;
            }

            // Create new user
            boolean success = userDAO.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getFullName()
            );

            if (success) {
                GenericResponse response = new GenericResponse(true, "Đăng ký thành công!");
                oos.writeObject(response);
                System.out.println("Registration successful for " + request.getUsername());
            } else {
                GenericResponse response = new GenericResponse(false, "Lỗi khi tạo tài khoản!");
                oos.writeObject(response);
                System.out.println("Registration failed for " + request.getUsername() + ": Database error");
            }
        }

        private void handleChangePassword(ChangePasswordRequest request, ObjectOutputStream oos) throws IOException {
            System.out.println("Processing password change for user ID: " + request.getUserId());
            
            boolean success = userDAO.changePassword(
                request.getUserId(),
                request.getOldPassword(),
                request.getNewPassword()
            );

            if (success) {
                GenericResponse response = new GenericResponse(true, "Thay đổi mật khẩu thành công!");
                oos.writeObject(response);
                System.out.println("Password change successful for user ID: " + request.getUserId());
            } else {
                GenericResponse response = new GenericResponse(false, "Mật khẩu hiện tại không đúng!");
                oos.writeObject(response);
                System.out.println("Password change failed for user ID: " + request.getUserId());
            }
        }

        private void handleUpdateProfile(UpdateProfileRequest request, ObjectOutputStream oos) throws IOException {
            System.out.println("Processing profile update for user ID: " + request.getUserId());
            
            // Validate email format
            if (!isValidEmail(request.getEmail())) {
                GenericResponse response = new GenericResponse(false, "Email không hợp lệ!");
                oos.writeObject(response);
                return;
            }

            boolean success = userDAO.updateProfile(
                request.getUserId(),
                request.getEmail(),
                request.getFullName()
            );

            if (success) {
                GenericResponse response = new GenericResponse(true, "Cập nhật profile thành công!");
                oos.writeObject(response);
                System.out.println("Profile update successful for user ID: " + request.getUserId());
            } else {
                GenericResponse response = new GenericResponse(false, "Lỗi khi cập nhật profile!");
                oos.writeObject(response);
                System.out.println("Profile update failed for user ID: " + request.getUserId());
            }
        }

        private void handleAdminAction(AdminActionRequest request, ObjectOutputStream oos) throws IOException {
            System.out.println("Processing admin action: " + request.getActionType());
            
            GenericResponse response;
            
            switch (request.getActionType()) {
                case BAN_USER:
                    boolean banSuccess = userDAO.banUser(request.getTargetUserId(), true);
                    if (banSuccess) {
                        response = new GenericResponse(true, "Đã ban user thành công!");
                        System.out.println("User banned: ID " + request.getTargetUserId());
                    } else {
                        response = new GenericResponse(false, "Lỗi khi ban user!");
                        System.out.println("Failed to ban user: ID " + request.getTargetUserId());
                    }
                    break;
                    
                case UNBAN_USER:
                    boolean unbanSuccess = userDAO.banUser(request.getTargetUserId(), false);
                    if (unbanSuccess) {
                        response = new GenericResponse(true, "Đã unban user thành công!");
                        System.out.println("User unbanned: ID " + request.getTargetUserId());
                    } else {
                        response = new GenericResponse(false, "Lỗi khi unban user!");
                        System.out.println("Failed to unban user: ID " + request.getTargetUserId());
                    }
                    break;
                    
                case DELETE_USER:
                    boolean deleteSuccess = userDAO.deleteUser(request.getTargetUserId());
                    if (deleteSuccess) {
                        response = new GenericResponse(true, "Đã xóa user thành công!");
                        System.out.println("User deleted: ID " + request.getTargetUserId());
                    } else {
                        response = new GenericResponse(false, "Lỗi khi xóa user!");
                        System.out.println("Failed to delete user: ID " + request.getTargetUserId());
                    }
                    break;
                    
                case SEARCH_USERS:
                    java.util.List<User> searchResults = userDAO.searchUsers(request.getSearchTerm());
                    response = new GenericResponse(true, "Tìm kiếm thành công!", searchResults);
                    System.out.println("Search completed for term: " + request.getSearchTerm() + 
                                     ", found " + searchResults.size() + " results");
                    break;
                    
                default:
                    response = new GenericResponse(false, "Hành động không được hỗ trợ!");
                    System.out.println("Unsupported admin action: " + request.getActionType());
                    break;
            }
            
            oos.writeObject(response);
        }

        private String getRoleName(int roleId) {
            return roleId == 1 ? "ADMIN" : "USER";
        }

        private boolean isValidEmail(String email) {
            return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Enhanced Login Server...");
        System.out.println("Features enabled:");
        System.out.println("✓ User Authentication");
        System.out.println("✓ User Registration");
        System.out.println("✓ Password Management");
        System.out.println("✓ Profile Management");
        System.out.println("✓ Admin Controls");
        System.out.println("✓ User Search");
        System.out.println("✓ Login History Tracking");
        System.out.println();
        
        new EnhancedLoginServer().start();
    }
}