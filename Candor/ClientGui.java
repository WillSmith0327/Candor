package chatClient;

import java.io.DataOutputStream; //For IO
import java.io.IOException; //For IO
import java.net.Socket; //For socket functionality

//All the necessary import statements for the GUI itself
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;

import javafx.event.ActionEvent; //For event handling
import javafx.event.EventHandler;
import javafx.geometry.Pos; 
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ClientGui  extends Application
{
	DataOutputStream output = null;
	TextArea chatRoom; //TO hold entered messages
	TextField chatInput; //TO hold inputted text from user to be sent with send button
	TextField usernameText;
	static int portNum;

	public static void main(String[] args)  //sets portNum and launches GUI
	{
		portNum = Integer.parseInt(args[0]);
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception 
	{
		BorderPane borderPane = new BorderPane(); //base pane used
		
		chatRoom = new TextArea(); //textArea to be used as the chat room, cannot be edited
		chatRoom.setEditable(false);
		
		HBox hPane = new HBox(); //hbox for the bottom textFields and button
		
		chatInput = new TextField();
		chatInput.setEditable(true); //create an input box and set it to be editable
		chatInput.setPrefWidth(300);
		
		usernameText = new TextField("Type username here"); //create a username box
		
		Button sendMsg = new Button("Send"); //button to send message
		
		sendMsg.setOnAction((event) ->
		{
			 try {
	                //gets username and message
	                String username = usernameText.getText().trim();
	                String message = chatInput.getText().trim();

	                //if username is empty, set it to User 
	                if (username.length() == 0) {
	                    username = "User";
	                }
	                //if message is empty, do nothing
	                if (message.length() == 0) {
	                    return;
	                }

	                //send message to the server
	                output.writeUTF("  " + username + " : " + message + ""); //puts username in front of message
	                output.flush();

	                //clear the chatInput field
	                chatInput.clear();
	            } catch (IOException err) {
	                System.err.println(err);
	            }
		});
		
		
		hPane.getChildren().addAll(usernameText, chatInput, sendMsg); //add stuff to the hBox
		borderPane.setCenter(chatRoom); //centers the chatroom and sets the hBox to the bottom 
		borderPane.setBottom(hPane);
		
		int windowWidth = 512; //variables to set the size of the chat window
		int windowHeight = 512;

		Scene scene = new Scene(borderPane, windowWidth, windowHeight);
		stage.setTitle("Candor");
		stage.setScene(scene);
		stage.show(); //create scene, set it, and show stage
		
		try {
	            //socket to connect to server, always going to be localhost
	            Socket socket = new Socket("localhost", portNum);
	            chatRoom.appendText("Connection Successful. \n");
	          
	            //output stream to send messages to server
	            output = new DataOutputStream(socket.getOutputStream());

	            //create a read thread
	            IOThread task = new IOThread(socket, this);
	            Thread thread = new Thread(task);
	            thread.start();
	       } catch (IOException e) { //catch if something goes wrong; NOTE: Window is already created at this point, window will still appear if error occurs but the error message will be in the chat room not "Connection Successful"
	            
	            chatRoom.appendText(e.toString() + '\n');
	        }
	    }
}