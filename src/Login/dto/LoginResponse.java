package Login.dto;

import java.io.Serializable;
import Login.model.User;

public class LoginResponse implements Serializable {
    private boolean success;
    private String message;
    private User user;
    private String roleName;
    
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public LoginResponse(boolean success, String message, User user, String roleName) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.roleName = roleName;
    }
    
    // getters/setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}