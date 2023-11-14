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

// import java.util.concurrent.BlockingQueue;
// import java.util.concurrent.LinkedBlockingQueue;

// the server code to host a channel/chatroom

/*
a class named GWackChannel that represents the server and contains the main method
a private class GWackConnectedClient inside of GWackChannel that is used to manage individual connections to the server. This class must extend the Thread class, and have the following methods: sendMessage(String message), isValid(), getClientName(), and run()
serve(), addClient(GWackConnectedClient client), enqueuMessage(String message), dequeueAll(), and getClientList() methods to implement the required functionality
*/

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
                
                //continue looping
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public synchronized void enqueueMessage(String message) {
   // queue.add(message);
    // make something like
    // START_CLIENT_LIST
    // false
    // Anna
    // true
    // END_CLIENT_LIST
    
    try {
        for (GWackConnectedClient client : clientsConnect) {
            PrintWriter pw = new PrintWriter(client.getSocket().getOutputStream());
            pw.println("START_CLIENT_LIST");
            pw.flush();
            for (GWackConnectedClient connectedClient : clientsConnect) {
                pw.println(connectedClient.getClientName());
                pw.flush();
            }
            pw.println("END_CLIENT_LIST");
            pw.flush();
        }
    } 
    catch (IOException e) {
        e.printStackTrace();
    }
}

    /*
     * try
     * printwriter pw2
     * for each socket sock: client
     * pw = new print writer(sock.outputstream)
     * pw2.println("STARRTCK")
     * flush
     * for string name clientname
     * pw 2 print ln(name)
     * flush
     * 
     * pw print kn client
     * flush
     * 
     * catch exception e
     */


public synchronized void dequeueAll() {
    for (GWackConnectedClient client : clientsConnect) {
        for (String message : queue) {
            client.sendMessage(message);
        }
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

public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);
    GWackChannel channel = new GWackChannel(Integer.toString(port));
    channel.serve();
}

}