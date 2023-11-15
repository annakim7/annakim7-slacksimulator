import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/*a private class GWackConnectedClient inside of GWackChannel 
that is used to manage individual connections to the server. 
This class must extend the Thread class, and have the following 
methods: sendMessage(String message), isValid(), getClientName(), 
and run() */

class GWackConnectedClient extends Thread{
    private Socket socket;
    public PrintWriter pw;
    private BufferedReader br;
    private GWackChannel channel;
    private volatile boolean connect;
    private String name;

    public GWackConnectedClient(Socket socket, GWackChannel gWackChannel){
        this.socket = socket;
        this.channel = gWackChannel;

        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid(){
        return connect;
    }

    public String getClientName(){
        return name;
    }

    public Socket getSocket() {
        return socket;
    }
    

    @Override
    public void run(){
        // check if message is not equal 
        try{
            // handshake
            String handshake = br.readLine();
            if(!handshake.equals("SECRET")){
                pw.close();   
            }
            handshake = br.readLine();
            if(!handshake.equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87")){
                pw.close();                
            }
            handshake = br.readLine();
            if(!handshake.equals("NAME")){
                pw.close();
            }
            name = br.readLine();

            // this is the name, need it to go into members online
            //String yas = "" + channel.getClientList();
            //channel.sendMessage(yas);
            
            String msg = br.readLine();
            while(msg != null){
                channel.sendMessage("[" + name + "]" + " " + msg);
                //channel.dequeueAll();
                msg = br.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try{
            pw.close();
            br.close();
            socket.close();
            channel.removeClients();
            channel.sendMessage("" + channel.getClientList());
            channel.dequeueAll();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}