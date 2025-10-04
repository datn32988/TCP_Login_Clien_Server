package Login.dto;

import java.io.Serializable;
import java.util.List;
import Login.model.User;

public class GenericResponse implements Serializable {
    private boolean success;
    private String message;
    private Object data;
    private List<User> userList;

    public GenericResponse() {}

    public GenericResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public GenericResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public GenericResponse(boolean success, String message, List<User> userList) {
        this.success = success;
        this.message = message;
        this.userList = userList;
    }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    public List<User> getUserList() { return userList; }
    public void setUserList(List<User> userList) { this.userList = userList; }
}