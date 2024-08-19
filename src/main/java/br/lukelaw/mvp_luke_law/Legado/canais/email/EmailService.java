package br.lukelaw.mvp_luke_law.Legado.canais.email;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;


import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String createEmailBody(String templateName, String variable, Object contextObject) {
        Context context = new Context();
        context.setVariable(variable, contextObject);
        return templateEngine.process(templateName, context);
    }

    /// Sobrecarga de Método para usar 2 Variavéis no template
    public String createEmailBody(String templateName, String variable, Object contextObject,
                                  String variable2, Object contextObject2) {

        Context context = new Context();
        context.setVariable(variable, contextObject);
        context.setVariable(variable2, contextObject2);

        return templateEngine.process(templateName, context);
    }


    public boolean sendEmail(Email email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("luke@legislator.com");
            helper.setTo(email.to());
            helper.setSubject(email.subject());
            helper.setText(email.body(), true); // true indica que é HTML

            mailSender.send(mimeMessage);

            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;

        }
    }
}