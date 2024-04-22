//https://www.geeksforgeeks.org/spring-boot-sending-email-via-smtp/

package xyz.magicraft.longshort.ssf.account.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import xyz.magicraft.longshort.ssf.account.model.Email;
import xyz.magicraft.longshort.ssf.account.repository.EmailRepository;


@Service
public class EmailService{

	@Autowired 
	private JavaMailSender javaMailSender;
	
	
	@Autowired
	private EmailRepository emailRepository;
	
	

	@Value("${spring.mail.username}") 
	private String sender;
	
	
	public boolean sendMail(String recipient,String subject,String body) {
		Email email = new Email();
		
		email.setRecipient(recipient);
		email.setSender(sender);
		email.setSubject(subject);
		email.setBody(body );
		
		email = this.emailRepository.save(email);

		// Try block to check for exceptions
		try {

			// Creating a simple mail message
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
			mailMessage.setFrom(sender);
			mailMessage.setTo(recipient);
			mailMessage.setText(email.getBody());
			mailMessage.setSubject(email.getSubject());

			// Sending the mail
			javaMailSender.send(mailMessage);
			
//			return "Mail Sent Successfully...";
			return true;
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
			e.printStackTrace();
//			return "Error while Sending Mail";
			return false;
		}
	}
	

//	// Method 1
//	// To send a simple email
//	public String sendVerifyMail(String recipient) {
//
//		String code = randomCodeGenerator.dec6();
//		
//		Optional<User> userOptional = this.userRepository.findByEmail(recipient);
//		
//		User user = null;
//		if (userOptional.isPresent()) {
//			user = userOptional.get();
//		}else {
//			user = new User();
//			user.setEmail(recipient);
//		}
//		user.setCode(code);
//		
//		this.userRepository.save(user);
//		
//		
//		
//		Email email = new Email();
//		
//		email.setRecipient(recipient);
//		email.setSender(sender);
//		email.setSubject("验证邮箱");
//		email.setBody(String.format("邮箱验证码为【 %s 】", code) );
//		
//		email = this.emailRepository.save(email);
//
//		// Try block to check for exceptions
//		try {
//
//			// Creating a simple mail message
//			SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//			// Setting up necessary details
//			mailMessage.setFrom(sender);
//			mailMessage.setTo(recipient);
//			mailMessage.setText(email.getBody());
//			mailMessage.setSubject(email.getSubject());
//
//			// Sending the mail
//			javaMailSender.send(mailMessage);
//			
//			return "Mail Sent Successfully...";
//		}
//
//		// Catch block to handle the exceptions
//		catch (Exception e) {
//			e.printStackTrace();
//			return "Error while Sending Mail";
//		}
//	}

}
