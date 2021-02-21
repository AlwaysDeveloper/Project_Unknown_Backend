package models;

public class User {
    public Integer id;
    public String username;
    public String fullname;
    public String accessLevel;
    public String email;
    public String password;
    public String phoneNo;
    public boolean isExist;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullname;
    }

    public Boolean getIsExist(){
        return isExist;
    }

    public String getAccessLevel(){ return accessLevel; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNo() { return phoneNo; }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String name) {
        this.fullname = name;
    }

    public void setAccessLevel(String level){this.accessLevel = level;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public void setExist(Boolean isExist) { this.isExist = isExist; }
}
