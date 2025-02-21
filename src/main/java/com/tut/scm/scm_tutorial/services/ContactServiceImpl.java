package com.tut.scm.scm_tutorial.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tut.scm.scm_tutorial.Repository.ContactRepository;
import com.tut.scm.scm_tutorial.entities.Contact;
import com.tut.scm.scm_tutorial.entities.User;
import com.tut.scm.scm_tutorial.helpers.ResourceNotFoundException;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact save(Contact contact) {
        
        String contactId=UUID.randomUUID().toString();
        contact.setId(contactId);

        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {

      var contactOld=  contactRepository.findById(contact.getId())
            .orElseThrow(()->new ResourceNotFoundException("Contact not found"));

            contactOld.setName(contact.getName());
            contactOld.setEmail(contact.getEmail());
            contactOld.setPhoneNumber(contact.getPhoneNumber());
            contactOld.setDescription(contact.getDescription());
            contactOld.setAddress(contact.getAddress());
            contactOld.setPicture(contact.getPicture());
            contactOld.setFavourite(contact.isFavourite());
            contactOld.setWebsiteLink(contact.getWebsiteLink());
            contactOld.setLinkedInLink(contact.getLinkedInLink());
            contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());  


            return contactRepository.save(contactOld);
    }

    @Override
    public List<Contact> getAllContacts() {
        
        return contactRepository.findAll();
    }

    @Override
    public Contact getById(String id) {
       
        return contactRepository.findById(id)
        .orElseThrow(()->new ResourceNotFoundException("Contact not found with id: "+id));

    }

    @Override
    public void delete(String id) {

        var contact= contactRepository.findById(id)
        .orElseThrow(()->new ResourceNotFoundException("Contact not found with id: "+id));

        contactRepository.delete(contact);
    }

  

    @Override
    public List<Contact> getUserById(String userId) {
       
        return contactRepository.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUserName(User user, int page,int size, String sortBy, String direction) {

        Sort sort=direction.equals("desc")
            ? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();

        var pageable=PageRequest.of(page, size,sort);
        
        return contactRepository.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,
            User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
            String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }


}
