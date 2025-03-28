package com.doctor.service;

import com.doctor.dto.AppointmentDto;
import com.doctor.dto.ContactDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String doctorEmail;
    private final String clinicName;

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username}") String fromEmail,
                        @Value("${doctor.email}") String doctorEmail,
                        @Value("${clinic.name}") String clinicName) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.doctorEmail = doctorEmail;
        this.clinicName = clinicName;
    }

    public void sendAppointmentNotification(AppointmentDto appointment) throws MessagingException {
        sendDoctorAppointmentAlert(appointment);
        sendPatientAppointmentConfirmation(appointment);
    }

    public void sendContactNotification(ContactDto contact) throws MessagingException {
        sendDoctorContactAlert(contact);
        sendUserContactConfirmation(contact);
    }

    private void sendDoctorAppointmentAlert(AppointmentDto appointment) {
        String subject = String.format("New Appointment: %s - %s", appointment.getName(), appointment.getDate());
        String text = String.format(
                "New Appointment Booking:\n\nPatient: %s\nMobile: %s\nEmail: %s\n" +
                        "Date: %s\nReason: %s\n\nClinic: %s",
                appointment.getName(), appointment.getMobile(), appointment.getEmail(),
                appointment.getDate(), appointment.getReason(), clinicName
        );
        sendSimpleEmail(doctorEmail, subject, text);
    }

    private void sendPatientAppointmentConfirmation(AppointmentDto appointment) throws MessagingException {
        String subject = "Your Appointment Confirmation - " + clinicName;
        String htmlContent = String.format(
                "<html><body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                        "<h2 style='color: #047857;'>Appointment Confirmation</h2>" +
                        "<p>Dear %s,</p><p>Your appointment has been confirmed with %s:</p>" +
                        "<div style='background: #f0fdf4; padding: 1rem; border-radius: 0.5rem;'>" +
                        "<p><strong>Date:</strong> %s</p><p><strong>Reason:</strong> %s</p></div>" +
                        "<p>We'll send you a reminder one day before your appointment.</p>" +
                        "<p style='margin-top: 2rem;'>Best regards,</p>" +
                        "<p><strong>Dr. Sudhir S. Jadon</strong><br>%s</p></body></html>",
                appointment.getName(), clinicName, appointment.getDate(),
                appointment.getReason(), clinicName
        );
        sendHtmlEmail(appointment.getEmail(), subject, htmlContent);
    }

    private void sendDoctorContactAlert(ContactDto contact) {
        String subject = "New Contact Form: " + contact.getName();
        String text = String.format(
                "New Contact Submission:\n\nName: %s\nAddress: %s\nCity: %s\n" +
                        "Mobile: %s\nEmail: %s\n\nMessage:\n%s\n\nClinic: %s",
                contact.getName(), contact.getAddress(), contact.getCity(),
                contact.getMobile(), contact.getEmail(), contact.getComments(), clinicName
        );
        sendSimpleEmail(doctorEmail, subject, text);
    }

    private void sendUserContactConfirmation(ContactDto contact) throws MessagingException {
        String subject = "Thank you for contacting " + clinicName;
        String htmlContent = String.format(
                "<html><body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                        "<h2 style='color: #047857;'>We've Received Your Message</h2>" +
                        "<p>Dear %s,</p><p>Thank you for reaching out to %s.</p>" +
                        "<div style='background: #f0fdf4; padding: 1rem; border-radius: 0.5rem;'>" +
                        "<p><strong>Your Message:</strong></p><p>%s</p></div>" +
                        "<p>We'll revert to you as soon as possible.</p>" +
                        "<p style='margin-top: 2rem;'>Best regards,</p>" +
                        "<p><strong>Dr. Sudhir S. Jadon</strong><br>%s</p></body></html>",
                contact.getName(), clinicName, contact.getComments(), clinicName
        );
        sendHtmlEmail(contact.getEmail(), subject, htmlContent);
    }

    private void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending simple email: " + e.getMessage());
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}