package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author mathiasjepsen
 */
public class Server {
    
    private final String IP = "0.0.0.0";
    private final int PORT = 8081;
    private final Map<String, ClientHandler> clients = new HashMap();
    
    private void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(IP, PORT));
        
        ExecutorService es = Executors.newCachedThreadPool();
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            es.execute(new ClientHandler(clientSocket, this));
        }
    }
    
    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    public Map<String, ClientHandler> getClients() {
        return clients;
    }
    
}
