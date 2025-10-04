package Login.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private String fullName;
    private boolean isLoggedIn;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer roleId;
    private boolean isActive;

    public User() {}

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public boolean isLoggedIn() { return isLoggedIn; }
    public void setLoggedIn(boolean loggedIn) { isLoggedIn = loggedIn; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
