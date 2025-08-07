package com.example.pnmbackend.service;

import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.entities.Subscribe;
import com.example.pnmbackend.model.entities.TokenRecovery;
import com.example.pnmbackend.repository.SubscribeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.sender.name}")
    private String from;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${newsletter.link.facebook}")
    private String linkFacebook;

    @Value("${newsletter.logo.path}")
    private String logoPath;

    @Value("icon/instagram_icon.jpg")
    private String instaPath;

    @Value(("icon/facebook_icon.png"))
    private String facePath;

    @Value(("icon/whatsapp_icon.png"))
    private  String whatPath;

    @Value("${recovery.url.path}")
    private String recoverPath;

    //NewsLetter email!!

    public void sendNewsletterToAllUsers(NewsLetter newsletter) {
        List<Subscribe> subscribes = subscribeRepository.findAll();
        for (Subscribe subscribe : subscribes) {
            MimeMessage message = emailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(new InternetAddress(from + " <" + fromEmail + ">"));
                helper.setTo(subscribe.getEmail());
                helper.setSubject(newsletter.getTitolo());

                Context context = new Context();
                context.setVariable("newsletter", newsletter);
                context.setVariable("logoPath", logoPath); // Assuming logoPath is correctly handled as before
                context.setVariable("linkFacebook", linkFacebook);

                // Process the HTML template
                String htmlContent = templateEngine.process("newsletter_template", context);
                helper.setText(htmlContent, true);

                // Add the logo as before
                helper.addInline("logo", new ClassPathResource(logoPath));
                // Add icons
                helper.addInline("facebook_icon", new ClassPathResource(facePath));
                helper.addInline("instagram_icon", new ClassPathResource(instaPath));
                helper.addInline("whatsapp_icon", new ClassPathResource(whatPath));
                if (newsletter.getImage() != null)
                 helper.addInline("base64", new ByteArrayDataSource(newsletter.getImage(),"image/jpeg"));

                emailSender.send(message);
            } catch (MessagingException e) {
                // Handle exception
                e.printStackTrace();
            }
        }
    }


    //Welcome email!!

    public void sendWelcomeEmailToProducer(Producer producer) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(from + " <" + fromEmail + ">"));
            helper.setTo(producer.getEmail()); // Get the email address from the Producer object

            // Generate the subject for the welcome email
            String subject = "Benvenuto, " + producer.getNome() + "!";
            helper.setSubject(subject);

            // Create the Thymeleaf context and pass the title, content, and logoPath variables
            Context context = new Context();
            context.setVariable("titolo", subject); // Set the title
            context.setVariable("contenuto", "Grazie per esserti iscritto  nella nostra piattaforma, siamo lieti di averti come nostro utente!"); // Set the content
            context.setVariable("logoPath", logoPath); // Set the logo path
            context.setVariable("linkFacebook", linkFacebook);

            // Process the HTML template with Thymeleaf
            String htmlContent = templateEngine.process("welcome_email_template", context);

            // Set the HTML content of the email
            helper.setText(htmlContent, true);
            // Add logo
            helper.addInline("logo", new ClassPathResource(logoPath));
            helper.addInline("facebook_icon", new ClassPathResource(facePath));
            // Add icons
            helper.addInline("instagram_icon", new ClassPathResource(instaPath));
            helper.addInline("whatsapp_icon", new ClassPathResource(whatPath));

            emailSender.send(message);
        } catch (MessagingException e) {
            // Handle exception
        }
    }

    //Change password email!!!

    public void sendPasswordResetEmail(TokenRecovery tokenRecovery) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(from + " <" + fromEmail + ">"));
            helper.setTo(tokenRecovery.getEmail());

            String subject = "Password Reset Request";
            helper.setSubject(subject);

            Context context = new Context();
            String recoverUrl = String.format("%s/%s", recoverPath, tokenRecovery.getID());
            context.setVariable("recoverPath", recoverUrl);
            context.setVariable("linkFacebook", linkFacebook);

            String htmlContent = templateEngine.process("reset_password_email_template", context);

            helper.setText(htmlContent, true);
            // Add the logo
            helper.addInline("logo", new ClassPathResource(logoPath));
            //Add the icons
            helper.addInline("facebook_icon", new ClassPathResource(facePath));
            helper.addInline("instagram_icon", new ClassPathResource(instaPath));
            helper.addInline("whatsapp_icon", new ClassPathResource(whatPath));

            emailSender.send(message);

    }


}