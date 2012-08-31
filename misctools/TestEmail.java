package misctools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.mail.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

 
// Send a simple, single part, text/plain e-mail
public class TestEmail implements ActionListener {
	private JLabel emailLabel = new JLabel("Email address:");
	private JLabel passwordLabel = new JLabel("Password:");
	private JTextField emailField = new JTextField(16);
	private JPasswordField passwordField = new JPasswordField(16);
	private JButton submitBtn = new JButton("Submit");
	private JFrame emailFrame = new JFrame("Google Mail login");
	private JPanel mainPanel = new JPanel();
	private JLabel statusMsgs = new JLabel("Click submit to login...");
	
    public static void main(String[] args) {
    	new TestEmail();
    }
    
    public TestEmail() {
		emailFrame.getContentPane().add(mainPanel, "Center");
		mainPanel.add(emailLabel);
		mainPanel.add(emailField);
		mainPanel.add(passwordLabel);
		mainPanel.add(passwordField);
		mainPanel.add(statusMsgs);
		mainPanel.add(submitBtn);
		
		submitBtn.addActionListener(this);
		
		emailFrame.setSize(300, 125);
		emailFrame.setVisible(true);
		emailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == submitBtn) {
			final String user = emailField.getText();
			char[] pass = passwordField.getPassword();
			final String epassword = new String(pass);
			
			try {
				statusMsgs.setText("Sending message...");
				new SendMailTLS(user, epassword, "E:\\");
				emailField.setText("");
				passwordField.setText("");
				statusMsgs.setText("Message sent!");
			}
			catch (MessagingException me){
				me.printStackTrace();
				JOptionPane.showMessageDialog(null, "The password and username are not correct.");
			}
			catch (IOException ioe) {
				JOptionPane.showMessageDialog(null, "Couldn't find the attachment...");
			}
			statusMsgs.setText("Click submit to login...");
		}
		
	}
}//End of class