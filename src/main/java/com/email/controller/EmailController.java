package com.email.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.dto.EmailRequest;
import com.email.service.EmailService;

@RestController
@RequestMapping("/api/email")
@CrossOrigin("https://moradanica.github.io/")
public class EmailController {
	@Autowired
	private EmailService emailService;
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@PostMapping("/send")
	public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
		boolean success = emailService.sendTextEmail(emailRequest.getName(), emailRequest.getEmail(),
				emailRequest.getSubject(), emailRequest.getMessage());
		logger.info(emailRequest.toString());
		if (success) {
			return ResponseEntity.ok("Email sent successfully!");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
		}
	}

//	@PostMapping("/send-html")
//	public ResponseEntity<String> sendHtmlEmail(@RequestBody EmailRequest emailRequest) {
//		boolean success = emailService.sendHtmlEmail(emailRequest.getTo(), emailRequest.getSubject(),
//				emailRequest.getBody());
//		if (success) {
//			return ResponseEntity.ok("HTML email sent successfully!");
//		} else {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send HTML email");
//		}
//	}

	@GetMapping("test")
	public String test() {
		return "Successful";
	}
}