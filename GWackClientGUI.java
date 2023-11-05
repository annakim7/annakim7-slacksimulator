// the client code and GUI for interacting with a server


/*
GWackClientGUI must have an instance of aGWackClientNetworking class 
as a field (to manage the networking components)

GWackClientGUI must have the methods newMessage(String message), updateClients(String clients), 
and sendMessage() to implement the required functionality for those logical components

GWackClientGUI must define, as private classes, 
a ConnectActionListener and DisconnectActionListener to help implement 
the required functionality for connecting and disconnecting.
 */

import javax.swing.SwingUtilities;

public class GWackClientGUI {
    public static void main(String[] args) {
        GWackClientFrame frame = new GWackClientFrame();
        frame.setVisible(true);
    }
}

