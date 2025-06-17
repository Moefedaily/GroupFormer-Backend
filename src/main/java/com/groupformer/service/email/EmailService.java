package com.groupformer.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.fromName}")
    private String fromName;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.email.verification.subject}")
    private String verificationSubject;

    @Value("${app.email.reset.subject}")
    private String resetSubject;


    public void sendVerificationEmail(String toEmail, String userName, String verificationToken) {
        try {
            String verificationUrl = frontendUrl + "/verify-email/" + verificationToken;

            String htmlContent = createVerificationEmailHtml(userName, verificationUrl);

            sendHtmlEmail(toEmail, verificationSubject, htmlContent);

            System.out.println("✅ Verification email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send verification email to: " + toEmail);
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }


    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        try {
            String resetUrl = frontendUrl + "/reset-password/" + resetToken;

            String htmlContent = createPasswordResetEmailHtml(userName, resetUrl);

            sendHtmlEmail(toEmail, resetSubject, htmlContent);

            System.out.println("Password reset email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send password reset email to: " + toEmail);
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }


    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        try {
            helper.setFrom(fromEmail, fromName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }


    private String createVerificationEmailHtml(String userName, String verificationUrl) {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Verify Your Email</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                "        .button { display: inline-block; background: #667eea; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; margin: 20px 0; }" +
                "        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 14px; }" +
                "        .warning { background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='header'>" +
                "        <h1>GroupFormer</h1>" +
                "        <p>Welcome to the smart group generation platform!</p>" +
                "    </div>" +
                "    <div class='content'>" +
                "        <h2>Hi " + userName + "! 👋</h2>" +
                "        <p>Thanks for registering with GroupFormer! We're excited to help you create amazing groups for your training activities.</p>" +
                "        <p>To get started, please verify your email address by clicking the button below:</p>" +
                "        <div style='text-align: center;'>" +
                "            <a href='" + verificationUrl + "' class='button'>Verify My Email</a>" +
                "        </div>" +
                "        <div class='warning'>" +
                "            <strong>⚠️ Important:</strong> This verification link will expire in 1 hour for security reasons." +
                "        </div>" +
                "        <p>Once verified, you'll be able to:</p>" +
                "        <ul>" +
                "            <li>Create and manage student lists</li>" +
                "            <li>Generate balanced groups with smart criteria</li>" +
                "            <li>Track group history and avoid duplicates</li>" +
                "            <li>Collaborate with other trainers</li>" +
                "        </ul>" +
                "        <p>If you didn't create this account, you can safely ignore this email.</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "        <p>Built with Spring Boot, JWT, Argon2, curiosity, and lots of coffee ☕</p>" +
                "        <p>If you have any questions, feel free to contact our support team.</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }


    private String createPasswordResetEmailHtml(String userName, String resetUrl) {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Reset Your Password</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                "        .button { display: inline-block; background: #e74c3c; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; margin: 20px 0; }" +
                "        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 14px; }" +
                "        .warning { background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='header'>" +
                "        <h1>🔒 Password Reset</h1>" +
                "        <p>GroupFormer Account Security</p>" +
                "    </div>" +
                "    <div class='content'>" +
                "        <h2>Hi " + userName + "! 👋</h2>" +
                "        <p>We received a request to reset your GroupFormer account password.</p>" +
                "        <p>Click the button below to create a new password:</p>" +
                "        <div style='text-align: center;'>" +
                "            <a href='" + resetUrl + "' class='button'>Reset My Password</a>" +
                "        </div>" +
                "        <div class='warning'>" +
                "            <strong>⚠️ Security Notice:</strong> This reset link will expire in 1 hour. If you didn't request this reset, please ignore this email and your password will remain unchanged." +
                "        </div>" +
                "        <p>For your security, never share this link with anyone.</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "        <p>Built with Spring Boot, JWT, Argon2, curiosity, and lots of coffee ☕</p>" +
                "        <p>If you need help, contact our support team.</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}