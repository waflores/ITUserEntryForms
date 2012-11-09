package clientapp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import helpertools.*;

@SuppressWarnings({ "unused" })
public class ITUserInputProgram implements ActionListener {
	// Form GUI
	private JFrame clientWindow = new JFrame("New User Input Client");
	private JPanel mainPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	
	/* Radiobuttons */
	private JRadioButton regEmp = new JRadioButton("Regular", true); // when active
	private JRadioButton tempContract = new JRadioButton("Temporary/ Contract");
	private ButtonGroup btnGroup1 = new ButtonGroup(); // group for our radio buttons
	
	private JRadioButton empIsRehire = new JRadioButton("Yes");
	private JRadioButton empIsNotRehire	= new JRadioButton("No", true);
	private ButtonGroup btnGroup2 = new ButtonGroup();
	
	/* Text labels */
	private JLabel lastName = new JLabel("Last Name: ");
	private JLabel firstName = new JLabel ("Legal First Name: ");
	private JLabel midName = new JLabel("Middle Initial(s): ");
	private JLabel prefName = new JLabel("Preferred Name: ");
	private JLabel empLocation = new JLabel("Employee Location: ");
	private JLabel empStartDate = new JLabel("Employee Start Date: ");
	private JLabel empRehire = new JLabel("Is the employee a re-hire?");
	private JLabel empTitle = new JLabel ("Title: ");
	private JLabel empManager = new JLabel("Manager: ");
	private JLabel empDept = new JLabel ("Department: ");
	private JLabel empID = new JLabel ("Diosynth Employee ID: ");
	private JLabel empStatus = new JLabel ("Employee Status: ");
	
	/* Text fields */
	private JTextField lastNametxt = new JTextField(8);
	private JTextField firstNametxt = new JTextField (8);
	private JTextField midNametxt = new JTextField(8);
	private JTextField prefNametxt = new JTextField(8);
//	private JTextField empLocationtxt = new JTextField(8);
	private JTextField empStartDatetxt = new JTextField(8);
	private JTextField empTitletxt = new JTextField (8);
	private JTextField empManagertxt = new JTextField(8);
	private JTextField empDepttxt = new JTextField (8);
	private JTextField empIDtxt = new JTextField (8);
	
	/* Selection Items */
	private String[] comboBoxItem = {"THC", "RTP", "WES", "KCL", "D&I"};
	private JComboBox<?> empLoc = new JComboBox<Object>(comboBoxItem);
	
	/* Control Buttons */
	private JButton submitButton = new JButton("Submit");
	private JButton clearButton = new JButton("Clear Form");
	private JButton selectDate = new JButton("Select Date");
//	private JButton windowSizeButton = new JButton("WindowSize");	
	
	public ITUserInputProgram(String serverAddress) throws Exception {
		/* Start building GUI */
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));		
		clientWindow.getContentPane().add(mainPanel, "Center");
		clientWindow.getContentPane().add(bottomPanel, "South");
		
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		// Start adding things to the gui
		mainPanel.add(firstName);
		mainPanel.add(firstNametxt);
		mainPanel.add(midName);
		mainPanel.add(midNametxt);
		mainPanel.add(lastName);
		mainPanel.add(lastNametxt);
		mainPanel.add(prefName);
		mainPanel.add(prefNametxt);
		mainPanel.add(empID);
		mainPanel.add(empIDtxt);
		mainPanel.add(empTitle);
		mainPanel.add(empTitletxt);
		mainPanel.add(empManager);
		mainPanel.add(empManagertxt);
		mainPanel.add(empLocation);
		mainPanel.add(empLoc); // a dropdown menu
		mainPanel.add(empDept);
		mainPanel.add(empDepttxt);
		mainPanel.add(empStartDate);
		mainPanel.add(empStartDatetxt);
		empStartDatetxt.setEditable(false); // Just display whatever the user chooses as a date
//		mainPanel.add(selectDate); // Instantiate datePicker
//		selectDate.addActionListener(this);
		
//		rightPanel.add(Box.createHorizontalStrut(15)); // create some space in the GUI
		mainPanel.add(Box.createHorizontalStrut(5));
		
		mainPanel.add(empRehire);
//		mainPanel.add(empIsNotRehire);
//		mainPanel.add(empIsRehire);	
		mainPanel.add(Box.createHorizontalStrut(15)); // create some space in the GUI
//		mainPanel.add(empStatus);
//		mainPanel.add(regEmp);
//		mainPanel.add(tempContract);
		
		// radio button functionality
//		btnGroup1.add(regEmp);
//		btnGroup1.add(tempContract);
//		btnGroup2.add(empIsNotRehire);
//		btnGroup2.add(empIsRehire);
		
		// Form Buttons
		bottomPanel.add(clearButton);
		clearButton.addActionListener(this);
		bottomPanel.add(submitButton);
		submitButton.addActionListener(this);
		
		// For diagnostic purposes
//		windowSizeButton.addActionListener(this);
//		windowSizeButton.setMnemonic(KeyEvent.VK_ENTER);
//		bottomPanel.add(windowSizeButton);
//		windowSizeButton.setVisible(false);
		
		/* Window Attributes */
		clientWindow.setSize(300, 610);
		clientWindow.setVisible(true);
//		clientWindow.setResizable(false);
//		clientWindow.setLocation(250, 250);
		clientWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String whereToConnect = JOptionPane.showInputDialog("Where to connect?", "localhost");
			try {
				new ITUserInputProgram(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
				
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getSource() == clearButton) {
			// Popup and ask before making changes
			int response = JOptionPane.showConfirmDialog(clientWindow, "Are you sure you want to clear the form?", 
					"Erase all form entries?", JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) clearForm();
			else {
				JOptionPane.showMessageDialog(clientWindow, "Entries not Erased.", "Clear Form Cancelled"
						, JOptionPane.PLAIN_MESSAGE);
			}
		}
		if (ae.getSource() == submitButton) {
			/* Ask before submitting processing */
			int response = JOptionPane.showConfirmDialog(clientWindow, "Are you sure you want to submit the form?", 
					"Submit User", JOptionPane.YES_NO_OPTION);
			
			if (response == JOptionPane.YES_OPTION) processUser();
			else {
				JOptionPane.showMessageDialog(clientWindow, "User form not submitted.", "Submit Form Cancelled"
						, JOptionPane.PLAIN_MESSAGE);
			}
		}
//		if(ae.getSource() == windowSizeButton) {
//			Dimension dim = clientWindow.getSize();
//			System.out.println(dim);
//		}
//		if (ae.getSource() == selectDate) {
//			empStartDatetxt.setText(new DatePicker(clientWindow).setPickedDate());
//		}
//		if (ae.getSource() == EsubmitBtn){
//			final String user = emailField.getText();
//			char[] pass = passwordField.getPassword();
//	
//			// Get authentication
//			try {
//				oos.writeObject(new RequestAuth(user, pass, this.filename));
//				emailFrame.dispose();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				JOptionPane.showMessageDialog(clientWindow, e.toString());
//			}
//		}
		
		
	}
	private void processUser () {
		
		/* Get the input from the form */
		String l_name = lastNametxt.getText().trim();
		String f_name = firstNametxt.getText().trim();
		String m_name = midNametxt.getText().trim();
		String p_name = prefNametxt.getText().trim();
		String emp_loc = empLoc.getSelectedItem().toString().trim();
		String emp_start = empStartDatetxt.getText().trim();
		String emp_title = empTitletxt.getText().trim();
		String emp_manager = empManagertxt.getText().trim();
		String emp_dept = empDepttxt.getText().trim();
		String emp_ID = empIDtxt.getText().trim();
		boolean emp_rehire = empIsRehire.isSelected();
		boolean emp_reg = regEmp.isSelected();
		
		if (f_name.length() == 0 || l_name.length() == 0 || emp_ID.length() == 0 || emp_title.length() == 0) {
			JOptionPane.showMessageDialog(clientWindow, "First name, Last Name, Employee ID, and Title need to be filled in!");
		}
		else { 
			
//			try {
//				/* Create user object */
//				NewUser u = new NewUser(f_name, l_name, p_name, m_name, emp_title, emp_manager, emp_dept, emp_ID, emp_loc, emp_start, emp_rehire, emp_reg);
//				/* Send it to the server in an Object and receive a payload to process */
//				oos.writeObject(u);
//				
//				// get form status
//				Object fs = ois.readObject();
//				
//				if (((FormStatus) fs).getStatusID() == 1) {
//					/* Get authentication request */
//					emailIt(u.getFileName());
//				}
//			} catch (Exception e) {
//				// Code the part where the server's down and you're sending the info it
//				JOptionPane.showMessageDialog(null, "Restart application or call IT.", "Connection to server lost.", JOptionPane.ERROR_MESSAGE);
//				e.printStackTrace();
//			}
			
			// Clear the form when we're done and expect a gui interface to send us a link to the pdf of the form
			clearForm();
		}	
	}
	
	private void clearForm() {
		lastNametxt.setText("");
		firstNametxt.setText("");
		midNametxt.setText("");
		prefNametxt.setText("");
		empLoc.setSelectedIndex(0);
		empStartDatetxt.setText("");
		empTitletxt.setText("");
		empManagertxt.setText("");
		empDepttxt.setText("");
		empIDtxt.setText("");
		regEmp.setSelected(true);
		empIsNotRehire.setSelected(true);
	}
}
