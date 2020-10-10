package util;

import javafx.scene.control.Button;

public class UserTM {

    private String userId;
    private String name;
    private String contactNumber;
    private String email;
    private String username;
    private String password;
    private String role;
    private String location;

    public UserTM() {
    }

    public UserTM(String userId, String name, String contactNumber, String email, String username, String password, String role, String location) {
        this.userId = userId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
