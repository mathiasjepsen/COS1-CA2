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
    
    public void handleServerMessage(String msg) {
      String[] splitMessage = msg.split(":");
        String command = splitMessage[0];
        switch (command) {
            case "MSGRES":
                displayMessage(splitMessage);
                break;
            case "CLIENTLIST" :
                displayConnectedClients(splitMessage);
            default:
                //wrong message
                break;
        }   
    }
    
    public void displayMessage(String[] msgFromServer) {
        String sender = msgFromServer[1];
        String message = msgFromServer[2];
        
        client.getGUI().getTextArea().append(sender + ": " + message + "\n");
    }
    
    public void displayConnectedClients(String[] msgFromServer) {
        String targets = msgFromServer[1];
        String[] splitTargets = targets.split(",");
//        String formattedTargets = "";
//        
//        int i = 0;
//        for (String target : splitTargets) {
//            formattedTargets += target;
//            if (i < splitTargets.length-1 && splitTargets.length > 1) {
//                    formattedTargets += ", ";
//            }
//            i++;
//        }
//        
        client.getGUI().updateClientList(splitTargets);
    }
    
}
