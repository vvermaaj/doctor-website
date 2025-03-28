package com.doctor.controller;

import com.doctor.dto.AppointmentDto;
import com.doctor.entity.Appointment;
import com.doctor.service.AppointmentService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDto appointmentDto) throws MessagingException {
        Appointment appointment = appointmentService.createAppointment(appointmentDto);
        return ResponseEntity.ok(appointment);
    }
}