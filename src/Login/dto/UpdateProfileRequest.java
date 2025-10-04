package Login.dto;

import java.io.Serializable;

public class UpdateProfileRequest implements Serializable {
    private int userId;
    private String email;
    private String fullName;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(int userId, String email, String fullName) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
    }

    // getters/setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}

