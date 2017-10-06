package client;

import gui.GUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.ServerMessageHandler;

/**
 *
 * @author mathiasjepsen
 */
public class Client implements Runnable {

    private final ServerMessageHandler smh = new ServerMessageHandler(this);
    private final GUI GUI;
    private Socket clientSocket;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private boolean isOpen = true;

    public Client(GUI GUI) {
        this.GUI = GUI;
    }

    @Override
    public void run() {
        while (isOpen) {
            try {
                String msgFromServer = fromServer.readLine();
                smh.handleServerMessage(msgFromServer);
            } catch (NullPointerException ex) {
                isOpen = false;
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        close();
    }

    public void close() {
        try {
            clientSocket.close();
            fromServer.close();
            toServer.close();
            GUI.resetFields();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect(String address, int port) throws IOException {
        clientSocket = new Socket(address, port);
        fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        toServer = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public void send(String msgToServer) {
        
        toServer.println(msgToServer);
    }

    public GUI getGUI() {
        return GUI;
    }

    public boolean isIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void updateClientList(String[] recipients) {
        GUI.updateClientList(recipients);
    }

    public void displayMessage(String[] msgFromServer) {
        GUI.displayMessage(msgFromServer);
    }
}
