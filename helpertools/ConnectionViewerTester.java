package helpertools;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;


public class ConnectionViewerTester implements Runnable {

	private Socket socket;
	private Thread t; 
	
	public static void main(String[] args) {
		try {
		for (int i = 0; i < 10; i++) {
			new ConnectionViewerTester("user" + i, "compName" + i, new Date(), "Client");
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ConnectionViewerTester (String user, String compName, Date date, String userLevel) throws Exception {
		socket = new Socket("localhost", 1234);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(new ActiveConnectionObj(user, compName, date, userLevel));
		t = new Thread(this);
		t.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Connected to server :)");
		try {
		synchronized (this) {
			t.wait();
		}
		} catch (Exception e) {} // don't do anything
		
	}
	
}
