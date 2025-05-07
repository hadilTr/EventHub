package com.example.mobile.mobile.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.mobile.mobile.Reservation.Model.Reservation;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReservationConfirmation(String email,String fullName, Reservation reservation) {
        String subject = "Confirmation de votre réservation";
        String body = "Bonjour " + fullName + ",\n\n" +
                "Nous vous confirmons votre réservation pour le spectacle:"+ reservation.getRepresentation().getSpectacle().getTitre() +"\n" +
                "Date : " + reservation.getRepresentation().getDate() + "\n" +
                "Lieu : " + reservation.getRepresentation().getLieu().getNom()+ "\n" +
                "Billets : " + reservation.getBillets().toString() + "\n" +
                "Nombre total : " + reservation.getNombreBillets() + "\n\n" +
                "Code confidentiel : abx52"+"\n" +
                "À bientôt !"+"\n"+
                "⚠️ Conservez précieusement ce code, il sera nécessaire pour accéder au spectacle.\r\n " + //
                                        "Nous vous recommandons de le sauvegarder ou de noter ce CC dans un endroit sûr ⚠️";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}