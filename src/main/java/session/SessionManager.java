package session;

import models.User;

import java.util.Date;

public class SessionManager extends Session{

    SessionManager(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    scanSessions();
                }
            }
        }).start();
    }

    private void scanSessions(){
        for(String key: hashMap.keySet()){
            SessionModel _session = hashMap.get(key);
            User _user = _session.getUser();
            Date _expires = _session.getExpire();
            Boolean _isActive = _session.getIsActive();
            if (!this.check_by_active(key, _isActive)) continue;
            else if(!this.check_by_expires(key, _expires)) continue;
        }
    }

    private Boolean check_by_active(String key, Boolean isActive){
        if(isActive || !hashMap.containsKey(key)) return false;
        hashMap.remove(key);
        return true;
    }

    private Boolean check_by_expires(String key, Date expire){
        Long now = new Date().getTime();
        Long then = expire.getTime();
        Long limit = new Long(15 * 60 * 1000);
        if( (now + limit) < then || !hashMap.containsKey(key) ) return false;
        hashMap.remove(key);
        return true;
    }
}
