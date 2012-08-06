/*
 * File: ITUserInputServer.java
 * Author: Will Flores
 * 
 * This file implements a Robust IT Server.
 * 
 * Created on July 11, 2012
 */

import java.io.*;
//import java.util.Collection;
//import java.util.TreeMap;
//import java.util.concurrent.ConcurrentHashMap;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

public class ITUserInputServer  implements Runnable, ActionListener {
	/* "clients" key is the usernames of WHO IS NOW using the program.
	 * So always start with this collection EMPTY!
	 * (So this collection need not be saved to disk.)
	 */ 
	private ConcurrentHashMap<ActiveConnectionObj, ObjectOutputStream> clients =
			new ConcurrentHashMap<ActiveConnectionObj, ObjectOutputStream>();
	
	/* "authorizedClients" key is associated with the clientname.
	 * This collection is always initialized from a prepared
	 * .ser file, and is NOT updated by the server.
	 * (So this collection need not be saved to disk.)
	 */
//	private TreeMap<String, String> authorizedClients;
	
	/* "passwords" key is associated with the password. 
	 * This collection is always restored from disk.
	 * This collection is updated by 1st-time joiners,
	 * and it must be saved to disk whenever it's changed.
	 */
//	private ConcurrentHashMap <String, String> passwords;
	
	private int portNumber = 1234;
	private ServerSocket ss;
	
	/* Server GUI Stuff */
	private JFrame serverWindow = new JFrame("IT New User Input Server");
	private JPanel mainPanel = new JPanel();
	private JPanel txtPanel = new JPanel();
	private JPanel btnPanel = new JPanel();

	private JTextArea outTextArea = new JTextArea(15,30);
	private JButton closeButton = new JButton("Close");
	private String newLine = System.getProperty("line.separator");
	private JScrollPane outScrollPane = new JScrollPane(outTextArea);
	private JScrollBar vsb = outScrollPane.getVerticalScrollBar();
	/* Menu Bar */
//	private JMenuBar mainBar = new JMenuBar();
	private JMenu serverOptions; // tools
	private JMenu monitorOption; // view
	
	
//	private String pdfProgLocation = "\"C:\\Users\\Will\\Documents\\Visual Studio 2010\\Projects\\ITFormCreatorConsole\\ITFormCreatorConsole\\bin\\Debug" +
//			"\\ITFOrmCreatorConsole.exe\"";
//	private String pdfProgLocation ="\"C:\\Users\\srvflores\\Desktop\\ITFormCreatorConsole.exe\"";
	private String pdfProgLocation = "\"D:\\ITFormCreatorConsole.exe\""; // Will's computer
	private String serverDocumentDir = "D:\\"; //Will's computer
//	private String serverDocumentDir = "I:\\";
//	private String pdfProgLocation = "\"I:\\ITFormCreatorConsole.exe\"";
	
	/* Server Admin Popup - called when the server admininstration option selected */
	private JFrame serverAdmin = new JFrame("Server Administration Options");
	private JPanel serverOpPanel = new JPanel();
	private JTextField serverDocumentDirTxt = new JTextField(16);
	private JTextField pdfProgLocationTxt = new JTextField(16);
	private JButton serverDocDirBtn = new JButton("Open");
	private JButton pdfProgLocBtn = new JButton("Open");
	
	/**
	 * @param args doesn't have any cmdline args.
	 * Main: starts up the ITUserInputServer
	 */
	public static void main(String[] args) {
		System.out.println("Starting New User Input Server...");
		
		if (args.length != 0) {
			System.out.println("Don't invoke ITUserInputServer with command line params.");
		}
		/* run the server */
		try {
			new ITUserInputServer();
		}
		catch (Exception e) {
			System.out.println(e.toString()); // server app died
		}
	} /* End main */

	/* 
	 * ITUserInputServer - takes in requests to disk
	 */
	public ITUserInputServer() throws Exception {
		ss = new ServerSocket(portNumber);
		
		serverWindow.setSize(450, 450);
		serverWindow.setLocation(250,250);
		
//		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		serverWindow.getContentPane().add(mainPanel, "Center");
		serverWindow.getContentPane().add(btnPanel, "South");
		mainPanel.add(txtPanel);
//		mainPanel.add(btnPanel);

		/* Create menu bar */
		createMonitorMenu();
		createServerMenu();
		
		JMenuBar menuBar = new JMenuBar();
		serverWindow.setJMenuBar(menuBar);
		
		/* Add server Log window */
		txtPanel.add(outScrollPane);
		outTextArea.setEditable(false); // Just make this a display text area

		btnPanel.add(closeButton);
		closeButton.addActionListener(this); // make sure button closes gui
		
		menuBar.add(serverOptions);
		menuBar.add(monitorOption);
		
		outTextArea.setText("New User Input server is up at " +
		    InetAddress.getLocalHost().getHostAddress() + " on port " +
				ss.getLocalPort() + newLine);
		
		/* Set up the admin stuff on this app */
		serverAdmin.getContentPane().add(serverOpPanel);
		serverOpPanel.add(new JLabel("Document Location"));
		serverOpPanel.add(serverDocumentDirTxt);
		serverOpPanel.add(serverDocDirBtn);
		serverOpPanel.add(new JLabel("PDF Generator Location"));
		serverOpPanel.add(pdfProgLocationTxt);
		serverOpPanel.add(pdfProgLocBtn);
		/* End admin gui */
		
		serverWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverWindow.setVisible(true);
		new Thread(this).start(); // create a thread and start running!
	}
	
	@Override
	public void run() {
		// Each Client goes here!		
		Socket s = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
//		String clientName = "";
//		String userid = null;
//		String password = null;	
		try {
			s = ss.accept(); // wait for a client
			new Thread(this).start(); // make a thread for the NEXT client
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			
//			// validate connection
//			if (firstMsg.equals("test")) {
//				// good connection
//			}
//			else {
//				oos.writeUTF("Invalid Protocol. ");
//				oos.close();
//				printToConsole("Invalid 1st message" + firstMsg);
//				return; // kill thread client
//			}
		}
		catch (Exception e) {
			printToConsole("Client connection failure: " + e);
			if (s.isConnected()) {
				try {
					s.close(); // hang up
				}
				catch (IOException ioe) {
					// Do nothing, because the client already hung up
				}
			}
		}
		
		// Barebones receive loop
		try {
			boolean formIsGenerated = false;
			while (true) {
				Object msg = ois.readObject();
				if (msg instanceof NewUser) {
					NewUser user = (NewUser) msg;
					printToConsole(user.toString()); // trace
					String command = getPdfProgLocation() +" n_" + getServerDocumentDir() + user.getFileName() +"_";
					Process createForm = Runtime.getRuntime().exec(command + user.toString());
					
					try {
						Integer exitStatus = new Integer(createForm.waitFor());
						printToConsole(exitStatus.toString());
						FormStatus fs = new FormStatus(exitStatus, user.getFileName());
						formIsGenerated = true;
						oos.writeObject(fs); // write the object to client, now for pdf email
						printToConsole("Waiting for user authentication...");
					} 
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch (IOException ioe) { 
						printToConsole("Someone is leaving.");
						printToConsole(ioe.toString());
						// at some point there needs to be something here to keep track
						// of logged on ppl
					} 	
				} // End if
				else if (formIsGenerated && msg instanceof RequestAuth) {
					RequestAuth ra = (RequestAuth) msg;
					// update filename with server location
					ra.updateFilename(serverDocumentDir + ra.getFilename());
					printToConsole("Sending message to admin.");
					ra.Authenticate();
					ra.sendMessageToUser();
				}
			} // End While
		}
		catch (ClassNotFoundException cnfe) {
			printToConsole(cnfe.toString());
		} 
		catch (IOException ioe) {
			// TODO Auto-generated catch block
			printToConsole(ioe.toString());
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// End run()
	
	private void printToConsole (String msg) {
		outTextArea.append(msg + newLine);
		vsb.setValue(vsb.getMaximum());
		outTextArea.setCaretPosition(outTextArea.getDocument().getLength());
		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getSource() == closeButton) {
			printToConsole("Closing server app...");
			System.exit(0); // terminate app
		}
		/* Got to handle when an item is selected */
	}
	
	private void createMonitorMenu() { // view different programs
		JMenuItem item;
		monitorOption = new JMenu("View");
		
		item = new JMenuItem("Administer Form Database");
		item.addActionListener(this);
		monitorOption.add(item);
	}
	private void createServerMenu() { // tools
		JMenuItem item;
		serverOptions = new JMenu("Tools");
		
		item = new JMenuItem("View Connected Users...");
		item.addActionListener(this);
		serverOptions.add(item);
		
	}

	public String getPdfProgLocation() {
		return pdfProgLocation;
	}

	public void setPdfProgLocation(String pdfProgLocation) {
		this.pdfProgLocation = pdfProgLocation;
	}

	public String getServerDocumentDir() {
		return serverDocumentDir;
	}

	public void setServerDocumentDir(String serverDocumentDir) {
		this.serverDocumentDir = serverDocumentDir;
	}
}
