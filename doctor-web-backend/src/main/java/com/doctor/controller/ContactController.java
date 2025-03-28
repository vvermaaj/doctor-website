package com.doctor.controller;

import com.doctor.dto.ContactDto;
import com.doctor.entity.Contact;
import com.doctor.service.ContactService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:5173")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody ContactDto contactDto) throws MessagingException {
        Contact contact = contactService.createContact(contactDto);
        return ResponseEntity.ok(contact);
    }
}