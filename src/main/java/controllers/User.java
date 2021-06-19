package controllers;

import com.sun.net.httpserver.HttpExchange;
import databases.MySQLQueries.UserQueries;
import databases.MySqlConnection;
import uitls.Auth;
import uitls.HttpExchangeHandler;
import uitls.JsonHandler;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class User{
    private JsonHandler jsonHandler = new JsonHandler();
    private HttpExchangeHandler handler = new HttpExchangeHandler();
    private MySqlConnection connection = new MySqlConnection();
    private Auth auth = new Auth();
    public Runnable Login(HttpExchange httpExchange){
        return () -> {
            try {
                JsonObject data = handler.DataReader(httpExchange.getRequestBody());
                models.User user = new models.User();
                jsonHandler.jsonToObject(data.toString(), user);
                String inputPassword = user.getPassword();
                new UserQueries().getUser(connection.MySqlConnection(), user);
                if(user.getIsExist() && auth.isPasswordCorrect(user.getPassword(), inputPassword)){
                    user.setPassword(null);
                    String token = auth.JsonWebToken_create(Integer.toString(user.getId()), user.getUsername());
                    handler.setCookies(httpExchange, "Bearer", token);
                    handler.SendResponse(httpExchange, 200, jsonHandler.objectToJson(user));
                }else {
                    handler.SendResponse(httpExchange, 400, new Exception("Password or Username is incorrect").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Runnable Create(HttpExchange httpExchange){
        Runnable runnable = () -> {
            try{
                models.User newUser = new models.User();
                String jsonDataString = handler.DataReader(httpExchange.getRequestBody()).toString();
                System.out.println(jsonDataString);
                jsonHandler.jsonToObject(jsonDataString, newUser);
                newUser.setPassword(auth.BcryptHashPassword(newUser.getPassword()));
                HashMap result = new UserQueries().createUser(connection.MySqlConnection(),newUser);
                if(Integer.parseInt(result.get("result").toString()) <= 0) handler.SendResponse(httpExchange, 400, new Exception("Data is not saved, something went wrong").toString());
                String token = auth.JsonWebToken_create(result.get("id").toString(), newUser.getUsername());
                String response = jsonHandler.objectToJson(newUser);
                handler.setCookies(httpExchange, "Bearer", token);
                handler.SendResponse(httpExchange, 200, jsonHandler.addNewProperty(response, "token", token));
            }catch (Exception exception){
                exception.printStackTrace();
            }
        };
        return runnable;
    }

    public Runnable GetUser(HttpExchange httpExchange){
        Runnable runnable = () -> {
            Set Req = httpExchange.getRequestHeaders().keySet();
            for(Object Key: Req){
                System.out.println(httpExchange.getRequestHeaders().get(Key));
            }
            Set Res = httpExchange.getResponseHeaders().keySet();
            for(Object Key: Res){
                System.out.println(httpExchange.getResponseHeaders().get(Key));
            }
            for(String key: httpExchange.getRequestURI().getRawQuery().split("&")){
                System.out.println(key);
            }
            try {
                handler.SendResponse(httpExchange, 200, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return runnable;
    }
}
