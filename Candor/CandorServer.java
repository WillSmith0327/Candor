package chatServer;
//class file will be stored in the package called chatServer

import java.net.ServerSocket; //These are necessary for the socket connection functions from Java library
import java.net.Socket;

import java.util.*; //Import the necessary java utilities for basic functionality
import java.io.*; //This is for the input/output functions

public class CandorServer {
    
    private int numUsersConnected = 0; //To hold the number of clients currently connected to the server
    private int timeout = 600; //A server timeout variable that serves to timeout the server after ten minutes or 600 seconds
    private Set<ClientSocket> clientSockets = new HashSet<>(); //A set which contains all of the clients which are currently connected
    private int portNumber; //to hold the port number to connect to
 
    public CandorServer(int portNumber) //constructor of the candor chat server
    {
    	System.out.print("\n Server object created... now activating server. \n"); //print this msg to cmd line
        this.portNumber = portNumber; //Set global var in this file to the passed in arg
    }

     public static void main(String[] args) 
     {
        if(Integer.parseInt(args[0]) < 1000)
        { //error statement
            System.out.println("Port number is reserved.\n"); //if the entered port number is under 1000 then it likely is already used
            System.exit(-1); //exits program
        }

        if (args.length == 0 || args.length > 3) //If there are an incorrect amount of arguments output error and exit
        { //error statement
            System.out.println("Incorrect syntax, you need a port number in the command line argument.\n");
            System.exit(-1); //exits program
        }
 
 	//sets port num to the argument entered at the command line
        int portNumber = Integer.parseInt(args[0]);
 
        CandorServer server = new CandorServer(portNumber);
        server.activate();
    }
 
 	//method which activates the server
    public void activate() {
    	//Try statement for catching errors
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) 
        {
 			//msg to show that the server is active and succesfully connected
            System.out.println("\nThe server is now active on port " + portNumber + " and is accepting connections from clients.\n");
 
            for(;;) //infinite loop
            {
                Socket socket = serverSocket.accept(); //accepts a new client connection
                
                ClientSocket newUser = new ClientSocket(socket, this); //creates a new clientsocket object
                System.out.println("\nA new client has connected to the server.\n There are currently " + (numUsersConnected + 1) + " users in the chatroom.\n"); //output msg to cmd line
                clientSockets.add(newUser); //add to list
                numUsersConnected += 1; //Add to the number of users connected
                newUser.start();
 
            }
 
        } catch (IOException err) { //If an error occurs then we print the stacktrace
           
            err.printStackTrace();
        }
    }
 	//This crucial method is responsible for displaying all sent messages to every currently connected user to the server.
    void displayMessage(String msg)
    {
        for (ClientSocket aUser : clientSockets) //Iterates through all users in the set
        {
                aUser.sendMessage(msg); //Sends the message to the current client in the loop
        }
    }
}