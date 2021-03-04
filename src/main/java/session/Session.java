package session;
import io.jsonwebtoken.Claims;
import models.User;
import uitls.Auth;

import java.util.Date;
import java.util.HashMap;

public class Session {
    private Auth auth = new Auth();
    HashMap<String, SessionModel> hashMap = new HashMap<String, SessionModel>();

    Session(){
        new SessionManager();
    }

    public void create(User user, Claims claims){
        SessionModel _new = new SessionModel();
        _new.setExpire(claims.getExpiration());
        _new.setUser(user);
        _new.setActive(true);
        hashMap.put(auth.BcryptHashPassword(String.valueOf(user.getId())), _new);
    }

    public User get(Integer id) throws Exception {
        String _key = auth.BcryptHashPassword(String.valueOf(id));
        if(!hashMap.containsKey(_key)) throw new Exception("You are not logged in.");
        SessionModel _session = hashMap.get(_key);
        if(_session.getExpire().before(new Date()) || !_session.getIsActive()) throw new Exception("Session Expired");
        return _session.getUser();
    }

    public void delete(Integer id){
        String _key = auth.BcryptHashPassword(String.valueOf(id));
        hashMap.remove(_key);
    }

    public void update(Integer id, Claims claims) throws Exception {
        String _key = auth.BcryptHashPassword(String.valueOf(id));
        if(!hashMap.containsKey(_key))throw new Exception("key not exists");
        SessionModel toUpdate = hashMap.get(_key);
        toUpdate.setExpire(claims.getExpiration());
        hashMap.replace(_key, toUpdate);
    }
}
