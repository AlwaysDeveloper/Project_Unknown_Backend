package session;

import models.User;

import java.util.Date;

public class SessionModel {

    private User user;
    private Date expire;
    private Boolean isActive;

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getExpire(){
        return this.expire;
    }

    public Boolean getIsActive(){
        return this.isActive;
    }

    public User getUser(){
        return this.user;
    }
}
