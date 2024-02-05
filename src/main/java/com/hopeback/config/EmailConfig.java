package com.hopeback.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

// Spring 프레임워크를 사용하여 이메일 전송 기능을 설정하는 클래스
@Configuration
public class EmailConfig {

    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String id;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;   // 인증 사용 여부
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;  // TLS 시작여부
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean startlls_required;  // TLS 필수 여부


    // Spring에서 이메일 전송을 위해서는 JavaMailSender 인터페이스를 구현한 구체적인 클래스가 필요
    // JavaMailSenderImpl 객체 생성
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }

    // javaMail의 속성을 설정하기 위한 properties 객체를 생성하는 역할
    // 사용 이유 : javaMailSender를 구성할 때 필요한 다양한 속성을 설정하기 위함.
    // properties 객체를 생성하고, 이 객체에 SMTP 속성들을 설정하여 javaMailSender에 전달함으로써 이메일 전송에 필요한 속성들을 설정.
    private Properties getMailProperties() {
        Properties pt = new Properties();
        pt.put("mail.smtp.socketFactory.port", port);
        pt.put("mail.smtp.auth", auth);
        pt.put("mail.smtp.starttls.enable", starttls);
        pt.put("mail.smtp.starttls.required", startlls_required);
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return pt;
    }

}
