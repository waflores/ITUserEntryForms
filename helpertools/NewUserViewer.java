package helpertools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NewUserViewer implements ActionListener, ListSelectionListener, Runnable {
	private String newLine = System.getProperty("line.separator");
	private String atSign = "@"; // newLine only serves as a separator... not an actual newLine char
	
	/* Main display window */
	private JFrame dispWindow = new JFrame("Connection Viewer"); // the viewing window :)
	private JList<String> connList = new JList<String>(); // list of classes to display
	private JLabel connViewLabel = new JLabel("Select a connection to view:");
	private JScrollPane connListPane = new JScrollPane(connList);
	private JButton selectConnButton = new JButton("Select Active User");
	private int selectedUsernName = -1; // the selected classname from the JList
	
	/* Method display window */
	private JFrame propertiesWindow = new JFrame(); 
	private JList<String> propertiesList = new JList<String>(); // list of methods to invoke
	private JLabel propertiesViewLabel = new JLabel("Active Connection Properties:");
	private JScrollPane propertiesListPane = new JScrollPane(propertiesList);
	private JButton killConnectionButton = new JButton("Disconnect User");
	private String selectedUser = null; // method name from JList
	private ObjectOutputStream ACOoos = null;
	
	/* New User Form Generator  */
	private JFrame formGenWindow = new JFrame("Form Generator");
	private JButton formGen = new JButton("Generate Form");
	private JButton prevFormGen = new JButton("Prev User");
	private JButton nextFormGen = new JButton("Next User");
	private JButton exitFormGen = new JButton("Close");
	private JTextArea userDataView = new JTextArea();
	private JScrollPane outScrollPane = new JScrollPane(userDataView);
	private JScrollBar vsb = outScrollPane.getVerticalScrollBar();
	private JPanel formBtnPanel = new JPanel();
	
	public Vector<NewUser> users = 
			new Vector<NewUser>();
	
	public NewUserViewer(Vector<NewUser> usersToBeAdded) throws Exception {
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
		updateUserList(usersToBeAdded);
		
		/* Properties Display Attributes */
		/* Main window attributes */
		killConnectionButton.addActionListener(this);
		killConnectionButton.setEnabled(false);
		propertiesList.addListSelectionListener(this);
		propertiesList.setSelectionMode(0); // Single selection of methods to invoke
		
		/* Method Display Window */
		propertiesWindow.setSize(300, 200);
		propertiesWindow.getContentPane().add(propertiesViewLabel, "North");
		propertiesWindow.getContentPane().add(killConnectionButton, "South");
		propertiesWindow.getContentPane().add(propertiesListPane, "Center");
		propertiesWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		propertiesWindow.setVisible(false);
		
		/* Form generator window*/
		formGenWindow.add(outScrollPane, "Center");
		formGenWindow.add(formBtnPanel, "South");
		formBtnPanel.add(prevFormGen);
		formBtnPanel.add(nextFormGen);
		formBtnPanel.add(formGen);
		formBtnPanel.add(exitFormGen);
		prevFormGen.addActionListener(this);
		nextFormGen.addActionListener(this);
		formGen.addActionListener(this);
		exitFormGen.addActionListener(this);
		userDataView.setEditable(false);
		formGenWindow.setSize(450, 450);
		formGenWindow.setVisible(false);
		formGenWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		/* Don't Exit the program when the GUI is closed */
		dispWindow.setVisible(true);
		dispWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void updateUserList(Vector<NewUser> people) {
		users = people; // update the list of clients
		
		// update active connection objects
		Vector<String> userData = new Vector<String>();
		
		for (NewUser user : users) {
			String dataList = user.getF_name() + " " + user.getM_initials() + " " + user.getL_name(); // separate the data
			userData.add(dataList);
		}
		connList.setListData(userData);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		// Re-enable the buttons that were disabled
		selectConnButton.setEnabled(true);
		killConnectionButton.setEnabled(true);
		
		if(lse.getValueIsAdjusting()) return; //de-bounce the JList Selection
		/* Handle selections */
		else if (lse.getSource() == connList) {
			selectedUsernName = connList.getSelectedIndex();
		}
		else if (lse.getSource() == propertiesList) {
			selectedUser = propertiesList.getSelectedValue();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// Handle the button presses
		if (ae.getSource() == selectConnButton) {
			//JOptionPane.showMessageDialog(dispWindow, users.elementAt(selectedUsernName).toString().replaceAll(atSign, newLine));
			userDataView.setText(users.elementAt(selectedUsernName).toString().replaceAll(atSign, newLine));
			formGenWindow.setVisible(true);
		}
		if (ae.getSource() == killConnectionButton) {
			try {
				ACOoos.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(propertiesWindow, e.getMessage());
			}
		}
		if (ae.getSource() == formGen) {
			// Generate the form and use the JFileChooser
			JOptionPane.showMessageDialog(formGenWindow, "Generate a form!");
		}
		if (ae.getSource() == nextFormGen) {
			// Show the next person in the list
			try {
				selectedUsernName += 1;
				userDataView.setText(users.elementAt(selectedUsernName).toString().replaceAll(atSign, newLine));
			}
			catch (ArrayIndexOutOfBoundsException aioobe) {
				selectedUsernName = 0; // get back the to the start of the list
				userDataView.setText(users.elementAt(selectedUsernName).toString().replaceAll(atSign, newLine));
			}
		}
		if (ae.getSource() == prevFormGen) {
			try {
				selectedUsernName -= 1;
				userDataView.setText(users.elementAt(selectedUsernName).toString().replaceAll(atSign, newLine));
			}
			catch (ArrayIndexOutOfBoundsException aioobe) {
				selectedUsernName = users.size() - 1;
				userDataView.setText(users.elementAt(selectedUsernName).toString().replaceAll(atSign, newLine));
			}
		}
		if (ae.getSource() == exitFormGen) {
			// close this form generating gui
			formGenWindow.dispose();
		}
	}

	private void generateForm(NewUser newUser) {
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
