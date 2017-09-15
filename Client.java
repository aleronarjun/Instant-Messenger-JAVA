package IM;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;


public class Client extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message ="";
	private String serverIP;
	private Socket connection;
	
	public Client(String host){
		
		super("IM - CLIENT");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed (ActionEvent e){
						sendMessage(e.getActionCommand());
						userText.setText("");
					}
				}
				
				);
		add(userText, BorderLayout.SOUTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,300);
		setVisible(true);
	}
	
	public void startRunning(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
			
		}catch(EOFException e ){
			sendMessage("/nConnect terminated.");
		}catch(IOException i){
			i.printStackTrace();
		}finally{
			closeCrap();
		}
	}
	
	public void connectToServer() throws IOException{
		
		showMessage("\nAttempting connection....");
		connection = new Socket(InetAddress.getByName(serverIP), 2403);
		showMessage("\nNow connected to: "+ connection.getInetAddress().getHostName());
	}
	
	public void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nStreams are now setup.");
	}
	
	public void whileChatting() throws IOException{
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n"+ message);
			}catch(ClassNotFoundException c){
				showMessage("\nI dont know that object.");
			}
			
		}while(!message.equals("SERVER - END"));
		
	}
	
	public void closeCrap(){
		showMessage("Closing streams...");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
			
		}catch(IOException i){
			i.printStackTrace();
		}
	}
	
	public void sendMessage(String message){
		try{
			
			output.writeObject("CLIENT - "+message);
			output.flush();
			showMessage("\nCLIENT - "+message);
			
		}catch(IOException ex){
			chatWindow.append("\n something messed up sending message");
		}
		
	}

	public void showMessage(final String txt){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(txt);
					}
				}
				);
		
	}
	
	public void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}
				);
	}
}
