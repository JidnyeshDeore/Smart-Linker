package com.tut.scm.scm_tutorial.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tut.scm.scm_tutorial.entities.Contact;
import com.tut.scm.scm_tutorial.entities.User;


public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAllContacts();

    Contact getById(String id);

    void delete(String id);

    

    List<Contact> getUserById(String userId);

    Page<Contact> getByUserName(User user, int page,int size, String sortBy,String direction);

    Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,
            User user);

}
