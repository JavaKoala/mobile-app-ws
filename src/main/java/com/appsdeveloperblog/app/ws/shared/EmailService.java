package com.appsdeveloperblog.app.ws.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

@Service
public class EmailService {

		final String FROM = "no-reply@mobile-app-ws.com";

		// The subject line for the email.
		final String SUBJECT = "One last step to complete your registration with PhotoApp";
		
		final String PASSWORD_RESET_SUBJECT = "Password reset request";

		// The HTML body for the email.
		final String HTMLBODY = "<h1>Please verify your email address</h1>"
				+ "<p>Thank you for registering with our mobile app. To complete registration process and be able to log in,"
				+ " click on the following link: "
				+ "<a href='http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
				+ "Final step to complete your registration" + "</a><br/><br/>"
				+ "Thank you! And we are waiting for you inside!";

		// The email body for recipients with non-HTML email clients.
		final String TEXTBODY = "Please verify your email address. "
				+ "Thank you for registering with our mobile app. To complete registration process and be able to log in,"
				+ " open then the following URL in your browser window: "
				+ " http://localhost:8080/verification-service/email-verification.html?token=$tokenValue"
				+ " Thank you! And we are waiting for you inside!";
		
		
		final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password</h1>"
			      + "<p>Hi, $firstName!</p> "
			      + "<p>Someone has requested to reset your password with our project. If it were not you, please ignore it."
			      + " otherwise please click on the link below to set a new password: " 
			      + "<a href='http://localhost:8080/verification-service/password-reset.html?token=$tokenValue'>"
			      + " Click this link to Reset Password"
			      + "</a><br/><br/>"
			      + "Thank you!";

	    // The email body for recipients with non-HTML email clients.
	    final String PASSWORD_RESET_TEXTBODY = "A request to reset your password "
			      + "Hi, $firstName! "
			      + "Someone has requested to reset your password with our project. If it were not you, please ignore it."
			      + " otherwise please open the link below in your browser window to set a new password:" 
			      + " http://localhost:8080/verification-service/password-reset.html?token=$tokenValue"
			      + " Thank you!";
	private JavaMailSender mailSender;

	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void verifyEmail(UserDto userDto) {

		// Update html body with token value
		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userDto.getEmail());
        mailMessage.setSubject(SUBJECT);
        mailMessage.setText(htmlBodyWithToken);
        mailMessage.setFrom(FROM);
        mailSender.send(mailMessage);
        System.out.println("Email verification sent to " + userDto.getEmail());
    }
}
