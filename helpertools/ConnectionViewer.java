package helpertools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/* 
 * File:   ConnectionViewer.java
 * Author: Will Flores waflores@ncsu.edu
 * 
 * ConnectionViewer: Will let us see who's connected to the server.
 * 
 * Created on August 24, 2012
 */
@SuppressWarnings("unused")
public class ConnectionViewer implements ActionListener, ListSelectionListener, Runnable {
	/* Main display window */
	private JFrame dispWindow = new JFrame("Connection Viewer"); // the viewing window :)
	private JList<String> connList = new JList<String>(); // list of classes to display
	private JLabel connViewLabel = new JLabel("Select a connection to view:");
	private JScrollPane connListPane = new JScrollPane(connList);
	private JButton selectConnButton = new JButton("Select Active User");
	private String selectedConnectionName = null; // the selected classname from the JList
	
	/* Method display window */
	private JFrame propertiesWindow = new JFrame(); 
	private JList<String> propertiesList = new JList<String>(); // list of methods to invoke
	private JLabel propertiesViewLabel = new JLabel("Active Connection Properties:");
	private JScrollPane propertiesListPane = new JScrollPane(propertiesList);
	private JButton killConnectionButton = new JButton("Disconnect User");
	private JButton sendConnectionButton = new JButton("Send Object");
	private String selectedUser = null; // method name from JList
	private ObjectOutputStream ACOoos = null;
	private JPanel propbtnpanel = new JPanel();
	
	public ConcurrentHashMap<String, HashMap<ActiveConnectionObj, ObjectOutputStream>> clients = 
			new ConcurrentHashMap<String, HashMap<ActiveConnectionObj, ObjectOutputStream>>();
	
	public ConnectionViewer(ConcurrentHashMap<String, HashMap<ActiveConnectionObj, ObjectOutputStream>> connections) throws Exception {
		/* Main window attributes */
		selectConnButton.addActionListener(this);
		selectConnButton.setEnabled(false);
		connList.addListSelectionListener(this);
		connList.setSelectionMode(0); // Single selection of classes to instantiate
		
		/* Build the Main Window */
		dispWindow.setSize(300, 200);
		dispWindow.getContentPane().add(connViewLabel, "North");
		dispWindow.getContentPane().add(selectConnButton, "South");
		dispWindow.getContentPane().add(connListPane, "Center");
		
		/* Initialize list of connections */
		updateConnectionList(connections);
		
		/* Properties Display Attributes */
		/* Main window attributes */
		killConnectionButton.addActionListener(this);
		killConnectionButton.setEnabled(false);
		sendConnectionButton.addActionListener(this);
		sendConnectionButton.setEnabled(false);
		propertiesList.addListSelectionListener(this);
		propertiesList.setSelectionMode(0); // Single selection of methods to invoke
		
		/* Method Display Window */
		propertiesWindow.setSize(300, 200);
		propertiesWindow.getContentPane().add(propertiesViewLabel, "North");
		propbtnpanel.add(killConnectionButton);
		propbtnpanel.add(sendConnectionButton);
		propertiesWindow.getContentPane().add(propbtnpanel, "South");
		propertiesWindow.getContentPane().add(propertiesListPane, "Center");
		propertiesWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		propertiesWindow.setVisible(false);
		
		/* Exit the program when the GUI is closed */
		dispWindow.setVisible(true);
		dispWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void updateConnectionList(ConcurrentHashMap<String, HashMap<ActiveConnectionObj, ObjectOutputStream>> connections) {
		clients = connections; // update the list of clients
		
		// update active connection objects
		Vector<String> dataL = new Vector<String>();
		Set<String> peopleLoggedIn = clients.keySet();
		
			try {
				dataL.addAll(peopleLoggedIn);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		connList.setListData(dataL);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		// Re-enable the buttons that were disabled
		selectConnButton.setEnabled(true);
		killConnectionButton.setEnabled(true);
		sendConnectionButton.setEnabled(true);
		
		if(lse.getValueIsAdjusting()) return; //de-bounce the JList Selection
		/* Handle selections */
		else if (lse.getSource() == connList) {
			selectedConnectionName = connList.getSelectedValue();
		}
		else if (lse.getSource() == propertiesList) {
			selectedUser = propertiesList.getSelectedValue();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// Handle the button presses
		if (ae.getSource() == selectConnButton) {
			HashMap<ActiveConnectionObj, ObjectOutputStream> userInQuestion = clients.get(selectedConnectionName);
			Object[] clientACOArray = userInQuestion.keySet().toArray(); // get the ActiveConnectionObject
//			Object clientOOS = userInQuestion.values();
			ActiveConnectionObj clientACO = (ActiveConnectionObj) clientACOArray[0];
			
			ACOoos = userInQuestion.get(clientACO);
			
			Vector<String> propData = new Vector<String>();
			
			propData.add(clientACO.getUserName());
			propData.add(clientACO.getConnectionStatus());
			propData.add(clientACO.getCompName());
			propData.add(clientACO.getLoginTime().toString());
			
			propertiesList.setListData(propData);
			propertiesWindow.setVisible(true);
			
		}
		if (ae.getSource() == killConnectionButton) {
			try {
				ACOoos.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(propertiesWindow, e.getMessage());
			}
		}
		if (ae.getSource() == sendConnectionButton) {
			// new ObjectDiagnostic Object
			try {
				String msg = JOptionPane.showInputDialog("Send a message to the user.");
				ACOoos.writeUTF(msg.trim());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(propertiesWindow, e.getMessage());
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
