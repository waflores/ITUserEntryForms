package misctools;
import java.io.IOException;
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

 
public class SendMailTLS {


	@SuppressWarnings("unused")
	public SendMailTLS(final String username, final String password, String file) throws MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		/* Use an authenticator to connect to gmail */
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		
		/* Set up a new message */
		
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(username));
			message.setSubject("Testing Subject");
			if (file != null) {
				// Attach the specified file, so there needs to be
				// a multipart message created.
				MimeBodyPart mbp1 = new MimeBodyPart();
				mbp1.setText("Here's that pdf you wanted...");
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.attachFile(file);
				MimeMultipart mp = new MimeMultipart();
				mp.addBodyPart(mbp1);
				mp.addBodyPart(mbp2);
				message.setContent(mp);
			}
			else {
				message.setText("Dear Mail Crawler,"
						+ "\n\n No spam to my email, please!");
			}
			Transport.send(message);
 
//			System.out.println("Done");

	}

}