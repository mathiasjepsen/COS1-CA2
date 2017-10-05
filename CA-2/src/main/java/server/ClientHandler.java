package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.ClientMessageHandler;

/**
 *
 * @author mathiasjepsen
 */
public class ClientHandler implements Runnable {

    private final ClientMessageHandler cmh = new ClientMessageHandler(this);
    BufferedReader fromClient;
    PrintWriter toClient;
    Socket socket;
    Server server;
    Map<String, ClientHandler> connectedClients;
    String name;
    private boolean loggedIn = false;
    private boolean connected = true;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.toClient = new PrintWriter(socket.getOutputStream(), true);
        this.fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.server = server;
        this.connectedClients = server.getClients();
    }

    @Override
    public void run() {
        try {
            while (connected) {               
                String msgFromClient = fromClient.readLine();
                cmh.handleMessage(msgFromClient);
            }
        } catch (NullPointerException ex) {
            connected = false;
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();
    }

    public void close()  {
        connectedClients.remove(name);
        cmh.printClientList();
        try {
            socket.close();
            toClient.close();
            fromClient.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BufferedReader getFromClient() {
        return fromClient;
    }

    public PrintWriter getToClient() {
        return toClient;
    }

    public Socket getSocket() {
        return socket;
    }

    public Server getServer() {
        return server;
    }

    public Map<String, ClientHandler> getConnectedClients() {
        return connectedClients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    
    

}
