package chatServer;

import java.io.DataInputStream; //For io
import java.io.DataOutputStream; //For io
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform; //For GUI functionality 

//Extends thread
public class ClientSocket extends Thread {

    Socket socket;
    CandorServer server;
    // Create data input and output streams
    DataInputStream input;
    DataOutputStream output;

    public ClientSocket(Socket sock, CandorServer serve) 
    {
        this.server = serve;
        this.socket = sock;
      
    }

    @Override
    public void run() {

        try {
            // Create data input and output streams
            input = new DataInputStream(
                    socket.getInputStream());
            output = new DataOutputStream(
                    socket.getOutputStream());

            while (true) {
                // Get message from the client
                String message = input.readUTF();

                //sends a message through the displayMessage method in CandorServer file
                server.displayMessage(message);
            }
            
            

        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try 
            {
                socket.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    //send message back to client
    public void sendMessage(String message) 
    {
        try 
        {
            output.writeUTF(message);
            output.flush();
        } catch (IOException err) 
        {
            err.printStackTrace();
        }
    }
}
