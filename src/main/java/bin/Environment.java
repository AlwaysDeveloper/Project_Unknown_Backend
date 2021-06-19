package bin;

import java.util.HashMap;

public class Environment {
    public Object MySQL;
    public Object MongoDB;
    public Object Services;

    public void setMySQL(Object mySQL) {
        MySQL = mySQL;
    }

    public void setMongoDB(HashMap mongoDB) {
        MongoDB = mongoDB;
    }

    public void setServices(HashMap services) {
        Services = services;
    }
}
