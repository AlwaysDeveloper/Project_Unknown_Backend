package context;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controllers.User;

import java.io.*;

public class APIContext extends Thread{
    User user = new User();
    public APIContext(HttpServer server) throws IOException {
        server.createContext("/", httpExchange -> {
            new Thread(() -> {
                try {
                    Index(httpExchange);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        server.createContext("/java_api/_v1/user/login", httpExchange ->  { new Thread(user.Login(httpExchange)).start(); });
        server.createContext("/java_api/_v1/user/create", httpExchange -> { new Thread(user.Create(httpExchange)).start(); });
        server.createContext("/java_api/_v1/user/getUser", httpExchange -> { new Thread(user.GetUser(httpExchange)).start(); });
    }

    private void Index(HttpExchange httpExchange) throws IOException {

        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");

        httpExchange.getResponseHeaders().set("Location", "http://localhost:4200/");
        httpExchange.sendResponseHeaders(302, -1);
        httpExchange.close();
    }

    public String getFileExtension(String fileName){
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
