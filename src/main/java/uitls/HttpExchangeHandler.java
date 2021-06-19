package uitls;

import com.sun.net.httpserver.HttpExchange;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;

public class HttpExchangeHandler {

    private static void setHttpResponseHeadersForCORS(HttpExchange exchange){
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");
    }

    public JsonObject DataReader(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8")
        );
        StringBuilder stringBuilder = new StringBuilder();
        String dataString ="";
        while ((dataString = bufferedReader.readLine()) != null){
            stringBuilder.append(dataString);
        }
        bufferedReader.close();
        dataString = stringBuilder.toString();
        JsonReader jsonReader = Json.createReader(new StringReader(dataString));
        return jsonReader.readObject();
    }

    public void SendResponse(HttpExchange exchange, int responseCode, String responseString) throws IOException {
        setHttpResponseHeadersForCORS(exchange);
        exchange.sendResponseHeaders(responseCode, responseString.length());
        exchange.getResponseBody().write(responseString.getBytes());
        exchange.close();
    }

    public void setCookies(HttpExchange exchange, String cookie, String value){
        exchange.getResponseHeaders().add("Set-Cookie", (cookie + "=" + value + "," + "HttpOnly=true"));
    }
}
