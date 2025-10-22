package com.yangshengzhou.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    /**
     * 发送验证码邮件
     * @param to 收件人邮箱
     * @param code 验证码
     * @throws Exception 发送失败时抛出异常
     */
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
            message.setSubject("LeafPan网盘系统验证码");
            
            String htmlContent = "<html>" +
                "<head></head>" +
                "<body>" +
                "<h2>LeafPan网盘系统验证码</h2>" +
                "<p>您好，</p>" +
                "<p>您正在进行验证操作，验证码为：</p>" +
                "<h3 style=\"color: #409EFF;\">" + code + "</h3>" +
                "<p>验证码有效期为5分钟，请及时使用。</p>" +
                "<p>如果您没有进行此操作，请忽略此邮件。</p>" +
                "<p>LeafPan团队</p>" +
                "</body>" +
                "</html>";
            
            message.setContent(htmlContent, "text/html;charset=UTF-8");
            
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("发送邮件失败: " + e.getMessage());
        }
    }
}