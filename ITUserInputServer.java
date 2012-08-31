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
import java.net.*;
import java.util.*;
//import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

@SuppressWarnings("unused")
public class ITUserInputServer  implements Runnable, ActionListener {
	/* "clients" key is the usernames of WHO IS NOW using the program.
	 * So always start with this collection EMPTY!
	 * (So this collection need not be saved to disk.)
	 */ 
	private ConcurrentHashMap<String, HashMap<ActiveConnectionObj, ObjectOutputStream>> clients =
			new ConcurrentHashMap<String, HashMap<ActiveConnectionObj, ObjectOutputStream>>();
	
	private Vector<NewUser> addedUsers = new Vector<NewUser>();
	
	private int portNumber = 1234;
	private ServerSocket ss;
	
	/* Server GUI Stuff */
	private JFrame serverWindow = new JFrame("IT New User Input Server");
	private JPanel mainPanel = new JPanel();
	private JPanel txtPanel = new JPanel();
	private JPanel btnPanel = new JPanel();

	private JTextArea outTextArea = new JTextArea(21,36);
	private JButton closeButton = new JButton("Close");
	private String newLine = System.getProperty("line.separator");
	private JScrollPane outScrollPane = new JScrollPane(outTextArea);
	private JScrollBar vsb = outScrollPane.getVerticalScrollBar();
	
	/* Menu Bar */
	private JMenu serverOptions; // tools
	private JMenu monitorOption; // view
	private JMenuItem adminDatabaseItem;
	private JMenuItem viewConnectionsItem;
	
	private String pdfProgLocation = "\"D:\\ITFormCreatorConsole.exe\""; // Will's computer
	private String serverDocumentDir = "D:\\"; //Will's computer
	
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
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		serverWindow.getContentPane().add(mainPanel, "Center");
		serverWindow.getContentPane().add(btnPanel, "South");
		mainPanel.add(txtPanel);

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
		ActiveConnectionObj aco = null;
		
		/* Connection establishing block */
		try {
			s = ss.accept(); // wait for a client
			new Thread(this).start(); // make a thread for the NEXT client
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			
			/* The users logging on to the server need to send as their first message an Active connection object. */
			// TODO write to log that a connection was made
			Object msg = ois.readObject();
			aco = (ActiveConnectionObj) msg; // cast the message from client to ACO but don't test this objref
			
			if (msg instanceof ActiveConnectionObj) {
				// Validate connection & save their connection information
				String personLoggingOn = aco.getUserName();
				HashMap<ActiveConnectionObj, ObjectOutputStream> HAO = new HashMap<ActiveConnectionObj, ObjectOutputStream>();
				HAO.put(aco, oos);
				
				clients.put(personLoggingOn, HAO);
				aco.appendToLog("Logged into server ");
				printToConsole(aco.getUserName() + " is joining the server.");
			}
			else {
//				oos.writeUTF("Invalid Protocol. ");
				oos.close();
				printToConsole("Invalid 1st message from " + s.getInetAddress());
				return; // kill thread client
			}

		}
		catch (Exception e) {
			printToConsole("Client connection failure: " + e);
			// TODO Log that there was a server failure
			if (s.isConnected()) {
				try {
					s.close(); // hang up
				}
				catch (IOException ioe) {
					// Do nothing, because the client already hung up
				}
			}
		} /* End Connection establishing block */
		
		
		// Barebones receive loop
		try {
			boolean userInputed = false;
			NewUser user = null;
			while (true) {
				Object msg = ois.readObject();
				if (msg instanceof NewUser) {
					user = (NewUser) msg;
					aco.appendToAddedUsers(user);
					aco.appendToLog("Added a user ");
					printToConsole(user.toString()); // trace
					/* Form received */
					oos.writeObject(new FormStatus(UserStatusID.FORM_RECIEVED));
					userInputed = true;
				}
				if (userInputed && msg instanceof RequestAuth) {
					RequestAuth ra = (RequestAuth) msg;
					// Sent the request to the admin
					printToConsole("Sending message to admin.");
					ra.Authenticate();
					ra.addUser(user);
					ra.sendMessageToUser();
					/* Sent the email */
					oos.writeObject(new FormStatus(UserStatusID.EMAIL_SENT));
					userInputed = false; // reset for next user to inputed
					user = null; // reset for next user to be input
				}
				
			} // End While
		}
		catch (IOException ioe) { // Leave processing
			// TODO Write to log what they did
			aco.appendToLog("Logged off server ");
			// Serialize log here!
			printToConsole("Someone's leaving.");
			clients.remove(aco);
			return; // gotta go!
		}
		catch (Exception e) { // What happened?
			aco.appendToLog("Fatal error: " + e.getMessage());
			clients.remove(aco);
			printToConsole(e.getMessage());
		} /* End receive loop */
	}// End run()
	
	private void printToConsole (String msg) {
		outTextArea.append(msg + newLine);
		vsb.setValue(vsb.getMaximum());
		outTextArea.setCaretPosition(outTextArea.getDocument().getLength());
		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			if (ae.getSource() == closeButton) {
				printToConsole("Closing server app...");
				System.exit(0); // terminate app
			}
			
			/* Got to handle when an item is selected */
			if (ae.getSource() == viewConnectionsItem) {
				new ConnectionViewer(clients);
			}
			if (ae.getSource() == adminDatabaseItem) {
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createMonitorMenu() { // view different programs
		monitorOption = new JMenu("View");
		
		adminDatabaseItem = new JMenuItem("Administer Form Database");
		adminDatabaseItem.addActionListener(this);
		monitorOption.add(adminDatabaseItem);
	}
	
	private void createServerMenu() { // tools
		serverOptions = new JMenu("Tools");
		
		viewConnectionsItem = new JMenuItem("View Connected Users...");
		viewConnectionsItem.addActionListener(this);
		serverOptions.add(viewConnectionsItem);
		
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
