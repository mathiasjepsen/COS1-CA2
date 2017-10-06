package utility;

import java.util.Map;
import server.ClientHandler;

/**
 *
 * @author mathiasjepsen
 */
public class ClientMessageHandler {

    private final ClientHandler ch;

    public ClientMessageHandler(ClientHandler ch) {
        this.ch = ch;
    }

    public synchronized void handleMessage(String msgFromClient) {
        String[] splitMsg = msgFromClient.split(":");
        switch (splitMsg[0]) {
            case "LOGOUT":
                if (ch.isLoggedIn()) {
                    logout(msgFromClient);
                }
                break;
            case "LOGIN":
                login(splitMsg);
                break;
            case "MSG":
                if (ch.isLoggedIn()) {
                    sendMessage(splitMsg);
                }
        }

    }
    
    public void logout(String msgFromClient) {
         if (msgFromClient.substring(msgFromClient.length() - 1).equals(":")) {
             ch.setConnected(false);
         }
    }

    public void login(String[] msgFromClient) {
        String username = msgFromClient[1];

        if (!ch.isLoggedIn()) {

            for (Map.Entry<String, ClientHandler> handler : ch.getConnectedClients().entrySet()) {
                if (username.equals(handler.getKey())) {
                    return;
                }
            }

            if (!username.substring(username.length() - 1).equals(":")) {
                if (!username.contains(",")) {
                    ch.setLoggedIn(true);
                    ch.setName(username);
                    ch.getConnectedClients().put(username, ch);
                    printClientList();
                }
            }

        }

    }

    public void sendMessage(String[] msgFromClient) {
        String message = msgFromClient[2];
        String target = msgFromClient[1];

        if (target.equals("*")) {
            for (Map.Entry<String, ClientHandler> handler : ch.getConnectedClients().entrySet()) {
                handler.getValue().getToClient().println("MSGRES:" + ch.getName() + ":" + message);
            }
            return;
        }

        if (message.contains(",")) {
            return;
        }

        if (!message.substring(message.length() - 1).equals(":")) {
            String[] persons = target.split(",");

            for (String username : persons) {
                if (ch.getConnectedClients().get(username) != null) {
                    ClientHandler otherClient = ch.getConnectedClients().get(username);
                    otherClient.getToClient().println("MSGRES:" + ch.getName() + ":" + message);
                }
            }

        }
    }

    public void printClientList() {
        String msgToClient = "CLIENTLIST: ";

        int i = 0;
        for (Map.Entry<String, ClientHandler> handler : ch.getConnectedClients().entrySet()) {
            i++;
            msgToClient += handler.getKey();
            if (i < ch.getConnectedClients().size()) {
                msgToClient += ",";
            }
        }

        for (Map.Entry<String, ClientHandler> handler : ch.getConnectedClients().entrySet()) {
            handler.getValue().getToClient().println(msgToClient);
        }

    }

}
