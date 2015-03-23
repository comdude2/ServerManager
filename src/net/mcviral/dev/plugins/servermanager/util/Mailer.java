package net.mcviral.dev.plugins.servermanager.util;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import net.mcviral.dev.plugins.servermanager.main.ServerManager;
//import javax.activation.*;

public class Mailer {
	
	private ServerManager server;
	private String senderEmail = null;
	private String host = null;
	private String username = null;
	private String password = null;
	
	public Mailer(ServerManager server, String senderEmail, String host, String username, String password){
		this.server = server;
		this.senderEmail = senderEmail;
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	public void sendMail(String email, String subject, String msg){
		// Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);
	      
	      //Set username and pass
	      properties.setProperty("mail.user", username);
	      properties.setProperty("mail.password", password);
	      
	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);
		try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(senderEmail));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(email));

	         // Set Subject: header field
	         message.setSubject(subject);

	         // Now set the actual message
	         message.setText(msg);

	         // Send message
	         Transport.send(message);
	         server.log.info("Sent message successfully.");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	
}
