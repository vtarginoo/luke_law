package br.lukelaw.mvp_luke_law.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


@Configuration
public class MailConfig {

    @Bean
        public JavaMailSender getJavaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.mail.com");
            mailSender.setPort(587);

            mailSender.setUsername("luke@legislator.com");
            mailSender.setPassword("gsJsH-nTVyC-LnwKr-pRrAs");
           // mailSender.setPassword("lukecompany12");




        Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");


        return mailSender;
        }
    }


//JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//mailSender.setHost("sandbox.smtp.mailtrap.io");
//mailSender.setPort(587);
//mailSender.setUsername("56997f5c1b8bdb");
//mailSender.setPassword("50df4ce92511b4");