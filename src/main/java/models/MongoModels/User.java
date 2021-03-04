package models.MongoModels;

import org.bson.types.ObjectId;

import javax.mail.Address;
import java.sql.Date;

public class User {
    public ObjectId _id;
    public Boolean active;
    public String fullname;
    public String email;
    public Date dob;
    public String address;
    public String usertype;
    public String department;
    public String password;

    public ObjectId get_id() {
        return _id;
    }

    public Boolean getActive() {
        return active;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public Date getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
