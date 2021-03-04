package databases.MySQLQueries;


import models.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserQueries {
    public HashMap createUser(Connection connection, User user) throws SQLException {
        HashMap map = new HashMap();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_accounts (username, fullname, password, email, phoneNo, access_level) VALUES(?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getFullName());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPhoneNo());
        preparedStatement.setString(6, user.getAccessLevel());
        map.put("result", preparedStatement.executeUpdate());
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()){
            map.put("id", resultSet.getString(1));
        }
        preparedStatement.close();
        resultSet.close();
        return map;
    }

    public void getUser(Connection connection, User user) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_accounts WHERE username = ?", PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.executeQuery();
        ResultSet set = preparedStatement.getResultSet();
        if (set.next()){
            Field[] fields = user.getClass().getDeclaredFields();
            for(Field field: fields){
                switch (field.getType().getSimpleName()){
                    case "Integer":
                        field.set(user, set.getInt(field.getName()));
                        break;
                    default:
                        field.set(user, set.getString(field.getName()));
                        break;
                }
            }
            user.setExist(true);
        }else {
            user.setExist(false);
        }
        preparedStatement.close();
        set.close();
    }

    public void queryCreatorByModel(Object object, Boolean create){

        String queryString = "INSERT INTO user_accounts ";
        Field[] fields = object.getClass().getDeclaredFields();
        int length = fields.length;
        int index = 0;
        String valueString = " VALUES";
        while(index < length){
            if(index == 0) {
                queryString += ("("+fields[index].getName());
                valueString += ("(?");
            }
            else if(index == length-1) {
                queryString += (", "+fields[index].getName()+")");
                valueString += (", ?)");
            }
            else {
                queryString += ", "+fields[index].getName();
                valueString += (", ?");
            }
            index++;
        }
        System.out.println(queryString+valueString);
    }
}
