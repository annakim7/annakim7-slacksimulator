import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class GWackClientNetworking {
    // constructor
    // have a way to make a thread
    private Socket socket;
    private String IPaddress;
    private int port;
    private PrintWriter pw;
    private BufferedReader br;
    private GWackClientGUI GUI;
    private volatile boolean connect;
    private String name;
    private boolean isPeople = false;

    // what parameters to take in?
    public GWackClientNetworking(String IPaddress, int port, String name, GWackClientGUI GUI){

        this.port = port;
        this.IPaddress = IPaddress;
        connect = true;
        this.GUI = GUI;
        this.name = name;

        try{
            socket = new Socket(IPaddress, port);
            pw = new PrintWriter(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.GUI = GUI;
            pw.println("SECRET");
            pw.println("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87");
            pw.println("NAME");
            pw.println(name);
            pw.flush();
            connect = true;
        }
        catch(Exception e){
            System.err.println("Cannot Connect");
            JOptionPane.showMessageDialog(GUI.frame, "Cannot connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
        }
        ReadingThread readingThread = new ReadingThread();
        readingThread.start();
    }

    public void writeMessage(String message){
        if(isConnected()){
            pw.println(message);
            pw.flush();
        }
    }

    public boolean isConnected(){
        if(socket.isConnected()){
            return true;
        }
        return false;
    }

    public void disconnect(){
        try{
            socket.close();
            pw.close();
            br.close();
        }
        catch(Exception e){
            e.printStackTrace(); 
        }
    }


    private class ReadingThread extends Thread{
        public void run(){
            String yas = "";
            try{
                while(true){
                    String msg = br.readLine();
                    System.out.println(msg);
                    System.out.println(isPeople);
                    if(msg.equals("START_CLIENT_LIST")){
                        isPeople = true;
                    }

                    else if(msg.equals("END_CLIENT_LIST")){
                        System.out.println("End client list!");
                        isPeople = false;
                        GUI.updateClients(yas);
                        yas = "";
                    }else if(isPeople == true){
                        yas += msg + "\n";
                    }
                    else{
                        GUI.newMessage(msg);
                        System.out.println("Sending message!");
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
                connect = false;
            }
        }
    }

}


