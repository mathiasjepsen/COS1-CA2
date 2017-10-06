package utility;

import client.Client;

/**
 *
 * @author mathiasjepsen
 */
public class ServerMessageHandler {

    private final Client client;

    public ServerMessageHandler(Client client) {
        this.client = client;
    }

    public synchronized void handleServerMessage(String msg) {
        String[] splitMessage = msg.split(":");
        String command = splitMessage[0];
        switch (command) {
            case "MSGRES":
                displayMessage(splitMessage);
                break;
            case "CLIENTLIST":
                displayConnectedClients(splitMessage);
            default:
                //wrong message
                break;
        }
    }

    public void displayMessage(String[] msgFromServer) {
        client.displayMessage(msgFromServer);
    }

    public void displayConnectedClients(String[] msgFromServer) {
        String targets = msgFromServer[1];
        String[] splitTargets = targets.split(",");

        client.updateClientList(splitTargets);
    }

}
