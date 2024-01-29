package com.example.MCM.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {
    private static final String FROM_ADDRESS = "usedcoding@gmail.com";

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;


    //인증 메일 존성
    @Bean
    public JavaMailSender javaMailSender() {

        Properties mailProperties = new Properties();
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.debug", "true");
        mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        mailSender.setDefaultEncoding("utf-8");
        return mailSender;
    }

//    // 비밀번호 전송
//    public void mailSend(MailDto mailDto){
//
//        Properties mailProperties = new Properties();
//        mailProperties.put("mail.transport.protocol", "smtp");
//        mailProperties.put("mail.smtp.auth", "true");
//        mailProperties.put("mail.smtp.starttls.enable", "true");
//        mailProperties.put("mail.smtp.debug", "true");
//        mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//        mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
//
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setJavaMailProperties(mailProperties);
//        System.out.println("이멜 전송 완료!");
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mailDto.getAddress());
//        message.setFrom(FROM_ADDRESS);
//        message.setSubject(mailDto.getTitle());
//        message.setText(mailDto.getMessage());
//
//        mailSender.send(message);
//    }
}
