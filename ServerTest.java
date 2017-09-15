package IM;


import javax.swing.JFrame;

public class ServerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server sv = new Server();
		sv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sv.startRunning();

	}

}
