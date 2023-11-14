// the client code and GUI for interacting with a server

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GWackClientGUI extends JFrame{
// GUI CONSTRUCTORS

static GWackClientGUI frame;

// labels
private JLabel nameLabel;
private JLabel ipLabel;
private JLabel portLabel;
private JLabel membersLabel;
private JLabel messageLabel;
private JLabel composeLabel;

// text fields ()
private JTextField name;
private JTextField ipAddress;
private JTextField port;

// text areas ()
private JTextArea messages;
private JTextArea compose;
private JTextArea membersOnline;

// buttons
private JToggleButton disconnect;
private JButton send;

// instance of GWackClientNetworking class
private GWackClientNetworking clientNetworking;

// GUI METHOD
public GWackClientGUI(){
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLayout(new BorderLayout());

// panel for top section
JPanel top = new JPanel();
top.setLayout(new FlowLayout());
name = new JTextField("", 5);
ipAddress = new JTextField("", 7);
port = new JTextField("", 5);
disconnect = new JToggleButton("Connect");
nameLabel = new JLabel("Name");
ipLabel = new JLabel("IP Address");
portLabel = new JLabel("Port");

// add to panel
top.add(nameLabel);
top.add(name);
top.add(ipLabel);
top.add(ipAddress);
top.add(portLabel);
top.add(port);
top.add(disconnect);

add(top, BorderLayout.NORTH);

// panel for right section
JPanel right = new JPanel();
right.setLayout(new BorderLayout());
messageLabel = new JLabel("Messages");
messages = new JTextArea(20, 40);
messages.setEditable(false);
composeLabel = new JLabel("Compose");
compose = new JTextArea(5, 40);

// ADDING KEY LISTENER SO MESSAGES SEND WHEN USER HITS ENTER
compose.addKeyListener(new KeyAdapter(){
    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == '\n'){
            compose.setText("");
        }    
    }
    @Override
    public void keyPressed(KeyEvent e) {        
        if(e.getKeyChar() == '\n'){
            sendMessage();
        }        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar() == '\n'){
            compose.setText("");
        }
    }
});



// add to panel

JPanel mess = new JPanel();
mess.setLayout(new BorderLayout());
mess.add(messageLabel, BorderLayout.NORTH);
mess.add(new JScrollPane(messages), BorderLayout.CENTER);

right.add(mess, BorderLayout.NORTH);

JPanel comp = new JPanel();
comp.setLayout(new BorderLayout());
comp.add(composeLabel, BorderLayout.NORTH);
comp.add(compose, BorderLayout.CENTER);

right.add(comp, BorderLayout.SOUTH);

JPanel sendPanel = new JPanel();
send = new JButton("Send");

send.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        sendMessage();
    }
});

sendPanel.add(send, BorderLayout.EAST);
add(sendPanel, BorderLayout.SOUTH);

add(right, BorderLayout.EAST);

// panel for left section
JPanel left = new JPanel();
left.setLayout(new BorderLayout());
membersLabel = new JLabel("Members Online");
membersOnline = new JTextArea(20,10);
membersOnline.setEditable(false);
// add to panel
left.add(membersLabel, BorderLayout.NORTH);
left.add(membersOnline, BorderLayout.CENTER);

add(left, BorderLayout.WEST);

disconnect.addActionListener(new ConnectActionListener());


pack();
setVisible(true);
}

// METHODS WITHIN GUI CLASS
// EVEYTHING ABOUT DISPLAYING
public void newMessage(String newMessage){
    messages.setText(messages.getText() + newMessage + "\n");
}

public void updateClients(String clients){
    membersOnline.setText(clients);
}

// button is hit
public void sendMessage(){
    String send = compose.getText();
    clientNetworking.writeMessage(send);
    compose.setText("");
}

// PRIVATE CLASS TO CONNECT TO SERVER
    // call method from client networking
    // GWackClientGUI.this.setText("Connect");
    // create new disconnect action listening
    // GWackClientGUI.___.removeactionlistener(this);

    private class ConnectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                disconnect.setText("Disconnect");
                int portNumber = Integer.parseInt(port.getText());
                clientNetworking = new GWackClientNetworking(ipAddress.getText(), portNumber, name.getText(), frame);
                disconnect.removeActionListener(this);
                disconnect.addActionListener(new DisconnectActionListener());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Cannot connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DisconnectActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            disconnect.setText("Connect");
            try {
                clientNetworking.disconnect();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Server disconnected", "Error", JOptionPane.ERROR_MESSAGE);
            }
            disconnect.removeActionListener(this);
            disconnect.addActionListener(new ConnectActionListener());
        }
    }

// MAIN METHOD TO RUN GUI
public static void main(String[] args) {
    frame = new GWackClientGUI();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

// END OF GUI CLASS
}
