package esprit.demandegestion.services;

import esprit.demandegestion.entity.Demande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String adminEmail = "admin@votreapp.com"; // 🔥 adresse de l'admin

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("admin@admin.com"); // ou une adresse que vous configurez dans application.properties

        try {
            mailSender.send(message);
            System.out.println("Email envoyé avec succès à : " + to);
        } catch (Exception e) {
            System.err.println("Erreur lors de l’envoi de l’email à " + to + " : " + e.getMessage());
        }
    }

    public void sendDemandeNotificationToAdmin(Demande demande) {
        String subject = "Nouvelle demande reçue de " + demande.getEmail();
        String body = "Bonjour Admin,\n\nUne nouvelle demande a été soumise.\n\n" +
                "📧 Email client : " + demande.getEmail() + "\n" +
                "📅 Date demande : " + demande.getDate_demande() + "\n" +
                "🗂 Type : " + demande.getTypeDemande() + "\n" +
                "📝 Description : " + demande.getDescription() + "\n" +

                "Merci de traiter cette demande dans les plus brefs délais.";
        sendEmail(adminEmail, subject, body);
    }
}