package com.doctor.service;

import com.doctor.dto.ContactDto;
import com.doctor.entity.Contact;
import com.doctor.repository.ContactRepository;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final EmailService emailService;

    public ContactService(ContactRepository contactRepository,
                          EmailService emailService) {
        this.contactRepository = contactRepository;
        this.emailService = emailService;
    }

    public Contact createContact(ContactDto contactDto) throws MessagingException {
        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setAddress(contactDto.getAddress());
        contact.setCity(contactDto.getCity());
        contact.setMobile(contactDto.getMobile());
        contact.setEmail(contactDto.getEmail());
        contact.setComments(contactDto.getComments());

        Contact savedContact = contactRepository.save(contact);
        emailService.sendContactNotification(contactDto);

        return savedContact;
    }
}