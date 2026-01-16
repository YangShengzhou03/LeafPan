package com.yangshengzhou.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@Service
public class EmailService {

    @Value("${email.sender}")
    private String senderEmail;
    
    @Value("${email.password}")
    private String senderPassword;
    
    @Value("${email.smtp.server}")
    private String smtpServer;
    
    @Value("${email.smtp.port}")
    private int smtpPort;

    public void sendVerificationCode(String to, String code) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("LeafAuto <" + senderEmail + ">"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("【轻羽云盘】LeafPan系统验证码");
            
            String htmlContent = "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f5f7fa; }" +
                ".email-container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }" +
                ".header h1 { margin: 0; font-size: 28px; font-weight: 600; }" +
                ".header p { margin: 10px 0 0; opacity: 0.9; }" +
                ".content { padding: 40px; }" +
                ".greeting { font-size: 18px; color: #333; margin-bottom: 20px; }" +
                ".code-section { background: #f8f9fa; border-radius: 8px; padding: 25px; text-align: center; margin: 30px 0; border-left: 4px solid #409EFF; }" +
                ".verification-code { font-size: 42px; font-weight: bold; color: #409EFF; letter-spacing: 8px; margin: 15px 0; }" +
                ".instructions { color: #666; line-height: 1.6; margin-bottom: 20px; }" +
                ".footer { background: #f8f9fa; padding: 25px; text-align: center; color: #666; font-size: 14px; border-top: 1px solid #e9ecef; }" +
                ".warning { color: #ff6b6b; font-size: 13px; margin-top: 10px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +
                "<div class=\"header\">" +
                "<h1>LeafPan 轻羽云盘</h1>" +
                "<p>安全可靠的云存储解决方案</p>" +
                "</div>" +
                "<div class=\"content\">" +
                "<p class=\"greeting\">尊敬的用户，您好！</p>" +
                "<p class=\"instructions\">您正在进行账户验证，为确保您的账户安全，请使用以下验证码完成验证：</p>" +
                "<div class=\"code-section\">" +
                "<p style=\"margin: 0 0 10px; color: #666;\">您的验证码</p>" +
                "<div class=\"verification-code\">" + code + "</div>" +
                "<p style=\"margin: 10px 0 0; color: #999; font-size: 14px;\">有效期 5 分钟</p>" +
                "</div>" +
                "<p class=\"instructions\">请勿将此验证码透露给他人。\n 如非本人操作，请立即修改账户密码并联系客服。</p>" +
                "<p class=\"warning\">⚠️ 注意：LeafPan团队绝不会向您索要验证码</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>感谢您使用 LeafPan 轻羽云盘服务</p>" +
                "<p>如有问题请联系开发者：yangsz03@foxmail.com</p>" +
                "<p style=\"margin-top: 15px; font-size: 12px; color: #999;\">© 2025 LeafPan Team. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
            
            message.setContent(htmlContent, "text/html;charset=UTF-8");
            
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("发送邮件失败: " + e.getMessage());
        }
    }
    
    public void sendPasswordResetCode(String to, String code) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("LeafAuto <" + senderEmail + ">"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("【轻羽云盘】LeafPan密码重置验证码");
            
            String htmlContent = "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f5f7fa; }" +
                ".email-container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }" +
                ".header { background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%); color: white; padding: 30px; text-align: center; }" +
                ".header h1 { margin: 0; font-size: 28px; font-weight: 600; }" +
                ".header p { margin: 10px 0 0; opacity: 0.9; }" +
                ".content { padding: 40px; }" +
                ".greeting { font-size: 18px; color: #333; margin-bottom: 20px; }" +
                ".code-section { background: #f8f9fa; border-radius: 8px; padding: 25px; text-align: center; margin: 30px 0; border-left: 4px solid #ff6b6b; }" +
                ".verification-code { font-size: 42px; font-weight: bold; color: #ff6b6b; letter-spacing: 8px; margin: 15px 0; }" +
                ".instructions { color: #666; line-height: 1.6; margin-bottom: 20px; }" +
                ".footer { background: #f8f9fa; padding: 25px; text-align: center; color: #666; font-size: 14px; border-top: 1px solid #e9ecef; }" +
                ".warning { color: #ff6b6b; font-size: 13px; margin-top: 10px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +
                "<div class=\"header\">" +
                "<h1>LeafPan 轻羽云盘</h1>" +
                "<p>密码重置验证</p>" +
                "</div>" +
                "<div class=\"content\">" +
                "<p class=\"greeting\">尊敬的用户，您好！</p>" +
                "<p class=\"instructions\">您正在申请重置账户密码，为确保您的账户安全，请使用以下验证码完成密码重置：</p>" +
                "<div class=\"code-section\">" +
                "<p style=\"margin: 0 0 10px; color: #666;\">您的密码重置验证码</p>" +
                "<div class=\"verification-code\">" + code + "</div>" +
                "<p style=\"margin: 10px 0 0; color: #999; font-size: 14px;\">有效期 5 分钟</p>" +
                "</div>" +
                "<p class=\"instructions\">请勿将此验证码透露给他人。\n 如非本人操作，请立即联系客服。</p>" +
                "<p class=\"warning\">⚠️ 注意：LeafPan团队绝不会向您索要验证码</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>感谢您使用 LeafPan 轻羽云盘服务</p>" +
                "<p>如有问题请联系开发者：yangsz03@foxmail.com</p>" +
                "<p style=\"margin-top: 15px; font-size: 12px; color: #999;\">© 2025 LeafPan Team. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
            
            message.setContent(htmlContent, "text/html;charset=UTF-8");
            
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("发送密码重置邮件失败: " + e.getMessage());
        }
    }
}
