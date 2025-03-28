package com.doctor.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentDto {
    private String name;
    private String mobile;
    private String email;
    private LocalDate date;
    private String reason;
}