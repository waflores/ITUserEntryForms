import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@SuppressWarnings("serial")
public class RequestAuth implements /*ActionListener,*/ Serializable/*, Runnable */{
	private String file = null;
	private String username;
//	public boolean authenticated;
	private Session session;
	private char[] password;
	
	public RequestAuth(String username,char[] password, String file) {
		this.file = file;
		this.username = username;
		this.password = password;
	}

//	@Override
//	public void actionPerformed(ActionEvent ae) {
//		if (ae.getSource() == submitBtn) {
//			System.out.println("About to wake up thread...");
//			synchronized (this) {
//				t.notify(); // wake the thread up
//			}
//			System.out.println("Thread is alive?: " + t.isAlive());
//			final String user = emailField.getText();
//			char[] pass = passwordField.getPassword();
//			final String epassword = new String(pass);
//			System.out.println("About to send msg...");
//			try {
//				System.out.println("About to authenticate...");
//				statusMsgs.setText("Sending message...");
//				Authenticate(user, epassword);
//				System.out.println("Authenticated!");
////				sendMessageToUser(this.session, user, file);
//				emailField.setText("");
//				passwordField.setText("");
//				statusMsgs.setText("Message sent!");
//				emailFrame.dispose();
//			}
//			catch (Exception me){
//				me.printStackTrace();
//				JOptionPane.showMessageDialog(null, "The password and username are not correct.");
//			}
//			statusMsgs.setText("Click submit to login...");
//		}
//	}
	
	public void Authenticate () {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		/* Use an authenticator to connect to gmail */
		final String pass = new String(password);
		final String user = this.username;
		
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		  });
		// clear out password when done
		for (int i = 0; i < password.length; i++) {
			password[i] = '0';
		}
		this.session = session;
	}
	
	public void sendMessageToUser() throws AddressException, MessagingException, IOException {
		System.out.println("Setting up message to send.");
		/* Set up a new message */
		Message message = new MimeMessage(this.session);
		message.setFrom(new InternetAddress(this.username));
		message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(this.username));
		message.setSubject("Request for New User Account Sent");
		if (file != null) {
			// Attach the specified file, so there needs to be
			// a multipart message created.
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText("This is the form that was generated for this user " + this.file);
			MimeBodyPart mbp2 = new MimeBodyPart();
			mbp2.attachFile(this.file);
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);
			message.setContent(mp);
		}
		else {
			message.setText("Request sent to Network Administrator.");
		}
		Transport.send(message);
		System.out.println("Actually sent message.");
	}
	
	public void updateFilename(String filename) {
		this.file = filename;
	}
	public String getFilename() {
		return this.file;
	}
//	public boolean isAuthenticated() {
//		return this.authenticated;
//	}

//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		System.out.println("In the run method...");
//		try {
//			synchronized(this) {
//				if (!isAuthenticated()) {
//					while (authenticated == false) {
//						System.out.println("waiting...");
//						t.wait(5);
//					}
//				}
//				else {
//					System.out.println("Out of waiting!");
//					sendMessageToUser();
//				}
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (AddressException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
