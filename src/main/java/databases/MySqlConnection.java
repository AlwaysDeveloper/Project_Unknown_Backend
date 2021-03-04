package databases;

import uitls.JsonHandler;

import java.sql.*;
import java.util.HashMap;

public class MySqlConnection {
    protected String URL = "jdbc:mysql://localhost:3306/";
    protected String database = "erp_v1";
    protected String username = "root";
    protected String password = "M@nvi0712";

    public Connection connection;

    public Connection MySqlConnection()throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(URL+database+"?useSSL=false", username, password);
        return connection;
    }

    public HashMap RootUserInfo(String rootUser) throws Exception {
        PreparedStatement preparedStatement = MySqlConnection().prepareStatement("SELECT * FROM root_table WHERE root_username = ?", PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, rootUser);
        preparedStatement.executeQuery();
        ResultSet set = preparedStatement.getResultSet();
        HashMap map = new HashMap();
        if(set.next()){
            JsonHandler handler = new JsonHandler();
            for(int i=0; i< set.getMetaData().getColumnCount(); i++){
                if(set.getString(set.getMetaData().getColumnName(i+1)) == null) continue;
                if(set.getMetaData().getColumnName(i+1).indexOf("object") > -1){
                    HashMap submap = new HashMap();
                    handler.jsonToHashMap(set.getString(set.getMetaData().getColumnName(i+1)), submap);
                    map.put(set.getMetaData().getColumnName(i+1).toUpperCase(), submap);
                    continue;
                }
                map.put(set.getMetaData().getColumnName(i+1).toUpperCase(), set.getString(set.getMetaData().getColumnName(i+1)));
            }
        }else {
            preparedStatement.close();
            set.close();
            return null;
        }

        return map;
    }
}
