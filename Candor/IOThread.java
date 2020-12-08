package chatClient;

import java.net.*;
import java.io.*;
import javafx.application.Platform;
 
public class IOThread implements Runnable {
    private BufferedReader reader;
    private Socket socket;
    private ClientGui client;
 
    public IOThread(Socket socket, ClientGui client) {
        this.client = client;
        this.socket = socket;
        try { //try to get input stream from the socket
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException err) { //catch if something goes wrong here, prints to console rather than chat room
          
            err.printStackTrace();
        }
    }
 
    public void run() {
        //always loop through it
        while (true) 
        {
            try 
            {
                //create data input stream from socket
            	DataInputStream  input = new DataInputStream(socket.getInputStream());

                //reads input from user
                String message = input.readUTF();

                //append user message to the chat room
                Platform.runLater(() -> {
                    client.chatRoom.appendText(message + "\n");
                });
            } catch (IOException err) { //catch if anything goes wrong    
                err.printStackTrace();
                break;
            }
        }
    }
}