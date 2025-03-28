package com.doctor.dto;

import lombok.Data;

@Data
public class ContactDto {
    private String name;
    private String address;
    private String city;
    private String mobile;
    private String email;
    private String comments;
}