package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Email_Sender;

import java.awt.print.Book;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Report;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.User;



public class Email_Automation {
	 public static boolean sendEmail(User user) {
	        final String from = "gpatole473@gmail.com";
	      final String password = "lnvycjtvejjyqtct";  // Use App Password for Gmail
	        String recipientEmail = user.getEmail();         // R
	        // SMTP server configuration
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        // Create a Session with Authentication
	        Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            });

	        try {
	            // Compose the message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail));
	            message.setSubject("🎉 Account Registration Successful!");
	            message.setText("Dear " + user.getName() + ",\n\n"
	                    + "Welcome to Ganesh's Application!\n\n"
	                    + "Your account has been successfully created.\n"
	                    + "You can now log in using your registered email address.\n\n"
	                    + "If you did not initiate this registration, please contact our support immediately.\n\n"
	                    + "Thank you for joining us!\n\n"
	                    + "Best Regards,\n"
	                    + "Ganesh Patole\n"
	                    + "Team GaneshApp");

	            // Send the message
	            Transport.send(message);
	            System.out.println("✅ Email sent successfully to " + recipientEmail);
                return true;
	        } catch (MessagingException e) {
	            e.printStackTrace();
	            System.out.println("❌ Error while sending email: " + e.getMessage());
	        }
			return false;
}
	  public static String sendEmailForgetPassword(User user) {
	        final String from = "gpatole473@gmail.com";
	      final String password = "lnvycjtvejjyqtct";  // Use App Password for Gmail
	        String recipientEmail = user.getEmail();         // R
	        // SMTP server configuration
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        // Create a Session with Authentication
	        Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            });

	        try {
	            // Compose the message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail));
	            message.setSubject("🎉 Password has been updated Successful!");
	            message.setText("Dear " + user.getName()+ ",\n\n"
	                    + "Welcome to Ganesh's Application!\n\n"
	                    + "Your account Password has been successfully Updated.\n"
	                    + "Your new password is:"
	                  +  "If you did not initiate this  Activity, please contact our support immediately.\n\n"
	                    + "Thank you for joining us!\n\n"
	                    + "Best Regards,\n"
	                    + "Ganesh Patole\n"
	                    + "Team GaneshApp");

	            // Send the message
	            Transport.send(message);
	             return "✅ Email sent successfully to " +user.getEmail();

	        } catch (MessagingException e) {
	            e.printStackTrace();
	            return "❌ Error while sending email:" + e.getMessage();
	        }
}
	  public static String sendEmailInfoUpdate(User user) {
	        final String from = "gpatole473@gmail.com";
	      final String password = "lnvycjtvejjyqtct";  // Use App Password for Gmail
	        String recipientEmail = user.getEmail();         // R
	        // SMTP server configuration
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        // Create a Session with Authentication
	        Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            });

	        try {
	            // Compose the message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail));
	            message.setSubject("🎉 Account Information has been updated Successful!");
	            message.setText("Dear " + user.getName()+ ",\n\n"
	                    + "Welcome to Ganesh's Application!\n\n"
	                    + "Your account Information has been successfully Updated.\n"
	                  +  "If you did not initiate this  Activity, please contact our support immediately.\n\n"
	                    + "Thank you for joining us!\n\n"
	                    + "Best Regards,\n"
	                    + "Ganesh Patole\n"
	                    + "Team GaneshApp");

	            // Send the message
	            Transport.send(message);
	             return "✅ Email sent successfully to " +user.getEmail();

	        } catch (MessagingException e) {
	            e.printStackTrace();
	            return "❌ Error while sending email:" + e.getMessage();
	        }
}
	  
	  public static String sendEmailACdelete(User user) {
	        final String from = "gpatole473@gmail.com";
	      final String password = "lnvycjtvejjyqtct";  // Use App Password for Gmail
	        String recipientEmail = user.getEmail();         // R
	        // SMTP server configuration
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        // Create a Session with Authentication
	        Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            });

	        try {
	            // Compose the message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail));
	            message.setSubject("🎉 Account has been Deleted Successful!");
	            message.setText("Dear " + user.getName()+ ",\n\n"
	                    + "Welcome to Ganesh's Application!\n\n"
	                    + "Your account Information has been successfully Updated.\n"
	                  +  "If you did not initiate this  Activity, please contact our support immediately.\n\n"
	                    + "Thank you for joining us!\n\n"
	                    + "Best Regards,\n"
	                    + "Ganesh Patole\n"
	                    + "Team GaneshApp");

	            // Send the message
	            Transport.send(message);
	             return "✅ Email sent successfully to " +user.getEmail();

	        } catch (MessagingException e) {
	            e.printStackTrace();
	            return "❌ Error while sending email:" + e.getMessage();
	        }
}
	  
	  public static String sendEmailDueDate( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book isissuedbyuser) {
	        final String from = "gpatole473@gmail.com";
	      final String password = "lnvycjtvejjyqtct";  // Use App Password for Gmail
	        String recipientEmail = isissuedbyuser.getIssuedTo().getEmail();         // R
	        // SMTP server configuration
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        // Create a Session with Authentication
	        Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            });

	        try {
	            // Compose the message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail));
	            message.setSubject("Your Library Book Return Date Has Passed");
	            message.setText("Dear " + isissuedbyuser.getIssuedTo().getName()+ ",\n\n"
	                    + "Please return the book as soon as possible to avoid further late fees or account restrictions.\n"
	                    + "If you have already returned the book, please disregard this notice.  \n"
	                  +  "For any queries, feel free to contact the library helpdesk at 91+8530694083..\n"
	                    + "Thank you for joining us!\n\n"
	                    + "Best Regards,\n"
	                    + "Ganesh Patole\n"
	                    + "Team GaneshApp");

	            // Send the message
	            Transport.send(message);
	             return "✅ Email sent successfully to " +isissuedbyuser.getIssuedTo().getEmail();

	        } catch (MessagingException e) {
	            e.printStackTrace();
	            return "❌ Error while sending email:" + e.getMessage();
	        }
} 
	  public static String sendEmailForTwoFactorAuthentication(User user) {
	        final String from = "gpatole473@gmail.com";
	      final String password = "lnvycjtvejjyqtct";  // Use App Password for Gmail
	        String recipientEmail = user.getEmail();         // R
	        // SMTP server configuration
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        // Create a Session with Authentication
	        Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            });

	        try {
	            // Compose the message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail));
	            message.setSubject(" Two Factor Authentication Code");
	            message.setText("Dear " + user.getName()+ ",\n\n"
	                    + "Welcome to Ganesh's Application!\n\n"
	                    + "Your Two Factor Authentication code is.\n" + user.getTwoFactorAuthCode() + "\n"
	                  +  "If you did not initiate this  Activity, please contact our support immediately.\n\n"
	                    + "Thank you for joining us!\n\n"
	                    + "Best Regards,\n"
	                    + "Ganesh Patole\n"
	                    + "Team GaneshApp");

	            // Send the message
	            Transport.send(message);
	             return "✅ Email sent successfully to " +user.getEmail();

	        } catch (MessagingException e) {
	            e.printStackTrace();
	            return "❌ Error while sending email:" + e.getMessage();
	        }
}  
	  public static String sendEmailToDisableTwoFactorAuthentication(User user) {
	        final String from = "gpatole473@gmail.com";
	      final String password = "lnvycjtvejjyqtct";  // Use App Password for Gmail
	        String recipientEmail = user.getEmail();         // R
	        // SMTP server configuration
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        // Create a Session with Authentication
	        Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(from, password);
	                }
	            });

	        try {
	            // Compose the message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(recipientEmail));
	            message.setSubject(" Two Factor Authentication Code");
	            message.setText("Dear " + user.getName()+ ",\n\n"
	                    + "Welcome to Ganesh's Application!\n\n"
	                    + " Factor Authentication has been disabled successfully.\n"
	                  +  "If you did not initiate this  Activity, please contact our support immediately.\n\n"
	                    + "Thank you for joining us!\n\n"
	                    + "Best Regards,\n"
	                    + "Ganesh Patole\n"
	                    + "Team GaneshApp");

	            // Send the message
	            Transport.send(message);
	             return "✅ Email sent successfully to " +user.getEmail();

	        } catch (MessagingException e) {
	            e.printStackTrace();
	            return "❌ Error while sending email:" + e.getMessage();
	        }
		}

		public static String EmailSendForReportReview(edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Review review , Report report) {
			final String from = "gpatole473@gmail.com";
			final String password = "lnvycjtvejjyqtct"; // Use App Password for Gmail
			final String adminEmail = "gpatole899@gmail.com"; // Replace with actual admin email

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, password);
				}
			});

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
				message.setSubject("Review Report Notification");
				message.setText("A review has been reported.\n\n" + "Reported By: " + report.getReportedBy().getName() + " ("
						+ report.getReportedBy().getEmail() + ")\n" + "Review ID: " + review.getReviewId() + "\n" + "Comment: "
						+ review.getComment() + "\n" + "Reason: " + report.getReason() + "\n\n"
						+ "Please review and take necessary action.\n\n" + "Best Regards,\n"
						+ "GaneshApp Automated System");

				Transport.send(message);
				return "✅ Report email sent successfully to admin.";
			} catch (MessagingException e) {
				e.printStackTrace();
				return "❌ Error while sending report email: " + e.getMessage();
			}
		}
 
}
