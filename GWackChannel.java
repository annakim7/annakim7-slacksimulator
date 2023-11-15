import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GWackChannel{
    // constructors
    private int port;
    private List<GWackConnectedClient> clientsConnect;
    private Queue <String> queue;
    private ServerSocket serverSock;

    // instance
    public GWackChannel(String port){
        try{
            this.port = Integer.parseInt(port);
            serverSock = new ServerSocket(this.port);
            clientsConnect = new ArrayList<>();
            queue = new LinkedList<>();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void serve(){
        while(true){
            try{
                //accept incoming connection
                Socket clientSock = serverSock.accept();
                System.out.println("New connection: "+ clientSock.getRemoteSocketAddress());
                //start the thread
                GWackConnectedClient connectedClient = new GWackConnectedClient(clientSock, this);
                connectedClient.start();
                clientsConnect.add(connectedClient);
                enqueueMessage();
                //continue looping
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public synchronized void enqueueMessage() {
    try{
        for (GWackConnectedClient client : clientsConnect) {
            System.out.println("Sending message to" + client.getName());
           // PrintWriter pw = new PrintWriter(client.getSocket().getOutputStream());
            client.pw.println("START_CLIENT_LIST");
            // pw.flush();
            for (GWackConnectedClient connectedClient : clientsConnect) {
                client.pw.println(connectedClient.getClientName());
            }
            client.pw.println("END_CLIENT_LIST");
            client.pw.flush();
            //pw.close();
        }
    } 
    catch (Exception e) {
        e.printStackTrace();
    }
}


public synchronized void dequeueAll() {
    for (String message : queue) {
        sendMessage(message);
    }
}

public List<String> getClientList() {
    List<String> names = new ArrayList<>();
    for (GWackConnectedClient client : clientsConnect) {
        names.add(client.getClientName());
    }
    return names;
}

public synchronized void removeClients() {
    for (GWackConnectedClient client : clientsConnect) {
        if (!client.isValid()) {
            clientsConnect.remove(client);
        }
    }
}

public void sendMessage(String message){
    try {
        for (GWackConnectedClient client : clientsConnect) {
            PrintWriter pw = new PrintWriter(client.getSocket().getOutputStream());
            pw.println(message);
            pw.flush();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);
    GWackChannel channel = new GWackChannel(Integer.toString(port));
    channel.serve();
}

}