package context;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controllers.User;
import services.mailer.MailerService;

public class APIContext extends Thread{
    User user = new User();
    MailerService mailerService = new MailerService();
    public APIContext(HttpServer server) {
        server.createContext("/services", httpExchange -> {
            new Thread(Index(httpExchange)).start();
        });
        server.createContext("/backend/_v1/user/login", httpExchange ->  { new Thread(user.Login(httpExchange)).start(); });
        server.createContext("/backend/_v1/user/create", httpExchange -> { new Thread(user.Create(httpExchange)).start(); });
        server.createContext("/backend/_v1/user/getUser", httpExchange -> { new Thread(user.GetUser(httpExchange)).start(); });
        server.createContext("/backend/_v1/user/createInMass", httpExchange -> { new Thread(user.createUserInMass(httpExchange)).start(); });
    }

    private Runnable Index(HttpExchange httpExchange) {
        return () -> {
            try {
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");

                httpExchange.getResponseHeaders().set("Location", "http://localhost:4200/");
                httpExchange.sendResponseHeaders(302, -1);
                httpExchange.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        };
    }

    public String getFileExtension(String fileName){
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
