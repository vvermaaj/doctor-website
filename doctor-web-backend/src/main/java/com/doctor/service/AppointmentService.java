package com.doctor.service;

import com.doctor.dto.AppointmentDto;
import com.doctor.entity.Appointment;
import com.doctor.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.emailService = emailService;
    }

    public Appointment createAppointment(AppointmentDto appointmentDto) throws MessagingException {
        Appointment appointment = new Appointment();
        appointment.setName(appointmentDto.getName());
        appointment.setMobile(appointmentDto.getMobile());
        appointment.setEmail(appointmentDto.getEmail());
        appointment.setDate(appointmentDto.getDate());
        appointment.setReason(appointmentDto.getReason());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        emailService.sendAppointmentNotification(appointmentDto);

        return savedAppointment;
    }
}