import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GWackClientFrame extends JFrame{

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

public GWackClientFrame(){
    super();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

// panel for top section
    JPanel top = new JPanel();
    top.setLayout(new FlowLayout());
    name = new JTextField("", 5);
    ipAddress = new JTextField("", 7);
    port = new JTextField("", 5);
    disconnect = new JToggleButton("Disconnect");
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
    sendPanel.add(send, BorderLayout.EAST);
    add(sendPanel, BorderLayout.SOUTH);

    add(right, BorderLayout.EAST);

// panel for left section
    JPanel left = new JPanel();
    left.setLayout(new FlowLayout());
    membersLabel = new JLabel("Members Online");
    membersOnline = new JTextArea(20,10);
    membersOnline.setEditable(false);
    // add to panel
    left.add(membersLabel, BorderLayout.NORTH);
    left.add(membersOnline, BorderLayout.CENTER);

    add(left, BorderLayout.WEST);

    disconnect.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            if(disconnect.isSelected()){
                disconnect.setText("Connect");
            }
            else{
                disconnect.setText("Disconnect");
                // The connect button allows the GUI to connect to one of the sample servers (doesn’t have work)	
            }
        }
    });
    pack();
    setVisible(true);
    }

}