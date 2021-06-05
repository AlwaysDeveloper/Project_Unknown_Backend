package context;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controllers.User;
import uitls.HttpExchangeHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

public class APIContext extends Thread{
    private HttpExchangeHandler httpExchangeHandler = new HttpExchangeHandler();
    User user = new User();

    public APIContext(HttpServer server) throws IOException {
        server.createContext("/", httpExchange -> {
            new Thread(() -> {
                try {
                    Index(httpExchange);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
        server.createContext("/java_api/_v1/user/login", httpExchange ->  { new Thread(user.Login(httpExchange)).start(); });
        server.createContext("/java_api/_v1/user/create", httpExchange -> { new Thread(user.Create(httpExchange)).start(); });
        server.createContext("/java_api/_v1/user/getUser", httpExchange -> { new Thread(user.GetUser(httpExchange)).start(); });
    }

    private void Index(HttpExchange httpExchange) throws Exception {
        try {
            Set headres = httpExchange.getRequestHeaders().keySet();
            URL request = new URL("http://app.unknown.node"+httpExchange.getRequestURI());
            HttpURLConnection connection = (HttpURLConnection) request.openConnection();
            for(Object key: headres) {
                connection.setRequestProperty(key.toString(), httpExchange.getRequestHeaders().get(key).get(0));
            }
            httpExchangeHandler.SendResponse(httpExchange, connection.getResponseCode(), httpExchangeHandler.DataReader(connection.getInputStream()).toString());
        }catch (Exception e){
            httpExchangeHandler.SendResponse(httpExchange, 500, e.toString());
        }
    }

    public String getFileExtension(String fileName){
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
