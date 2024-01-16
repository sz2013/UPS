package com.zjf.ups.util;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MailUtil.class);

    private static JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        MailUtil.javaMailSender = javaMailSender;
    }

    private static String from;

    @Value("${ups.mailFrom}")
    public void setMailFrom(String from) {
        MailUtil.from = from;
    }

    private static String to;

    @Value("${ups.mailTo}")
    public void setMailTo(String to) {
        MailUtil.to = to;
    }

    public static void sendEmail(String subject, String text) {
        logger.info("开始发送邮件, to={}, subject={}, text={}", to, subject, text);
        long start = System.currentTimeMillis();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("发送邮件失败, to={}, subject={}, text={}", to, subject, text, e);
            return;
        }
        logger.info("发送邮件成功, to={}, subject={}, text={}, 耗时={}ms", to, subject, text, System.currentTimeMillis() - start);
    }
}
