package com.email.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	@Autowired
	private SendGrid sendGrid;
	@Value("${sendgrid.from.email}")
	private String fromEmail;
	@Value("${sendgrid.from.name}")
	private String fromName;

	public boolean sendTextEmail(String name, String email, String subject, String message) {
		 // ✅ Verified sender
	    Email from = new Email(email, fromName);

	    // ✅ YOU receive the email
	    Email to = new Email(fromEmail);

	    Content content = new Content(
	        "text/plain",
	        "Name: " + name +
	        "\nEmail: " + email +
	        "\n\nMessage:\n" + message
	    );

	    Mail mail = new Mail(from, subject, to, content);

	    // ✅ Set Reply-To to the user
	    mail.setReplyTo(new Email(email));

	    return sendEmail(mail);
	}

	public boolean sendHtmlEmail(String toEmail, String subject, String htmlBody) {
		Email from = new Email(fromEmail, fromName);
		Email to = new Email(toEmail);
		Content content = new Content("text/html", htmlBody);
		Mail mail = new Mail(from, subject, to, content);
		return sendEmail(mail);
	}

	private boolean sendEmail(Mail mail) {
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sendGrid.api(request);
			logger.info("Email sent. Status code: {}", response.getStatusCode());
			logger.debug("Response body: {}", response.getBody());
			logger.debug("Response headers: {}", response.getHeaders());
			return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
		} catch (IOException ex) {
			logger.error("Error sending email: ", ex);
			return false;
		}
	}
}