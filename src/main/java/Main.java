import bin.Environment;
import com.sun.net.httpserver.HttpServer;
import context.APIContext;
import databases.MySqlConnection;
import services.mailer.MailerService;
import uitls.Auth;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;

public class Main {
    public static Integer port = 4600;
    public static Desktop desktop = java.awt.Desktop.getDesktop();
    public static Environment environment;
    public static MailerService mailerService = new MailerService();
    public static void main(String[] args) throws IOException, URISyntaxException {
//        if(check_is_installed()){
            HttpServer server = HttpServer.create(
                    new InetSocketAddress(port), 10
            );
            new APIContext(server);
            server.start();
            URI uri = new URI("http://app.unknown.java/services");
//            desktop.browse(uri);
        new Thread(mailerService.emailRun()).start();
//        }else{
//            return;
//        }
    }

    public static String getIPAddress() throws Exception{
        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        return socket.getLocalAddress().getHostAddress();
    }

    private static boolean check_is_installed() throws Exception{
        Console console = System.console();
        String username = null;
        String password = null;
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("To start the server please enter your root credentials:");
            username = bufferedReader.readLine();
            System.out.println("Enter the root password:");
            password = bufferedReader.readLine();
        }else{
            console.printf("root username%n");
            char[] usernameArray = console.readPassword("Enter your server root username: ");
            username = new String(usernameArray);
            console.printf("root password%n");
            char[] passwordArray = console.readPassword("Enter your secret password: ");
            password = new String(passwordArray);
        }

        MySqlConnection mySqlConnection = new MySqlConnection();
        HashMap rootUserInfo = mySqlConnection.RootUserInfo(username);
        if (rootUserInfo == null) throw new Exception("Root user is not exist!");
        if (!new Auth().isPasswordCorrect((String) rootUserInfo.get("root_password".toUpperCase()), password)) throw new Exception("Root credentials are incorrect, server cannot start!");
        rootUserInfo.remove("root_password".toUpperCase());
        rootUserInfo.remove("idroot_table".toUpperCase());

        //environment.setMySQL(rootUserInfo.get("ROOT_SQL_DATABASE_URL_OBJECT"));
        System.out.println(rootUserInfo.get("ROOT_SQL_DATABASE_URL_OBJECT"));
        for(Object key: rootUserInfo.keySet()){
            System.out.println(key.toString()+"------------>"+rootUserInfo.get(key).getClass());
        }
        return false;
    }
}


