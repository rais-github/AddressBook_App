package com.example.AddressBook.service.implimentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {


    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;
    public EmailService(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }


    public void sendEmail(String to, String subject, String message, String replyTo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.setFrom(senderEmail);
            helper.setReplyTo(replyTo);

            mailSender.send(mimeMessage);
            System.out.println("✅ Email sent successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }




    public void sendEmailWithAttachment(String to, String subject, String message, String attachmentPath, String replyTo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.setFrom(senderEmail);
            helper.setReplyTo(replyTo);

            File attachment = new File(attachmentPath);
            if (!attachment.exists()) {
                throw new IllegalArgumentException("Attachment file not found: " + attachmentPath);
            }
            FileSystemResource file = new FileSystemResource(attachment);
            helper.addAttachment(file.getFilename(), file);

            mailSender.send(mimeMessage);
            System.out.println("✅ Email with attachment sent successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to send email with attachment: " + e.getMessage());
        }
    }

}
