import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.util.UUID;

public class TarantoolServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/user/create", new createUserHandler());
        server.createContext("/user/password/reset", new verifyUserHandler());
        server.createContext("/user/verify", new verifyUserHandler());
        server.createContext("/user/device", new registerDeviceHandler());
        server.createContext("/user/signin", new authenticateHandler());
        server.setExecutor(null);  //creates a default executor
        server.start();
    }

    static class createUserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "{\"status\":\"ok\",\"user_id\":"+NumberGenerator.generateId()+"}";
            System.out.println("createUserHandler : "+response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
static class verifyUserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "{\"status\":\"ok\",\"user_id\":"+NumberGenerator.getGeneratedId()+"}";
            System.out.println("verifyUserHandler : "+response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    static class registerDeviceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            UUID uuid = UUID.randomUUID();
        
            // Get the string representation of the UUID
            String randomUUIDString = uuid.toString();
            
            String response = "{\"status\":\"ok\",\"device_id\":\""+randomUUIDString+"\"}";
             System.out.println("registerDeviceHandler : "+response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class authenticateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String jsonResponse = "{\"status\":\"ok\"," +
                "\"session\":{" +
                    "\"id\": \"session_id_1\"," +
                    "\"expiry_time\":  38734085 " +
                "}," +
                "\"account\":{" +
                    "\"id\": 1 ," +
                    "\"account_number\": \"account_number_1\"" +
                "}," +
                "\"user\":{" +
                    "\"id\": "+NumberGenerator.getGeneratedId()+"," +
                    "\"login\": \"01020574493\"," +
                    "\"password\": \"123456\"," +
                    "\"name\": \"John\"," +
                    "\"name_ar\": \"جون\"," +
                    "\"user_type\": \"CUSTOMER\"," +
                    "\"status\": \"NEW\"," +
                    "\"account_type\": \"CC\"," +
                    "\"change_password_required\":  false,\"tenant\": \"BEE\"}" +
                "}";
        System.out.println("authenticateHandler : "+jsonResponse);
            byte[] jsonResponseBytes = jsonResponse.getBytes("UTF-8");
            int responseLength = jsonResponseBytes.length;

            t.getResponseHeaders().set("Content-Type", "application/json");
            t.sendResponseHeaders(200, responseLength);
            
            OutputStream os = t.getResponseBody();
            os.write(jsonResponseBytes);
            os.close();
        }
    }
    
}