package com.rtcc.demo.services;

import com.rtcc.demo.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.scheduling.annotation.Async;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendVerificationEmail(User user) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String verificationLink = "http://localhost:8080/verify-email?token=" + user.getVerificationToken();

        Context context = new Context();
        context.setVariable("subject", "Confirme seu e-mail");
        context.setVariable("message",
                "Olá, senhor(a) " + user.getName() + ".<br/><br/>" +
                        "Para continuarmos, pedimos que clique no link abaixo para verificar seu e-mail:<br/>" +
                        "<a href=\"" + verificationLink + "\">Verificar E-mail</a>");

        String htmlContent = templateEngine.process("email-template", context);

        helper.setTo(user.getEmail());
        helper.setSubject("RTCCIF: Confirme seu e-mail");
        helper.setText(htmlContent, true);
        helper.setFrom("rtcc.ifnmg@gmail.com");

        mailSender.send(mimeMessage);
    }
}
