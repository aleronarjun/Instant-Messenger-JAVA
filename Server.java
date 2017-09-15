package IM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

public class Server extends JFrame {
	
	private JTextField msg;
	private JTextArea chatArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection; //for client side. (Active)
	private ServerSocket server; //for server side (Passive)
	
public Server(){
	
	super("IM - SERVER");
	msg = new JTextField("");
	msg.setEditable(false);
	msg.addActionListener(
			new ActionListener(){
			public void actionPerformed(ActionEvent e){
					sendMessage(e.getActionCommand());
					msg.setText("");
				}
			}
			);
	add(msg, BorderLayout.SOUTH);
	chatArea = new JTextArea();
	chatArea.setEditable(false);
	add(new JScrollPane(chatArea));
	setSize(300,300);
	setVisible(true);	
}

public void startRunning(){
	
	try{
		while(true){
			
			try{
			waitConnection();
			setStreams();
			whileChatting();}
			
			catch(EOFException eo){
				showMessage("\n Server ended the connection :(");
			}finally{
				closeCrap();
			}
		}
		
	}
	catch(IOException e){
		e.printStackTrace();
	}
}


private void waitConnection() throws IOException{
	
	showMessage("\nWaiting for connection... \n");
	server = new ServerSocket(2403,5);
	connection = server.accept();
	showMessage("\nNow connected with "+connection.getInetAddress().getHostName()+" ! :)");
	
}

private void setStreams() throws IOException {
	// TODO Auto-generated method stub
	output = new ObjectOutputStream(connection.getOutputStream());
	output.flush();
	input = new ObjectInputStream(connection.getInputStream());
	showMessage("\nStreams are now set up!");
}

private void whileChatting()throws IOException{
	String message = "\nYou are now connected! Start chatting.";
	sendMessage(message);
	ableToType(true);
	do{
		try
		{message = (String) input.readObject();
		showMessage("\n" + message);}
		
		catch(ClassNotFoundException cnf){
		showMessage("\nSomething went wrong");
		}
		
	}while(!message.equals("CLIENT - END"));
}

private void closeCrap(){
	
	showMessage("\nClosing connections...");
	ableToType(false);
	try{
		output.close();
		input.close();
		connection.close();
	}catch(IOException ioe){
		ioe.printStackTrace();
	}
}

private void sendMessage(String message){
	 try{
		 output.writeObject("\nSERVER - "+message);
		 output.flush();
		 showMessage("\nSERVER -  "+message);
		 
	 }
	catch(IOException i){
		chatArea.append("Cant send...");
	}
}

private void showMessage(final String text){
	SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatArea.append(text);
				}
			}
			);
	
}

private void ableToType(final boolean tof){
	
	SwingUtilities.invokeLater(
			new Runnable(){
				
				public void run(){
					msg.setEditable(tof);
					}
			}
			);	
}
}