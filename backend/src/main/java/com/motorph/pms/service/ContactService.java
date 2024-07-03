package com.motorph.ems.service;

import com.motorph.pms.model.Contact;

import java.util.List;

public interface ContactService {
    void addNewContact(Contact contact);
    List<Contact> getAllContacts();
    Contact getContactById(Long contactId);
    Contact updateContact(Long contactId, Contact contactDetails);
    void deleteContact(Long contactId);
}
