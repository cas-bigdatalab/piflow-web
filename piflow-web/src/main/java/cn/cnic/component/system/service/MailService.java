package cn.cnic.component.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName MailService
 * @Description 邮件服务类，用于发送各种系统通知邮件（如忘记密码、注册确认等）
 * @Author ty
 * @Date 2025/5/30 16:51
 */
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String fromEmail;

    //    @Autowired
    public MailService(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.javaMailSender = javaMailSender;
        this.fromEmail = fromEmail;
    }

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件正文内容
     */
    public void sendMail(String to, String subject, String content) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(content);
//        javaMailSender.send(message);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, false); // 第二个参数为 true 支持 HTML
        helper.setFrom(fromEmail);
        javaMailSender.send(mimeMessage);
    }
}

