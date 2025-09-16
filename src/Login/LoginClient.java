package Login;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class LoginClient {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    public boolean connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến server");
            return false;
        }
    }
    
    public String login(String username, String password) {
        try {
            oos.writeObject("LOGIN");
            oos.writeObject(username);
            oos.writeObject(password);
            oos.flush();
            
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return "ERROR";
        }
    }
    
    public String register(String username, String password, String email, String fullName) {
        try {
            oos.writeObject("REGISTER");
            oos.writeObject(username);
            oos.writeObject(password);
            oos.writeObject(email);
            oos.writeObject(fullName);
            oos.flush();
            
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return "ERROR";
        }
    }
    
    public void logout(String username) {
        try {
            oos.writeObject("LOGOUT");
            oos.writeObject(username);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String changePassword(int userId, String currentPassword, String newPassword) {
        try {
            oos.writeObject("CHANGE_PASSWORD");
            oos.writeObject(userId);
            oos.writeObject(currentPassword);
            oos.writeObject(newPassword);
            oos.flush();
            
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return "ERROR";
        }
    }
    
    public String updateProfile(int userId, String email, String fullName) {
        try {
            oos.writeObject("UPDATE_PROFILE");
            oos.writeObject(userId);
            oos.writeObject(email);
            oos.writeObject(fullName);
            oos.flush();
            
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return "ERROR";
        }
    }
    
    public User getUserInfo(int userId) {
        try {
            oos.writeObject("GET_USER_INFO");
            oos.writeObject(userId);
            oos.flush();
            
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    public User getUserByUsername(String username) {
        try {
            // Kết nối đến server để lấy thông tin user
            if (!connectToServer("localhost", 12345)) {
                return null;
            }
            
            oos.writeObject("GET_USER_BY_USERNAME");
            oos.writeObject(username);
            oos.flush();
            
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    public void disconnect() {
        try {
            if (oos != null) oos.close();
            if (ois != null) ois.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}