package com.bank.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	public static void sendAttach(String to, String subject, File f) {


				String from = "";

				String host = "smtp.gmail.com";

				Properties properties = System.getProperties();

				// Setup mail server
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.auth", "true");

				// Get the Session object.// and pass
				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {

						return new PasswordAuthentication("", "");

					}

				});
				// session.setDebug(true);
				try {

					MimeMessage message = new MimeMessage(session);

					message.setFrom(new InternetAddress(from));

					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

					message.setSubject(subject);

					Multipart multipart = new MimeMultipart();

					MimeBodyPart attachmentPart = new MimeBodyPart();

					MimeBodyPart textPart = new MimeBodyPart();

					try {

						attachmentPart.attachFile(f);
						textPart.setText("Transaction History");
						multipart.addBodyPart(textPart);
						multipart.addBodyPart(attachmentPart);

					} catch (IOException e) {

						e.printStackTrace();

					}

					message.setContent(multipart);

					Transport.send(message);

				} catch (MessagingException mex) {
					mex.printStackTrace();
				}
			
	}

	public static void send(String to, String subject, String msg) {

	

				String from = "";

				String host = "smtp.gmail.com";

				Properties properties = System.getProperties();

				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.auth", "true");

				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {

						return new PasswordAuthentication("", "");

					}

				});

//        session.setDebug(true);

				try {

					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(from));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					message.setSubject(subject);
					message.setText(msg);
					Transport.send(message);

				} catch (MessagingException mex) {
					mex.printStackTrace();
				}
			
	}
}
