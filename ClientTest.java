package IM;
import javax.swing.JFrame;
public class ClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c;
		c = new Client("127.0.0.1");
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.startRunning();

	}

}
